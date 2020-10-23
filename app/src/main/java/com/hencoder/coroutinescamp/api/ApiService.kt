package com.hencoder.coroutinescamp.api

import com.hencoder.coroutinescamp.api.info.ApiResult
import com.hencoder.coroutinescamp.api.info.Article
import com.hencoder.coroutinescamp.api.info.Category
import com.hencoder.coroutinescamp.api.info.Pagination
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * author: taolipeng
 * time:   2020/10/16
 */

interface ApiService {

    companion object {
//        const val BASE_URL = "https://www.wanandroid.com"/**/
        const val BASE_URL = "http://10.10.13.211:8080/"/**/
    }

    @GET("greeting1")
    suspend fun greeting1(): Any

    @GET("greeting2")
    suspend fun greeting2(): Any

    @GET("greeting3")
    suspend fun greeting3(): Any

    @GET("wxarticle/chapters/json")
     fun getWechatCategories(): ApiResult<MutableList<Category>>

    @GET("/article/listproject/{page}/json")
    suspend fun getProjectList(@Path("page") page: Int): ApiResult<Pagination<Article>>

    @GET("/article/top/json")
    suspend fun getTopArticleList(): ApiResult<List<Article>>


}