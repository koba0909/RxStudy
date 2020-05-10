package com.testcode.btyisu.rxstudy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.testcode.btyisu.rxstudy.example.AdaptOperator
import com.testcode.btyisu.rxstudy.example.RxOperator
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.Single
import io.reactivex.subjects.AsyncSubject
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.ReplaySubject
import org.reactivestreams.Publisher
import org.reactivestreams.Subscriber
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

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
