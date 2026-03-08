package com.test.memoapp.core.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.skydoves.sandwich.retrofit.adapters.ApiResponseCallAdapterFactory
import com.test.memoapp.BuildConfig
import com.test.memoapp.core.data.TokenManager
import com.test.memoapp.memo.network.MemoApiService
import com.test.memoapp.memo.network.MemoTagApiService
import com.test.memoapp.memo.network.TagApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetWorkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(tokenManager: TokenManager): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val original = chain.request()
                val addHeader = original.newBuilder()
                    .addHeader("apikey", BuildConfig.SUPABASE_ANON_CODE)
                    .addHeader("Content-Type", "application/json")
                val request = addHeader.build()
                chain.proceed(request)
            }
            .addInterceptor { chain ->
                val userInfo = runBlocking { tokenManager.userFlow.first() }
                val request = chain.request().newBuilder()
                    .apply {
                        userInfo?.let {
                            addHeader("Authorization", "Bearer ${it.accessToken}")
                        }
                    }.build()
                chain.proceed(request)
            }
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.SUPABASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApiService(retrofit: Retrofit): AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideMemoApiService(retrofit: Retrofit) : MemoApiService {
        return retrofit.create(MemoApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideTagApiService(retrofit: Retrofit) : TagApiService {
        return retrofit.create(TagApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideMemoTagApiService(retrofit: Retrofit) : MemoTagApiService {
        return retrofit.create(MemoTagApiService::class.java)
    }
}