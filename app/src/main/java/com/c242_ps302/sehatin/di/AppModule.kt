package com.c242_ps302.sehatin.di

import android.app.Application
import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.room.Room
import androidx.work.Configuration
import androidx.work.WorkManager
import com.c242_ps302.sehatin.BuildConfig
import com.c242_ps302.sehatin.data.local.dao.RecommendationDao
import com.c242_ps302.sehatin.data.local.dao.UserDao
import com.c242_ps302.sehatin.data.local.room.SehatinDatabase
import com.c242_ps302.sehatin.data.preferences.SehatinAppPreferences
import com.c242_ps302.sehatin.data.remote.AuthApiService
import com.c242_ps302.sehatin.data.remote.NewsApiService
import com.c242_ps302.sehatin.data.remote.RecommendationApiService
import com.c242_ps302.sehatin.data.repository.AndroidConnectivityObserver
import com.c242_ps302.sehatin.data.repository.NewsRepository
import com.c242_ps302.sehatin.data.utils.Constants
import com.c242_ps302.sehatin.domain.ConnectivityObserver
import com.c242_ps302.sehatin.presentation.notification.NotificationHelper
import com.c242_ps302.sehatin.presentation.utils.LanguageChangeHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
    fun provideNewsRepository(
        newsApiService: NewsApiService,
    ): NewsRepository {
        return NewsRepository(newsApiService)
    }

    @Provides
    @Singleton
    fun provideWorkManager(application: Application): WorkManager {
        return WorkManager.getInstance(application)
    }

    @Provides
    @Singleton
    fun provideNotificationHelper(
        context: Context,
        preferences: SehatinAppPreferences,
    ): NotificationHelper {
        return NotificationHelper(context, preferences)
    }

    @Provides
    @Singleton
    fun provideWorkManagerConfiguration(
        workerFactory: HiltWorkerFactory,
    ): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }
}