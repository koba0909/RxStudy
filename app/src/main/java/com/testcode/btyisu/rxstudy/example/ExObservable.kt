package com.testcode.btyisu.rxstudy.example

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

class ExObservable {
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

    /**
     * Single 클래스는 오직 1개의 데이터만 밸행하도록 한정합니다.
     * 데이터 하나가 발행과 동시에 종료됩니다.
     * Single 클래스는 Obsevable과 거의 같은 방법으로 활용할 수 있습니다.
     *
     * just로 2개 이상의 아이템이 발행되면 onNext 이벤트 발생시 에러가 발생
     */
    fun singleJust(){
        val source = Single.just("hello single");
        source.subscribe{ x -> println(x)}
    }

    /**
     * 차가운 Observable은 옵서버가 subscribe()함수를 호출하여 구독하지 않으면 데이터를 발행하지 않습니다.
     * 다른 말로 게으른(lazy)접근법 입니다.
     *
     * 뜨거운 Observable은 구독자의 존재 여부와 관계없이 데이터를 발행합니다.
     * 따라서 처음부터 모두 수신할 것을 보장받을 수 없다.
     *
     * 즉, 차가운 Observable은 구독하면 준비된 데이터를 처음부터 발행하지만 뜨거운 Observable은 구독한 시점부터
     * Observable에서 발행한 값을 받는다.
     *
     * 뜨거운 Observable은 배압을 고려해야 한다.
     * ※ 배압은 Observable에서 데이터를 발행하는 속도와 구독자가 처리하는 속도의 차이가 클 때 발생한다.
     */
    fun hotObservable(){
    }

    /**
     * Subject 클래스는 차가운 Observable을 뜨거운 Observable로 바꿔준다.
     * Subject 클래스의 특성은 Observable의 속성과 구독자의 속성이 모두 있다는 점이다.
     * Observable처럼 데이터를 발행할 수도 있고 구독자처럼 발행된 데이터를 바로 처리할 수도 있습니다.
     */
    fun subject(){
    }

    /**
     * AsyncSubject는 Observable에서 발행한 마지막 데이터를 얻어올 수 있는 Subject 클래스이다.
     * 완료되기 전 마지막 데이터에만 관심이 있으며 이전 데이터는 무시한다.
     */
    fun asyncSubject(){
        val subject = AsyncSubject.create<Int>()
        subject.subscribe{ data -> println("Subscriber #1 => $data")}
        subject.onNext(1)
        subject.onNext(3)
        subject.subscribe{ data -> println("Subscriber #2 => $data")}
        subject.onNext(5)
        subject.onComplete()
    }

    /**
     * AsyncSubject는 구독자로 동작할 수 있다.
     */
    fun asyncSubjectAsSubscriber(){
        val temperature = arrayOf(10.1f, 13.4f, 12.5f)
        val source = Observable.fromArray(*temperature)

        val subject = AsyncSubject.create<Float>()
        subject.subscribe{ data -> println("Subscriber #1 => $data")}

        source.subscribe(subject)
    }

    /**
     * BehaviorSubject는 구독을 하면 가장 최근 값 혹은 기본값을 넘겨주는 클래스이다.
     */
    fun behaviorSubject(){
        val subject = BehaviorSubject.createDefault(6)
        subject.subscribe{data -> println("Subscriber #1 => $data")}
        subject.onNext(1)
        subject.onNext(3)
        subject.subscribe{data -> println("Subscriber #2 => $data")}
        subject.onNext(5)
        subject.onComplete()
    }

    /**
     * PublishSubject는 구독자가 함수를 호출하면 값을 발행하기 시작하는 가장 평범한 Subject클래스이다.
     */
    fun publishSubject(){
        val subject = PublishSubject.create<Int>()
        subject.subscribe{ data -> println("Subscriber #1 => $data")}
        subject.onNext(1)
        subject.onNext(3)
        subject.subscribe{ data -> println("Subscriber #2 => $data")}
        subject.onNext(5)
        subject.onComplete()
    }

    /**
     * ReplaySubject 클래스는 구독자가 새로 생기면 항상 데이터의 처음부터 끝까지 발행하는 것을 보장한다.
     *
     * 따라서, 모든 데이터 내용을 저장해두는 과정 중 메모리 누수가 발생할 가능성을 염두에 두고 사용할 때 주의.
     */
    fun replaySubject(){
        val subject = ReplaySubject.create<Int>()
        subject.subscribe{ data -> println("Subscriber #1 => $data")}
        subject.onNext(1)
        subject.onNext(3)
        subject.subscribe{ data -> println("Subscriber #2 => $data")}
        subject.onNext(5)
        subject.onComplete()
    }

    /**
     * ConnectableObservable 클래스는 Observable을 여러 구독자에게 공유할 수 있으므로 데이터 하나를 여러 구독자에게
     * 동시에 전달할 때 사용합니다. 특이한점은 subscribe()함수를 호출해도 아무런 동작하지 않고 connect() 함수를 호출한 시점부터
     * 구독자에게 데이터를 발행한다.
     */
    fun connectableObservable(){
        val thread = Thread(Runnable {
            val dataArray = arrayOf(1, 3, 5)
            val balls = Observable.interval(100, TimeUnit.MILLISECONDS)
                .map { i -> dataArray.get(i.toInt()) }
                .take(dataArray.size.toLong())

            val source = balls.publish()
            source.subscribe{ data -> println("Subscriber #1 => $data")}
            source.subscribe{ data -> println("Subscriber #2 => $data")}
            source.connect()

            Thread.sleep(250)
            source.subscribe{ data -> println("Subscriber #3 => $data")}
            Thread.sleep(100)
        })

        thread.run()
    }


}