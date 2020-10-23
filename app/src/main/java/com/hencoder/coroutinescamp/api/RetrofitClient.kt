package com.hencoder.coroutinescamp.api

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

/**
 * author: taolipeng
 * time:   2020/10/16
 */
object RetrofitClient {



    private val logInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }


    /**OkhttpClient*/
    private val okHttpClient = OkHttpClient.Builder()
        .callTimeout(10, TimeUnit.SECONDS)
        .addNetworkInterceptor(logInterceptor)
        .build()

    /**Retrofit*/
    private val retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(ApiService.BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(MoshiHelper.moshi))
        .build()

    /**ApiService*/
    val apiService: ApiService = retrofit.create(ApiService::class.java)

}
