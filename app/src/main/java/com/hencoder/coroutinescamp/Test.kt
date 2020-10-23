package com.hencoder.coroutinescamp

import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * author: taolipeng
 * time:   2020/10/14
 */

fun main() = runBlocking {

    val job = launch {
        repeat(200) {
            println("hello : $it")
            delay(500)
        }
    }

    delay(4100)
    println("world")
//    job.cancel()
//    job.join()
    job.cancelAndJoin()

    println("welcome")
}