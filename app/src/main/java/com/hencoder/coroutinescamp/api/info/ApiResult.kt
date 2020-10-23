package com.hencoder.coroutinescamp.api.info

import com.hencoder.coroutinescamp.api.ApiException

/**
 * author: taolipeng
 * time:   2020/10/16
 */

data class ApiResult<T>(
    val errorCode: Int,
    val errorMsg: String,
    private val data: T?
) {
    fun apiData(): T {
        if (errorCode == 0 && data != null) {
            return data
        } else {
            throw ApiException(errorCode, errorMsg)
        }
    }
}