package com.testcode.btyisu.rxstudy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import org.reactivestreams.Publisher
import org.reactivestreams.Subscriber
import java.util.concurrent.Callable
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        emit()

//        disposable()

//        observableCreate()

//        fromArray()

//        fromIterable()

//        fromCallable()

//        fromFuture()

        fromPublisher()
    }

    fun emit(){
        Observable.just("Hello", "RxJava 2!!")
            .subscribe{
                println(it)
            }
    }

    /**
     * dipose()는 Observable에게 더이상 데이터를 발행하지 않도록 구독을 해지하는 함수
     * onComplete 알림을 보냈을 때 자동으로 dispose()를 호출하여 구독관계를 끊는다.
     */
    fun disposable() {
        val observable = Observable.just("red", "green", "blue")

        val disposable = observable.subscribe(
            { v: String -> println("onNext : $v") },
            { err: Throwable -> println("onError : $err") },
            { println("onComplete") }
        )
        println("is disposed : " + disposable.isDisposed)
    }




    /**
     *  create()를 사용하여 onNext, onError, onComplete를 직접 지정하여 사용
     *  숙련자에게만 권고되는 방법
     *
     *  사용시 주의사항
     *  1. 구독 해지가 되었을 때 등록된 콜백을 모두 해지 해야 한다. 안그럼 메모리 누수 발생
     *  2. 구독자가 구독하는 동안에만 onNext, onComplete 이벤트 호출해야 함 (아래 예제에서는 관련 처리 하지 않음)
     *  3. 에러가 발생했을 경우 onError 이벤트로만 에러 전달
     *  4. 배입을 직접 처리해야 한다.
     */
    fun observableCreate() {
        val observable = Observable.create { emitter: ObservableEmitter<Int?> ->
            emitter.onNext(100)
            emitter.onNext(200)
            emitter.onNext(300)
            emitter.onComplete()
        }

        observable.subscribe { v -> println(v.toString()) }
    }

    /**
     * 배열에 들어 있는 데이터를 처리할 경우
     */
    fun fromArray() {
        val arr = arrayOf(100, 200, 300)
        val observable: Observable<Int> = Observable.fromArray(*arr)
        observable.subscribe { x -> println(x) }
    }

    /**
     *  ArrayList, Array BlockingQueue, HashSet, LinkedList, Stack, TreeSet, Vector 등의 데이터를 처리
     */
    fun fromIterable(){
        val list = mutableListOf<Int>()
        list.add(100)
        list.add(200)
        list.add(300)

        val observable = Observable.fromIterable(list)

        observable.subscribe { x -> println(x) }
    }

    /**
     * 자바5에서 추가된 동시성 api인 Callable 처리
     */
    fun fromCallable() {
        val callable = Callable {
                Thread.sleep(1000)
                "callable!!"
            }

        val observable = Observable.fromCallable(callable)

        observable.subscribe { x -> println(x) }
    }

    /**
     * 자바5에서 추가된 동시성 api인 Future 처리
     */
    fun fromFuture() {
        val future = Executors.newSingleThreadExecutor().submit{
            Thread.sleep(1000)
            "future!!"
        }

        val observable = Observable.fromFuture(future)

        observable.subscribe { x -> println(x) }
    }

    /**
     * Publisher는 자바9의 표준임 Flow API의 일부
     */
    fun fromPublisher() {
        val publisher = Publisher { s: Subscriber<in String?> ->
            s.onNext("publisher!!")
            s.onComplete()
        }

        val observable = Observable.fromPublisher(publisher)

        observable.subscribe { x -> println(x) }
    }

}
