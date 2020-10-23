package com.hencoder.coroutinescamp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hencoder.coroutinescamp.api.RetrofitClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * author: taolipeng
 * time:   2020/10/21
 */
class TestViewModel : ViewModel() {

    /**
     * 基本的网络请求
     * 测试取消网络情况
     */
    fun http1() {


        viewModelScope.launch() {

            Log.e("CoroutineViewModel", "Current Thread : ${Thread.currentThread()}")
            /**
             * retrofit 自动切换 线程
             */

            delay(1000)


//            var greeting1 = RetrofitClient.apiService.greeting2()

//            Log.e("CoroutineViewModel", greeting1.toString())
            Log.e("CoroutineViewModel", "Current Thread : ${Thread.currentThread()}  sleep")
//            Log.e("CoroutineViewModel", " delay(1000) ")
//            delay(1000)
            /***
             * 如何取消
             */
//            Log.e("CoroutineViewModel", " greeting3")
//            var greeting3 = RetrofitClient.apiService.greeting3()
//            Log.e("CoroutineViewModel", greeting3.toString())
        }


    }
}

suspend fun XXX() {

}
//
//suspend fun <T : Any> Call<T>.await(): T {
//    return suspendCancellableCoroutine { continuation ->
//        continuation.invokeOnCancellation {
//            cancel()
//        }
//        enqueue(object : Callback<T> {
//            override fun onResponse(call: Call<T>, response: Response<T>) {
//                if (response.isSuccessful) {
//                    val body = response.body()
//                    if (body == null) {
//                        val invocation = call.request().tag(Invocation::class.java)!!
//                        val method = invocation.method()
//                        val e = KotlinNullPointerException("Response from " +
//                                method.declaringClass.name +
//                                '.' +
//                                method.name +
//                                " was null but response body type was declared as non-null")
//                        continuation.resumeWithException(e)
//                    } else {
//                        continuation.resume(body)
//                    }
//                } else {
//                    continuation.resumeWithException(HttpException(response))
//                }
//            }
//
//            override fun onFailure(call: Call<T>, t: Throwable) {
//                continuation.resumeWithException(t)
//            }
//        })
//    }
//}