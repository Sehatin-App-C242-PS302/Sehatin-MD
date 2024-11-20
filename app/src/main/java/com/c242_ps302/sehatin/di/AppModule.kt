package com.c242_ps302.sehatin.di

import android.app.Application
import android.content.Context
import com.c242_ps302.sehatin.BuildConfig
import com.c242_ps302.sehatin.data.remote.NewsApiService
import com.c242_ps302.sehatin.data.repository.NewsRepository
import com.c242_ps302.sehatin.data.utils.Constants
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
    fun provideNewsRepository(
        newsApiService: NewsApiService,
    ): NewsRepository {
        return NewsRepository(newsApiService)
    }
}