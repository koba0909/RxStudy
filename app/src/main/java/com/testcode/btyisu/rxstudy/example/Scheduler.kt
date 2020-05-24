package com.testcode.btyisu.rxstudy.example

import android.annotation.SuppressLint
import com.testcode.btyisu.rxstudy.common.NLog
import com.testcode.btyisu.rxstudy.common.OkHttpHelper
import com.testcode.btyisu.rxstudy.common.Utils
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.toObservable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executors
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
     * Executor 변환 스케줄러
     * java.util.current 패키지에서 제공하는 실행자(executor)을 이용하여
     * 스케줄러를 생성 할 수 있다.
     *
     * Executor 클래스와 스케줄러의 동작 방식이 다르므로 추천되는 방식은 아니다.
     */
    fun executorSchedulerExample(){
        val THREAD_NUM = 10

        val data = arrayOf("1", "3", "5")

        val source = data.toObservable()
        val executor = Executors.newFixedThreadPool(THREAD_NUM)
        source.subscribeOn(Schedulers.from(executor))
            .subscribe { NLog.i(it) }

        source.subscribeOn(Schedulers.from(executor))
            .subscribe { NLog.i(it) }

        Utils.sleep(500)
    }

    /**
     * Callback HeavenF
     * zip 함수를 활용한 두개의 url에 request를 보낸 후 응답 받기
     * concatWith()에 비해 동시에 request를 던지기 때문에 2배 성능 향상을 본다.
     */
    fun callBackHeavenByZip(){
        val BASE_GITHUB = "https://raw.githubusercontent.com/yudong80/reactivejava/master"
        val FIRST_URL = "https://api.github.com/zen"
        val SECOND_URL = BASE_GITHUB + "/samples/callback_hell"

        val source1 = Observable.just(FIRST_URL)
            .subscribeOn(Schedulers.io())
            .map { OkHttpHelper.get(it) }

        val source2 = Observable.just(SECOND_URL)
            .subscribeOn(Schedulers.io())
            .map { OkHttpHelper.get(it) }

        val observable = Observable.zip(source1,
            source2,
            BiFunction{ a: String, b: String ->  (
                    "\n>> $a" +
                    "\n>> $b" )
            })

        observable.subscribe { NLog.i(it) }

        Utils.sleep(5000)
    }

    /**
     * subscribeOn() 함수는 Observable에서 구독자가 subscribe() 함수를 호출 했을 때
     * 데이터 흐름을 발행하는 스레드를 지정하고 observeOn() 함수는 처리된 결과를 구독자에게
     * 전달하는 스레드를 지정합니다. 또한, subscribeOn()은 처음 지정한 스레드를 고정하고
     * 다음부터 불리는 subscribeOn()은 무시한다.
     *
     * subscribeOn() : 처음 지정한 스레드로만 고정
     * observeOn() : 여러번 호출할 수 있으며 스레드가 바뀐다.
     */
    


}