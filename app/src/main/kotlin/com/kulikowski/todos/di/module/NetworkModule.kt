package com.kulikowski.todos.di.module

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kulikowski.todos.BuildConfig
import com.kulikowski.todos.network.ToDoApiService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule constructor(private val baseUrl: String) {

    val jsonFieldNamingPolicy = FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES

    @Provides
    @Singleton
    fun provideRetrofit(httpClient: OkHttpClient, gson: Gson?): Retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .client(httpClient)
            .build()

    @Provides
    @Singleton
    fun provideGson(): Gson? = GsonBuilder()
            .setFieldNamingPolicy(jsonFieldNamingPolicy)
            .excludeFieldsWithoutExposeAnnotation()
            .create()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logInterceptor = HttpLoggingInterceptor()
        logInterceptor.level =
                if (BuildConfig.DEBUG)
                    HttpLoggingInterceptor.Level.BODY
                else
                    HttpLoggingInterceptor.Level.NONE

        return OkHttpClient().newBuilder()
                .addInterceptor(logInterceptor)
                .build()
    }

    @Provides
    @Singleton
    fun provideToDoApiService(retrofit: Retrofit) = retrofit.create(ToDoApiService::class.java)
}