package com.hencoder.coroutinescamp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.*
import java.lang.Exception


/**
 * https://mp.weixin.qq.com/s?__biz=MzAwODY4OTk2Mg==&mid=2652054301&idx=2&sn=917dddffbdb4d97f950f70dfc570c021&chksm=808c8358b7fb0a4ee7ab15a9655543c501b3e1fd7c6c5f84f9e151a58c93264ff74066246696&scene=21#wechat_redirect
 * 当我们需要避免多余的处理来减少内存浪费并节省电量时，取消操作就显得尤为重要；
 * 而妥善的异常处理也是提高用户体验的关键。
 * 本篇是另外两篇文章的基础 (第二篇和第三篇将为大家分别详解协程取消操作和异常处理)，
 * 所以有必要先讲解一些协程的核心概念，比如 CoroutineScope (协程作用域)、Job (任务) 和 CoroutineContext (协程上下文)
 *
 *
 *  重点：
 *      一种是 主动取消
 *      一种是 被动/异常取消
 *
 *  问题：
 *      并发过程出现异常 是否可以继续？
 *
 *
 */
class MainActivity : AppCompatActivity() {




    /**
     *  CoroutineScope (协程作用域)
     *  CoroutineScope 会追踪每一个您通过 launch 或者 async
     *  创建的协程 (这两个是 CoroutineScope 的扩展函数)。
     *  任何时候都可通过调用 scope.cancel() 来取消正在进行的工作 (正在运行的协程)。
     *
     *  Job (任务)
     *  用于处理协程。对于每一个您所创建的协程 (通过 launch 或者 async)，
     *  它会返回一个 Job 实例，
     *  该实例是协程的唯一标识，并且负责管理协程的生命周期
     *
     *  CoroutineContext (协程上下文)
     *  Job: 控制协程的生命周期；
         CoroutineDispatcher: 向合适的线程分发任务；
         CoroutineName: 协程的名称，调试的时候很有用；
         CoroutineExceptionHandler: 处理未被捕捉的异常，
    在未来的第三篇文章里会有详细的讲解。
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        /**
         *  Job 用于处理协程。对于每一个您所创建的协程 (通过 launch 或者 async)，
         *  它会返回一个 Job 实例，
         *  该实例是协程的唯一标识，并且负责管理协程的生命周期。正如我们上面看到的，
         *  您可以将 Job 实例传递给 CoroutineScope 来控制其生命周期。
         */
        val scope = CoroutineScope(Job() + Dispatchers.Main)
        val job = scope.launch {
            //新的协程
            Log.d("MainActivity", "job 1")

            // 新的协程将 CoroutineScope 作为父级
            var result = async {
                // 通过launch 创建的新协程会将当期协程作为父级


            }.await()

        }


        /**
         * job 生命周期
         *  New   新创建
         *  Active  活跃
         *  Completing  完成中
         *  Completed   已完成
         *  Cancelling  取消中
         *  Cancelled   已取消
         *
         *   如果协程处于活跃状态，协程运行出错或者调用 job.cancel()
         *   都会将当前任务置为取消中 (Cancelling)
         *   状态 (isActive = false, isCancelled = true)。
         *   当所有的子协程都完成后，协程会进入已取消 (Cancelled) 状态，
         *   此时 isCompleted = true
         *
         */
        job.isActive
        job.isCancelled
        job.isCompleted


        /**
         * 协程可以 根据重复点击 一些具体场景使用。
         * 如果 知道前一个任务是否完成.
         * 在 Android 开发中使用协程 | 代码实战
         * https://mp.weixin.qq.com/s?__biz=MzAwODY4OTk2Mg==&mid=2652053382&idx=2&sn=3c9ffe976c69675e9c0e08940afd566f&scene=21#wechat_redirect
         */


        /**
         *
         * 协程异常处理
         *  取消它自己的子级；
         *  取消它自己；
         *  将异常传播并传递给它的父级。
         *
         * 异常会到达层级的根部，而且当前 CoroutineScope 所启动的所有协程都会被取消。
         */


        /**
         * 使用 SupervisorJob 时，一个子协程的运行失败不会影响到其他子协程。
         * SupervisorJob 不会取消它和它自己的子级，
         * 也不会传播异常并传递给它的父级，
         * 它会让子协程自己处理异常。
         */
        val uiScope = CoroutineScope(SupervisorJob())


        /**
         *
         * 出现错误时不会退出父级和其他平级的协程
         * 使用 SupervisorJob 或 supervisorScope。
         */

        /**
         * 如果 Child 1 失败了，无论是 scope 还是 Child 2 都会被取消。
         */
        val scope2 = CoroutineScope(SupervisorJob())
        scope2.launch {
            // child 1
        }
        scope2.launch {
            // child 2
        }

        //------------------------------------------------------------


        /**
         *  如果 Child 1 失败了，Child 2 不会被取消
         */
        var scope3 = CoroutineScope(Job())
        scope3.launch {

            supervisorScope {
                launch {
                    // child 1
                }
                launch {
                    // child 2
                }
            }
        }

        //------------------------------------------------------------


        /**
         *  如果 Child 1 失败了，Child 2 也会 失败了
         *  此写法无效
         */
        val scope4 = CoroutineScope(Job())
        scope4.launch(SupervisorJob()) {
            launch {
                // Child 1
            }
            launch {
                // Child 2
            }
        }

        //------------------------------------------------------------
        /**
         *  try 异常
         */
        val scope5 = CoroutineScope(Job())
        scope5.launch {
            try {

            } catch (e: Exception) {

            }
        }


        //------------------------------------------------------------
        /**
         *   当 async 被用作根协程 (CoroutineScope 实例
         *   或 supervisorScope 的直接子协程) 时不会自动抛出异常，
         *   而是在您调用 .await() 时才会抛出异常。
         *
         *  注意： async 永远不会抛出异常
         *        await 将会抛出 async所有的异常
         */

        val scope6 = CoroutineScope(Job())
        // 第一种
        scope6.launch {
            // 让 协程自己处理异常
            supervisorScope {
                val deferred = async {
                    //  throw  exception
                }

                try {
                    deferred.await()
                } catch (e: Exception) {
                    // 处理 async 中抛出的异常
                }
            }
        }

        // 第二种
        scope6.launch {
            try {
                val deferred = async {
                    //  throw  exception
                }
                deferred.await()
            } catch (e: Exception) {
                // async 中抛出的异常将不会在这里被捕获
                // 但是异常会被传播和传递到 scope
            }
        }

        //第三种
        // 其他协程所创建的协程中产生的异常总是会被传播，
        // 无论协程的 Builder 是什么

        /**
         * 由于 scope 的直接子协程是 launch，
         * 如果 async 中产生了一个异常，这个异常将就会被立即抛出。
         * 原因是 async (包含一个 Job 在它的 CoroutineContext 中)
         * 会自动传播异常到它的父级 (launch)，这会让异常被立即抛出。
         */
        val scope7 = CoroutineScope(Job())
        scope7.launch {
            async {
                // 如果 async 抛出异常，launch 就会立即抛出异常，而不会调用 .await()
            }
        }


        //------------------------------------------------------------
        /**
         * 在 coroutineScope builder 或在其他协程创建的协程中抛出的异常不会被 try/catch 捕获
         *
         * 并不是很好理解 ？？？？？
         * 以下的条件被满足时，异常就会被捕获:
        时机 ⏰: 异常是被自动抛出异常的协程所抛出的 (使用 launch，而不是 async 时)；
        位置 🌍: 在 CoroutineScope 的 CoroutineContext 中或在一个根协程 (CoroutineScope 或者 supervisorScope 的直接子协程) 中。
         */
        //------------------------------------------------------------

        var handler = CoroutineExceptionHandler { context, ecxception ->
            {
                println("Caught $ecxception")
            }
        }


        //------------------------------------------------------------

        /**
         * 可以被捕获的例子
         */
        val scope8 = CoroutineScope(Job())
        scope8.launch(handler) {
            launch {
                throw Exception(" failed coroutine")
            }
        }

        /**
         * 不可以被捕获
         * 异常不会被捕获的原因是因为 handler 没有被安装给正确的 CoroutineContext。
         * 内部协程会在异常出现时传播异常并传递给它的父级，
         * 由于父级并不知道 handler 的存在，异常就没有被抛出
         */
        val scope9 = CoroutineScope(Job())
        scope9.launch {
            launch(handler) {
                throw Exception("Failed coroutine")
            }
        }


        //  GlobalScope  是撒
    }


}