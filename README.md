# CoroutineDemo
kotlin 协程  Demo


思考:


    1.基本 状态
     2.简单取消job
       2.1取消问题
     3.异常作用域
     4.supervisor vs  CoroutineScope
     5.并发/顺序 好处
     6.retrofit  and  取消job  如何取消





参考:

    在 Android 开发中使用协程 | 背景介绍
    https://mp.weixin.qq.com/s?__biz=MzAwODY4OTk2Mg==&mid=2652052998&idx=2&sn=18715a7e33b7f7a5878bd301e9f8f935&chksm=808cbe43b7fb3755e01af29a316c402c8ad70bed5282109516c7f54d70db93013217ceb4e84a&scene=21#wechat_redirect

    * 在 Android 开发中使用协程 | 代码实战
             * https://mp.weixin.qq.com/s?__biz=MzAwODY4OTk2Mg==&mid=2652053382&idx=2&sn=3c9ffe976c69675e9c0e08940afd566f&scene=21#wechat_redirect


    协程中的取消和异常 | 核心概念介绍
    https://mp.weixin.qq.com/s?__biz=MzAwODY4OTk2Mg==&mid=2652054301&idx=2&sn=917dddffbdb4d97f950f70dfc570c021&chksm=808c8358b7fb0a4ee7ab15a9655543c501b3e1fd7c6c5f84f9e151a58c93264ff74066246696&scene=21#wechat_redirect


    协程中的取消和异常 | 异常处理详解
    https://mp.weixin.qq.com/s?__biz=MzAwODY4OTk2Mg==&mid=2652060229&idx=1&sn=38b67881237ee411645c42248b9be2d4&chksm=808c9a00b7fb131624f169dc3c2b958e44980ab7118e97539b3ec42611fe3d1e72a277e1bad5&scene=178#rd

    协程中的取消和异常 | 驻留任务详解
    https://mp.weixin.qq.com/s/0ZPzUeUiBlFhl3uIkgSQ7Q
    计划：
       协程作用
        // ok
       协程启动  ->  job 生命周期 ->  job取消/结合网络请求取消

       /**
          问题一: 线程池 某一个线程出现异常，全部不工作运行吗？

       */
       协程异常各种情况
           并发 一处异常并不会影响全局。
           并发 异常异常影响全局
           如何收集异常

        相比局部异常/取消
        怎么能够创建全局job？
        GlobalScope  是撒
        ProcessLifecycleOwner 是撒


        如何在协程中配置自己的线程池？
            打印自定义线程名称


