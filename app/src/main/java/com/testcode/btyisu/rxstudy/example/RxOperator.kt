package com.testcode.btyisu.rxstudy.example

import io.reactivex.Observable
import io.reactivex.rxkotlin.toObservable

class RxOperator {


    /**
     * Function을 이용하지 않아서 코틀린에선 람다로 observable을 flatmap으로 넘길 수 있다.
     * 구구단을 flatmap을 이용하여 구현
     */
    fun rxGugudanByFlatMap(input: Int){
        val guguObservable = Observable.range(1, 9)
            .map { "$input x $it = ${input * it}" }

        val printObservable = Observable.just(input)
            .flatMap {guguObservable}
            .subscribe { println(it) }
    }

    /**
     * RxKotlin에선 IntRange를 이용해여 간단하게 구구단 구현이 가능
     */
    fun rxGugudanByMap(input: Int){
        val dan = 1..9
        val observable = dan.toObservable()
            .map { "$input x $it = ${input * it}" }
            .subscribe{result -> println(result)}
    }


    /**
     * filter는 boolean값을 리턴하는 predicate를 인자로 넣는다.
     */
    fun filter(){
        val foodList = arrayOf("apple", "banana-cream", "cake", "oreo", "ice-cream")

        foodList.toObservable()
            .filter{food: String -> food.endsWith("cream")}
            .subscribe{ println(it)}
    }

    /**
     * reduce() 함수는 발행한 데이터를 모두 사용하여 어떤 최종 결과 데이터를 합성할 때 활용한다.
     */
    fun reduce(){
        
    }
}