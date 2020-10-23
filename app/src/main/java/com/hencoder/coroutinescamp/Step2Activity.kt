package com.hencoder.coroutinescamp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_practice1.*

/**
 * author: taolipeng
 * time:   2020/10/21
 */

class Step2Activity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practice1)
        textView.setOnClickListener {
            var intent = Intent(Step2Activity@ this, CoroutineActivity::class.java)
            startActivity(intent)
        }
    }
}