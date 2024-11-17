package com.c242_ps302.sehatin.di

import com.c242_ps302.sehatin.data.remote.NewsApiService
import com.c242_ps302.sehatin.data.repository.NewsRepositoryImpl
import com.c242_ps302.sehatin.data.utils.Constants
import com.c242_ps302.sehatin.domain.repository.NewsRepository
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .header("X-Api-Key", Constants.NEWS_API_KEY)
                    .method(original.method, original.body)
                    .build()
                chain.proceed(request)
            }
            .build()
    }

    @Provides
    @Singleton
    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true
        }
    }

    @Provides
    @Singleton
    fun provideNewsApiService(okHttpClient: OkHttpClient, json: Json): NewsApiService {
        val contentType = "application/json".toMediaType()
        val newsRetrofit = Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .baseUrl(Constants.NEWS_BASE_URL)
            .build()
        return newsRetrofit.create(NewsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideNewsRepository(
        newsApiService: NewsApiService,
    ): NewsRepository {
        return NewsRepositoryImpl(newsApiService)
    }
}