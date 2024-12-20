package com.c242_ps302.sehatin.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import com.c242_ps302.sehatin.BuildConfig
import com.c242_ps302.sehatin.data.local.dao.PredictionDao
import com.c242_ps302.sehatin.data.local.dao.RecommendationDao
import com.c242_ps302.sehatin.data.local.dao.UserDao
import com.c242_ps302.sehatin.data.local.room.SehatinDatabase
import com.c242_ps302.sehatin.data.preferences.SehatinAppPreferences
import com.c242_ps302.sehatin.data.remote.AuthApiService
import com.c242_ps302.sehatin.data.remote.HealthApiService
import com.c242_ps302.sehatin.data.remote.NewsApiService
import com.c242_ps302.sehatin.data.remote.PredictionApiService
import com.c242_ps302.sehatin.data.remote.RecommendationApiService
import com.c242_ps302.sehatin.data.remote.UserApiService
import com.c242_ps302.sehatin.data.repository.AndroidConnectivityObserver
import com.c242_ps302.sehatin.data.repository.AuthRepository
import com.c242_ps302.sehatin.data.repository.HealthRepository
import com.c242_ps302.sehatin.data.repository.NewsRepository
import com.c242_ps302.sehatin.data.repository.PredictionRepository
import com.c242_ps302.sehatin.data.repository.RecommendationRepository
import com.c242_ps302.sehatin.data.repository.UserRepository
import com.c242_ps302.sehatin.data.utils.Constants
import com.c242_ps302.sehatin.domain.ConnectivityObserver
import com.c242_ps302.sehatin.ui.utils.LanguageChangeHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideWorkManager(context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideSehatinAppPreferences(context: Context): SehatinAppPreferences {
        return SehatinAppPreferences(context)
    }

    @Provides
    @Singleton
    fun provideLanguageChangeHelper(): LanguageChangeHelper {
        return LanguageChangeHelper()
    }

    @Provides
    @Singleton
    fun provideConnectivityObserver(context: Context): ConnectivityObserver {
        return AndroidConnectivityObserver(context)
    }

    @Provides
    @Singleton
    fun provideAuthApiService(): AuthApiService {
        val loggingInterceptor = HttpLoggingInterceptor().setLevel(
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE
        )

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(Constants.SEHATIN_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideRecommendationApiService(): RecommendationApiService {
        val loggingInterceptor = HttpLoggingInterceptor().setLevel(
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE
        )

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(Constants.RECOMMENDATION_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RecommendationApiService::class.java)
    }

    @Provides
    @Singleton
    fun providePredictionApiService(
        preferences: SehatinAppPreferences,
    ): PredictionApiService {
        val authInterceptor = Interceptor { chain ->
            val originalRequest = chain.request()
            val newRequest = runBlocking {
                withContext(Dispatchers.IO) {
                    val token = preferences.getToken().first()
                    originalRequest.newBuilder()
                        .apply {
                            if (token.isNotEmpty()) {
                                addHeader("Authorization", "Bearer $token")
                            }
                        }
                        .build()
                }
            }
            chain.proceed(newRequest)
        }

        val loggingInterceptor = HttpLoggingInterceptor().setLevel(
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE
        )
        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(Constants.PREDICTION_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(PredictionApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideHealthApiService(
        preferences: SehatinAppPreferences,
    ): HealthApiService {
        val authInterceptor = Interceptor { chain ->
            val originalRequest = chain.request()
            val newRequest = runBlocking {
                withContext(Dispatchers.IO) {
                    val token = preferences.getToken().first()

                    originalRequest.newBuilder()
                        .apply {
                            if (token.isNotEmpty()) {
                                addHeader("Authorization", "Bearer $token")
                            }
                        }
                        .build()
                }
            }
            chain.proceed(newRequest)
        }

        val loggingInterceptor = HttpLoggingInterceptor().setLevel(
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE
        )
        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(Constants.SEHATIN_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(HealthApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserApiService(
        preferences: SehatinAppPreferences,
    ): UserApiService {
        val authInterceptor = Interceptor { chain ->
            val originalRequest = chain.request()
            val newRequest = runBlocking {
                withContext(Dispatchers.IO) {
                    val token = preferences.getToken().first()

                    originalRequest.newBuilder()
                        .apply {
                            if (token.isNotEmpty()) {
                                addHeader("Authorization", "Bearer $token")
                            }
                        }
                        .build()
                }
            }
            chain.proceed(newRequest)
        }

        val loggingInterceptor = HttpLoggingInterceptor().setLevel(
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE
        )
        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(Constants.SEHATIN_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(UserApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideNewsApiService(): NewsApiService {
        val authInterceptor = Interceptor { chain ->
            val newRequest = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer ${Constants.NEWS_API_KEY}")
                .build()
            chain.proceed(newRequest)
        }
        val loggingInterceptor = HttpLoggingInterceptor().setLevel(
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE
        )
        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(Constants.NEWS_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(NewsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideSehatinDatabase(application: Application): SehatinDatabase {
        return Room.databaseBuilder(
            application,
            SehatinDatabase::class.java,
            Constants.SEHATIN_DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun providePredictionDao(sehatinDatabase: SehatinDatabase): PredictionDao {
        return sehatinDatabase.predictionDao()
    }

    @Provides
    @Singleton
    fun provideRecommendationDao(sehatinDatabase: SehatinDatabase): RecommendationDao {
        return sehatinDatabase.recommendationDao()
    }

    @Provides
    @Singleton
    fun provideUserDao(sehatinDatabase: SehatinDatabase): UserDao {
        return sehatinDatabase.userDao()
    }

    @Provides
    @Singleton
    fun provideHealthRepository(
        healthApiService: HealthApiService,
        userDao: UserDao,
        recommendationDao: RecommendationDao,
        predictionDao: PredictionDao,
        context: Context,
    ): HealthRepository {
        return HealthRepository(
            userDao,
            healthApiService,
            recommendationDao,
            predictionDao,
            context
        )
    }

    @Provides
    @Singleton
    fun providePredictionRepository(
        predictionApiService: PredictionApiService,
    ): PredictionRepository {
        return PredictionRepository(predictionApiService)
    }

    @Provides
    @Singleton
    fun provideRecommendationRepository(
        recommendationApiService: RecommendationApiService,
        userDao: UserDao,
    ): RecommendationRepository {
        return RecommendationRepository(recommendationApiService, userDao)
    }

    @Provides
    @Singleton
    fun provideNewsRepository(
        newsApiService: NewsApiService,
    ): NewsRepository {
        return NewsRepository(newsApiService)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        context: Context,
        authApiService: AuthApiService,
        userDao: UserDao,
        preferences: SehatinAppPreferences,
        predictionDao: PredictionDao,
        recommendationDao: RecommendationDao
    ): AuthRepository {
        return AuthRepository(
            context,
            authApiService,
            userDao,
            preferences,
            predictionDao,
            recommendationDao
        )
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        userApiService: UserApiService,
        userDao: UserDao,
    ): UserRepository {
        return UserRepository(userApiService, userDao)
    }
}