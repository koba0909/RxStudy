package com.testcode.btyisu.rxstudy

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.reactivex.Observable

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        emit()

    }

    fun emit(){
        Observable.just("Hello", "RxJava 2!!")
            .subscribe{
                println(it)
            }
    }
}
