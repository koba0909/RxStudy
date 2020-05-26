package com.testcode.btyisu.rxstudy.example

import android.annotation.SuppressLint
import android.os.Environment
import android.util.Log
import com.testcode.btyisu.rxstudy.common.NLog
import com.testcode.btyisu.rxstudy.common.Utils
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.internal.schedulers.IoScheduler
import io.reactivex.rxkotlin.toObservable
import io.reactivex.schedulers.Schedulers
import java.io.File
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

    /**
     * IO 스케줄러는 계산 스케줄러와는 다르게 네트워크상의 요청을 처리하거나
     * 각종 입출력 작업을 실행하기 위한 스케줄러입니다. 계산 스케줄러와 다른 점은
     * 기본으로 생성되는 스레드 개수가 다르는 것이다.
     *
     * 즉, 계산 스케줄러는 CPI 개수만큼 스레드를 생성하지만 IO 스케줄러는 필요할 때 마다
     * 스레드를 계속 생성한다. 입출력 작업은 비동기로 실행되지만 결가를 얻기까지
     * 대기시간이 길다.
     *
     *  ☞계산 스케줄러 : 일반적인 계산 작업
     *  ☞IO 스케줄러 : 네트워크상의 요청, 파일 입출력, DB 쿼리 등
     */
    @SuppressLint("CheckResult")
    fun ioSchedulerExample(){
        // 루트 디렉터리에 파일 목록 생성.
        val root = Environment.getRootDirectory()
        val files = root.listFiles()

        files.toObservable()
            .filter { f -> !f.isDirectory}
            .map { f -> f.absolutePath}
            .subscribeOn(Schedulers.io())
            .subscribe { NLog.i(it) }

        Utils.sleep(500)
    }

    /**
     * 트램펄린 스케줄러는 새로운 스레드를 생성하지 않고 현재 스레드에 무한한 크기의
     * 대기 행렬를 생성하는 스케줄러입니다.
     *
     * 새로운 스레드를 생성하지 않는다는 것과 대기 행렬을 자동으로 만들어 준다는 것이
     * 뉴 스레드 스케줄러, 계산 스케줄러, IO 스케줄러와 다릅니다.
     */
    @SuppressLint("CheckResult")
    fun trampolineScheduler(){
        val orgs = arrayOf("1", "3", "5")
        val source = orgs.toObservable()

        // 구독 #1
        source.subscribeOn(Schedulers.trampoline())
            .map{ data -> "<<$data>>"}
            .subscribe{ NLog.i(it) }

        // 구독 #2
        source.subscribeOn(Schedulers.trampoline())
            .map{ data -> "##$data##" }
            .subscribe{ NLog.i(it) }

        Utils.sleep(500)
    }

    /**
     * 싱글 스레드 스케줄러는 RxJava 내부에서 단일 스레드를 별도로 생성하여
     * 구독 작업을 처리합니다. 단, 생성된 스레드는 여러번 구독 요청이 와도
     * 공통으로 사용한다.
     *
     * 리액티브 프로그래밍이 비동ㅇ기 프로그래밍을 지향하기 때문에 싱글 스레드
     * 스케줄러를 활용할 확률은 낮다.
     */
    fun singleThreadScheduler(){
        val nums = Observable.range(100, 5)
        val chars = Observable.range(0, 5)
            .map { Utils.numToAlphabet(it) }

        nums.subscribeOn(Schedulers.single())
            .subscribe { NLog.i(it.toString()) }

        chars.subscribeOn(Schedulers.single())
            .subscribe { NLog.i(it) }

        Utils.sleep(500)
    }


}