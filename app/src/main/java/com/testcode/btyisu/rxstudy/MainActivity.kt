package com.testcode.btyisu.rxstudy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.testcode.btyisu.rxstudy.example.AdaptOperator

/**
 * 1,2장 -> ExObservable / 3장 -> RxOperator / 4장 -> AdapterOperator
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rxOperator = AdaptOperator()
        rxOperator.scan()
    }

}
