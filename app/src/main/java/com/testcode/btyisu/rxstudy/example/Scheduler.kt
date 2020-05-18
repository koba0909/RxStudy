package com.testcode.btyisu.rxstudy.example

import android.annotation.SuppressLint
import android.util.Log
import com.testcode.btyisu.rxstudy.common.NLog
import com.testcode.btyisu.rxstudy.common.Utils
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.toObservable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class Scheduler {

    @SuppressLint("CheckResult")
    fun flipExample(){
        val objs = arrayOf("1S", "2T", "3P")

        val source = objs.toObservable()
            .doOnNext{NLog.i("Original Data : $it")}
            .subscribeOn(Schedulers.newThread())
//            .observeOn(Schedulers.newThread())
            .map {Utils.flip(it)}

        source.subscribe{ NLog.i(it) }

        Utils.sleep(500)
    }

    /**
     * 뉴 스레드 스케줄러는 새로운 스레드를 생성한다. 새로운 스레드를 만들어 어떤 동작을
     * 실행하고 싶을 때 Scheduler.newThread()를 인자로 넣어주면 된다.
     */
    @SuppressLint("CheckResult")
    fun newSchedulerExample(){
        val orgs = arrayOf("1", "3", "5")
        orgs.toObservable()
            .doOnNext{data -> NLog.d("Origianl data : $data")}
            .map { data -> "<<$data>>" }
            .subscribeOn(Schedulers.newThread())
            .subscribe { NLog.i(it) }
//        Utils.sleep(500)

        orgs.toObservable()
            .doOnNext{data -> NLog.d("Origianl data : $data")}
            .map { data -> "##$data##" }
            .subscribeOn(Schedulers.newThread())
            .subscribe { NLog.i(it) }
        Utils.sleep(500)
    }

    /**
     *  계산 스케줄러는 CPU에 대응한는 계산용 스케줄러입니다. '계산'작업을 할 때는 대기
     *  시간 없이 빠르게 결과를 도출한는 것이 중요하다. (입출력 작업을 하지 않는 스케줄러)
     *  내부적으로 스페드 풀을 생성하며 스레드 개수는 기본적으로 프로세서 개수와 동일하다.
     */
    @SuppressLint("CheckResult")
    fun computationSchedulerExample(){
        val orgs = arrayOf("1", "3", "5")

        val source = orgs.toObservable()
            .zipWith(Observable.interval(100L, TimeUnit.MILLISECONDS), BiFunction { a: String, _: Long -> a})

        source.map { data -> "<<$data>>" }
            .subscribeOn(Schedulers.computation())
            .subscribe { NLog.i(it) }

        source.map { data -> "##$data##" }
            .subscribeOn(Schedulers.computation())
            .subscribe { NLog.i(it) }

        Utils.sleep(1000L)
    }


}