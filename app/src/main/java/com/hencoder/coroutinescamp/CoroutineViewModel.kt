package com.hencoder.coroutinescamp

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hencoder.coroutinescamp.api.RetrofitClient
import kotlinx.coroutines.*
import java.io.IOException
import java.lang.Exception
import java.util.*
import kotlin.coroutines.coroutineContext
import kotlin.system.measureTimeMillis


/**
 * author: taolipeng
 * time:   2020/10/16
 * 重点看下
 * https://www.kotlincn.net/docs/reference/coroutines/cancellation-and-timeouts.html
 */

class CoroutineViewModel : ViewModel() {

    var boolean = MutableLiveData<Boolean>(false);

    override fun onCleared() {
        super.onCleared()
        Log.e("CoroutineViewModel", "onCleared ")
    }

    var job: Job? = null;

    /**
     * 基本的网络请求
     * 测试取消网络情况
     */
    fun http1() {


        job = viewModelScope.launch(handler) {

            Log.e("CoroutineViewModel", "Current Thread : ${Thread.currentThread()}")
            /**
             * retrofit 自动切换 线程
             */
            var greeting1 = RetrofitClient.apiService.greeting2()

            Log.e("CoroutineViewModel", "result --------> " + greeting1.toString())
            Log.e("CoroutineViewModel", "Current Thread : ${Thread.currentThread()}  sleep")
            Log.e("CoroutineViewModel", " delay(1000) ")
            delay(1000)
            /***
             * 如何取消
             */
            boolean.value = true
            Log.e("CoroutineViewModel", " greeting3")
            var greeting3 = RetrofitClient.apiService.greeting3()
            Log.e("CoroutineViewModel", "result -------->  " + greeting3.toString())
        }


    }

    fun GlobalScopeHttp() {
        /**
         *  默认是子线程
         */
        GlobalScope.launch {
            Log.e("CoroutineViewModel", "Current Thread : ${Thread.currentThread()}")
            /**
             * retrofit 自动切换 线程
             */
            var greeting1 = RetrofitClient.apiService.greeting2()

            Log.e("CoroutineViewModel", greeting1.toString())
            Log.e("CoroutineViewModel", "Current Thread : ${Thread.currentThread()}  sleep")
            Log.e("CoroutineViewModel", " delay(1000) ")
            delay(1000)
            /***
             * 如何取消
             */
            withContext(Dispatchers.Main){
                boolean.value = true
            }


            Log.e("CoroutineViewModel", " greeting3")
            var greeting3 = RetrofitClient.apiService.greeting3()
            Log.e("CoroutineViewModel", greeting3.toString())
        }
    }

    /**
     * 并发
     */
    fun concurrentHttp2() {
        viewModelScope.launch {

            /**
             * 线程格式
             */
            launch(Dispatchers.IO) {
                Log.e(
                    "CoroutineViewModel",
                    "concurrentHttp2 ---> Current Thread : ${Thread.currentThread()}  sleep"
                )
            }
            val job1 = async() {
                Log.e(
                    "CoroutineViewModel",
                    "concurrentHttp2 ---> Current Thread : ${Thread.currentThread()}  sleep"
                )
                RetrofitClient.apiService.greeting1()
            }
            val job2 = async {
                RetrofitClient.apiService.greeting3()
            }

            val time = measureTimeMillis {
                Log.e(
                    "CoroutineViewModel",
                    "concurrentHttp2 ---> Current Thread : ${Thread.currentThread()}  sleep"
                )
                var result1 = job1.await().toString() + "   ----  " + job2.await().toString()
                Log.e(
                    "CoroutineViewModel",
                    "concurrentHttp2 ---> Current Thread : ${Thread.currentThread()}  sleep"
                )

                Log.e("CoroutineViewModel", result1.toString())
            }

            Log.e("CoroutineViewModel", "Completed in $time ms")
        }


    }

    /**
     * 顺序网络情况切换
     *  相比并行(concurrentHttp2) 只是多加了一个async
     *
     *  会根据上下文
     */
    fun http2() {
        viewModelScope.launch {
            val time = measureTimeMillis {
                Log.e(
                    "CoroutineViewModel",
                    "http2 ---> Current Thread : ${Thread.currentThread()}  sleep"
                )
                var result1 = RetrofitClient.apiService.greeting1()
                var result2 = RetrofitClient.apiService.greeting3()
                Log.e(
                    "CoroutineViewModel",
                    "http2 ---> Current Thread : ${Thread.currentThread()}  sleep"
                )
                Log.e("CoroutineViewModel", result1.toString() + result2.toString())
            }

            Log.e("CoroutineViewModel", "Completed in $time ms")
        }
    }

    fun http1Cancel() {
        job?.cancel()
        //job?.cancelAndJoin()

    }

    /**
     * 协程取消，并不一定能取消任务。
     */
    fun mathCancelAndJoin() {
        val startTime = System.currentTimeMillis()
        job = viewModelScope.launch(Dispatchers.Default) {
            var nextPrintTime = startTime
            var i = 0
//            while (isActive) { // 一个执行计算的循环，只是为了占用 CPU
//            while (i < 25 && isActive) {
            while (i < 1000000005) {
//                delay(1000)
//                launch(Dispatchers.Main) {
//                    boolean.value = true
//                }
//                if (System.currentTimeMillis() >= nextPrintTime) {
                Log.e("CoroutineViewModel", "job: I'm sleeping ${i++} ...")
                nextPrintTime += 500L
//                }
            }
        }
        Log.e("CoroutineViewModel", " I'm tired of waiting!")
        //job!!.cancelAndJoin() // 取消一个作业并且等待它结束
//        job!!.cancel()
        Log.e("CoroutineViewModel", " Now I can quit.")
    }

    /**
     * 顺序计算时间
     *   目的: 顺序或者并发 测试
     */
    fun measureTimeMills() {

        viewModelScope.launch {
            val time = measureTimeMillis {
                //  顺序执行
//                val one = doSomethingUsefulOne()
//                val two = doSomethingUsefulTwo()

                // 同步执行
                val one = async {
                    doSomethingUsefulOne()
                }
                val two = async {
                    doSomethingUsefulTwo()
                }

//                Log.e("CoroutineViewModel", "The answer is ${one + two}")
                Log.e("CoroutineViewModel", "The answer is ${one.await() + two.await()}")
            }
            Log.e("CoroutineViewModel", "Completed in $time ms")
        }
    }

    suspend fun doSomethingUsefulOne(): Int {
        delay(1000L)
        return 13;
    }

    suspend fun doSomethingUsefulTwo(): Int {
        delay(1000L) // 假设我们在这里也做了一些有用的事
        return 29
    }

    /**
     * 同一个作用域情况下，出现异常 其他的job都会取消
     */
    suspend fun SameScopeThrowError(): Int = coroutineScope() {
        val one = async<Int> {
            try {
                delay(Long.MAX_VALUE) // 模拟一个长时间的运算
                42
            } finally {
                Log.e("CoroutineViewModel", "First child was cancelled")
            }
        }
        val two = async<Int> {
            Log.e("CoroutineViewModel", "Second child throws an exception")
            throw ArithmeticException()
//            try {
//                throw ArithmeticException()
//            } catch (e: Exception) {
//                Log.e("CoroutineViewModel", "---->ArithmeticException")
//                1
//            }

        }
        one.await() + two.await()
    }


    /**
     * 同一个作用域情况下，
     * 出现异常 其他的job都会取消
     *
     * 当父协程的所有子协程都结束后，原始的异常才会被父协程处理
     *
     */
    fun SameScopeThrowError2() {
        var c = viewModelScope.launch(handler) {
            launch { // 第一个子协程
                try {
                    Log.e("CoroutineViewModel", "launch1")
                    delay(Long.MAX_VALUE)
                    Log.e("CoroutineViewModel", "  delay(Long.MAX_VALUE)  launch1")
                } finally {
                    withContext(NonCancellable) {
                        Log.e("CoroutineViewModel", "finally")
                        delay(5000)
                        Log.e("CoroutineViewModel", "   delay(100)  finally")
                    }
                }
            }
            launch { // 第二个子协程
                Log.e("CoroutineViewModel", "launch2")
                delay(5000)
                Log.e("CoroutineViewModel", " delay(10) launch2")
                throw ArithmeticException()
            }
        }

        /**
         * 如果父 取消了，子也取消了
         */
//        viewModelScope.launch {
//            delay(2000)
//            c.cancel()
//        }

    }


    val handlerMoreError = CoroutineExceptionHandler { _, exception ->
        Log.e(
            "CoroutineViewModel",
            "CoroutineExceptionHandler got $exception     " + exception.suppressed.contentToString()
        )
    }

    /**
     * 多个异常出现 在协程中产生
     * 可以捕获产生的所有异常。
     *
     */
    fun throwErrorMore() {
        viewModelScope.launch(handlerMoreError) {
            launch {
                try {
                    delay(Long.MAX_VALUE) // 当另一个同级的协程因 IOException  失败时，它将被取消
                } finally {
                    throw ArithmeticException() // 第二个异常
                }
            }
            launch {
                delay(100)
                throw IOException() // 首个异常
            }
        }

    }


    val handler = CoroutineExceptionHandler { _, exception ->
        Log.e("CoroutineViewModel", "CoroutineExceptionHandler got $exception")
    }

    /**
     * CoroutineExceptionHandler
     *  使用以及无法使用的场景
     */
    fun CoroutineExceptionHandlerUse() {
        // 场景一
        //GlobalScope.launch(handler) {
        //throw AssertionError()
        //}


        //场景二   捕获异常无效
        viewModelScope.launch() {
            /**
             * 父级处理问题，才能有效 ？
             * 自己无法处理
             */
            val deferred = async(handler) { // 同样是根协程，但使用 async 代替了 launch
                throw ArithmeticException() // 没有打印任何东西，依赖用户去调用 deferred.await()
            }
            deferred.await()
        }
    }


    /**
     *   coroutineScope  vs  supervisorScope
     *   coroutineScope:
     *          SameScopeThrowError
     *          SameScopeThrowError2
     *          throwErrorMore
     *   child 异常了(未try catch),所有都停止了
     *   父异常了，都无法进行。
     *
     * supervisorScope:
     *    child 异常不会影响父工作
     */
    suspend fun supervisorScopeUse() {

        var job = viewModelScope.launch {
            supervisorScope {
                val child1 = launch(handler) {
                    Log.e("CoroutineViewModel", "supervisorScope child1 ")
                    throw AssertionError()
                }
                val child2 = launch(handler) {
                    Log.e("CoroutineViewModel", "supervisorScope child2 ")
                }
                Log.e("CoroutineViewModel", "supervisorScope  parent")
            }
        }
        Log.e("CoroutineViewModel", "supervisorScope completed")
    }


    /**
     * 模拟协程并发情况，统一收集错误异常。
     *
     * 任务:
     *     网络请求ViewModel 情况下 如何cancel
     *     GlobalScope  网络请求，如果取消
     */


    /**
     * 于没有协程作用域，但需要启动协程的时候
     *  全局作用域 GlobalScope：
     *  对于没有协程作用域，但需要启动协程的时候：
     */


    /**
     * 目的：取消后 协程状态
     */
    fun jobState() {
        if (job == null) {
            Log.e("CoroutineViewModel", "job == null ")
            return
        }
        job?.let {
            Log.e(
                "CoroutineViewModel",
                "isActive = " + it.isActive
                        + "  isCancelled = " + it.isCancelled
                        + " isCompleted = " + it.isCompleted
            );
        }

    }
}