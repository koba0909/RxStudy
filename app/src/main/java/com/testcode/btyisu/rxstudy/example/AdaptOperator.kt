package com.testcode.btyisu.rxstudy.example

import android.annotation.SuppressLint
import com.testcode.btyisu.rxstudy.common.NLog
import com.testcode.btyisu.rxstudy.common.Utils
import io.reactivex.Observable
import io.reactivex.rxkotlin.toObservable
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * 4장 예제
 */
class AdaptOperator {

    /**
     *  interval() 함수는 일정시간 간격으로 데이터 흐름을 생성합니다.
     *  interval() 함수는 주어진 시간 간격으로 0부터 1씩 증가하는 Long 객체를 발행한다.
     */
    @SuppressLint("CheckResult")
    fun interval(){
        Utils.setTime()

//        Observable.interval(1000L, TimeUnit.MILLISECONDS)                         //초기값이 없어 1초 후 에 실행
        Observable.interval(0L, 1000L, TimeUnit.MILLISECONDS)     //초기값이 0임으로 바로 실행
            .map{ data -> (data + 1) * 100 }
            .take(5)
            .subscribe{ NLog.it(it, Utils.time) }

        // 일반 적인 자바 프로그램에선 스레드 슬립을 해주지 않으면 프로그램이 바로 종료되는거 같다
        // 안드로이드에선 종료되지 않는다.
        Utils.sleep(1000)
    }

    /**
     * timer()함수는 interval()함수와 유사하지만 데이터를 한번만 발행하고
     * onComplete() 이벤트가 실행된다.
     */
     fun timer(){
        Utils.setTime()

        Observable.timer(1000L, TimeUnit.MILLISECONDS)
            .map {
                SimpleDateFormat("yy/MM/dd HH:mm:ss", Locale.KOREA)
                .format(Date())
            }
            .subscribe{NLog.it(it, Utils.time)}
    }

    /**
     * range()함수는 주어진 값부터 n개의 데이터를 발행한다.
     * interval과 timer 함수는 Long을 반환하지만 range함수는 Int를 반환한다.
     */
    fun range(){
        Observable.range(1, 9)
            .filter { it % 2 == 0}
            .subscribe{ println(it) }
    }

    /**
     * intervalRange()함수는 interval과 ragne을 혼합해놓은 함수이다.
     * 즉, interval처럼 무한히 데이터를 발행하지 않는다.
     */
    fun intervalRange(){
        Utils.setTime()

        Observable.intervalRange(1, 5, 1000L, 1000L, TimeUnit.MILLISECONDS)
            .subscribe{ NLog.it(it, Utils.time)}
    }

    /**
     * interRange()함수를 다른 함수들을 조합하여 구현하면, 인자가 5개인 intervalRange()함수를 이용하는 것 보다
     * 가독성 좋은 코드가 될 수 있다.
     */
    fun intervalRangeImpl(){
        Utils.setTime()

        Observable.interval(1000L, TimeUnit.MILLISECONDS)
            .map { it + 1 }
            .take(5)
            .subscribe{NLog.it(it, Utils.time)}
    }

    /**
     * defer()함수는 timer()함수와 비슷하지만 데이터 흐름을 구독자가 subscribe()함수를
     * 호출 할 때까지 미룰 수 있다.
     * Observable의 생성이 구독할 때 까지 미뤄지기 때문에 최신의 데이터를 구할 수 있다.
     */
    fun defer(){
        val colors = arrayOf<Int>(1, 3, 5, 7).iterator()

        val observable = Observable.defer{getDeferSupplyObservable(colors)}

        observable.subscribe { println("Subscriber #1:$it") }
        observable.subscribe { println("Subscriber #2:$it") }
    }

    fun getDeferSupplyObservable(iterator: Iterator<Int>): Observable<String>{
        if(iterator.hasNext()){
            var num = iterator.next()
            return Observable.just(
                "$num-BAll",
                "$num-RECTANGLE",
                "$num-PENTAGON"
            )
        }
        return Observable.empty()
    }

    /**
     * repeat()함수는 반복실행을 하는 함수이다.
     * repeat()함수는 동작이 한 번 끝난 다음에 다시 구독하는 방식으로 동작한다.
     * 그리고 다시 구독할 땜다 동작하는 스레드의 번호가 달라진다.
     *
     * (ex. 서버와의 통신 중 해당 서버가 잘 살아 있는지 확인하는 기능에 사용)
     */
    fun repeat(){
        var balls = arrayOf(1, 3, 5)

        balls.toObservable()
            .repeat(3)
            .doOnComplete { println("onComplete") }
            .subscribe ( System.out::println )
    }

    /**
     * concatMap()함수는 flatMap()함수와 매우 비슷하고, flatMap()는 먼저
     * 들어온 데이터를 처리하는 도중에 새로운 데이터가 들어오면 나중에
     * 들어온 데이터의 처리 결과가 먼저 출력 될 수 있다.
     * 이를 인터리빙(끼어들기)라고 하는데, concapMap()을 사용하면 먼저
     * 들어온 데이터 순서대로 처리하여 결과를 낼 수 있다.
     */
    fun concatMap(){
        var balls = arrayOf(1, 3, 5)
        Observable.interval(1000L, TimeUnit.MILLISECONDS)
            .map { it.toInt() }
            .map { balls.get(it) }
            .take(balls.size.toLong())
            .concatMap {ball -> Observable.interval(2000L, TimeUnit.MILLISECONDS)
                .map { "$ball - PENTAGON" }
                .take(2)}
            .subscribe(System.out::println)
    }

    /**
     * switchMap()함수는 순서를 보장하기 위해 기존에 진행 중이던 작업을
     * 바로 중단한다. 여러 개의 값이 발행되었을 때 마지막에 들어온 값만
     * 처리하고 싶을 때 사용한다.
     *
     * interval값을 조정가면서 테스트해야 이해가 쉽다.
     */
    fun switchMap(){
        var balls = arrayOf(1, 3, 5)
        Observable.interval(1000L, TimeUnit.MILLISECONDS)
            .map { it.toInt() }
            .map { balls.get(it) }
            .take(balls.size.toLong())
            .doOnNext {println(it)}
            .switchMap {ball -> Observable.interval(2000L, TimeUnit.MILLISECONDS)
                .map { "$ball - PENTAGON" }
                .take(2)}
            .subscribe(System.out::println)
    }

    /**
     * groupBy() 함수는 어떤 기준으로 단일 Observable을 여러개로 이루어진
     * Observable 그룹으로 만든다.
     */
    fun groupBy(){
        val balls = arrayOf("6", "4", "2t", "2", "6t", "4")

        balls.toObservable()
            .groupBy { grouping(it) }
            .subscribe { group ->
                group.subscribe {
                    println("key : ${group.key}, value : $it")
                }}
    }

    fun grouping(ball: String): String{
        when(ball.last().toString()){
            "t" -> return "TRIANGLE"
            else -> return "BALL"
        }
    }

    /**
     * scan()함수는 reduce()함수와 비슷한데, 모든 데이터가 입력된 후
     * 그것을 종합하여 마지막 1개의 데이터만을 구독자에게 발행하는
     * reduce()와 달리 scan()함수는 실행할 때만다 입력값에 맞는 중간 결과
     * 및 최종결과를 구독자에게 발행한다.
     */
    fun scan(){
        val foodList = arrayOf("apple", "banana-cream", "cake")

        foodList.toObservable()
            .scan{ food1, food2 -> "$food1 < $food2"}
            .subscribe(::println)
    }
}