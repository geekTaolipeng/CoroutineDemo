package com.hencoder.coroutinescamp

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.hencoder.coroutinescamp.api.RetrofitClient
import com.hencoder.coroutinescamp.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * author: taolipeng
 * time:   2020/10/16
 *
 * ProcessLifecycleOwner  基数
 */
class CoroutineActivity : AppCompatActivity() {



    lateinit var viewBind: ActivityMainBinding
    lateinit var viewModel: CoroutineViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBind = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(CoroutineViewModel::class.java)
        viewBind.viewModel = viewModel

        viewModel.boolean.observe(this, Observer<Boolean> {
            if (it) {
                finish()
            }
        })


    }

    fun onclick(v: View) {
        when (v.id) {
            R.id.http1 -> {
//                Log.e(
//                    "CoroutineViewModel",
//                    "CoroutineActivity Current Thread : ${Thread.currentThread()}  sleep"
//                )
                viewModel!!.http1()
            }
            R.id.GlobalScopeHttp -> {
                viewModel!!.GlobalScopeHttp()
            }

            R.id.concurrentHttp2 -> {
                viewModel!!.concurrentHttp2()
            }
            R.id.http2 -> {
                viewModel!!.http2()
            }
            R.id.http1Cancel -> {
                viewModel!!.http1Cancel()
            }

            R.id.jobState -> {
                viewModel!!.jobState()
            }
            R.id.mathCancelAndJoin -> {
                viewModel!!.mathCancelAndJoin()
            }
            R.id.measureTimeMills -> {
                viewModel!!.measureTimeMills()
            }
            R.id.SameScopeThrowError -> {
            //lifecycleScope.launch {
//                    try {
//                        viewModel!!.SameScopeThrowError()
//                    } catch (e: ArithmeticException) {
//                        Log.e("CoroutineViewModel","Computation failed with ArithmeticException")
//                    }
//                }
                viewModel!!.SameScopeThrowError2()
            }
            R.id.CoroutineExceptionHandlerUse -> {
                viewModel!!.CoroutineExceptionHandlerUse()
            }
            R.id.ThrowErrorMore -> {
                viewModel!!.throwErrorMore()
            }
            R.id.supervisorScopeUse -> {
                lifecycleScope.launch {
                    viewModel!!.supervisorScopeUse()
                }
            }
        }

    }


}