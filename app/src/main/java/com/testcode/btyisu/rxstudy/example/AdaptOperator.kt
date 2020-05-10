package com.testcode.btyisu.rxstudy.example

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
     */
    fun interval(){
        Utils.setTime()

//        Observable.interval(1000L, TimeUnit.MILLISECONDS)                         //초기값이 없어 1초 후 에 실행
        Observable.interval(0L, 1000L, TimeUnit.MILLISECONDS)     //초기값이 0임으로 바로 실행
            .map{ data -> (data + 1) * 100 }
            .take(5)
            .subscribe{ NLog.it(it, Utils.time) }

        // 일반 적인 자바 프로그램에선 스레드 슬립을 해주지 않으면 프로그램이 바로 종료되는거 같다
        // 안드로이드에선 종료되지 않는다.
//        Utils.sleep(1000)
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

}