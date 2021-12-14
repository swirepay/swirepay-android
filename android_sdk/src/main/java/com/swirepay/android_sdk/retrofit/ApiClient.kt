package com.swirepay.android_sdk.retrofit

import com.google.gson.GsonBuilder
import com.swirepay.android_sdk.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

object ApiClient {
    private val gsonBuilder = GsonBuilder().apply {
        registerTypeAdapter(
            Date::class.java,
            DateDeSerializer()
        )
    }

    private val logging: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        this.level = HttpLoggingInterceptor.Level.BODY
    }

    var okHttpClient: OkHttpClient = OkHttpClient()
        .newBuilder()
        .addInterceptor(logging)
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("${BuildConfig.BASE_URL}")
        .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
        .client(okHttpClient)
        .build()
}