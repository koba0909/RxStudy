package com.testcode.btyisu.rxstudy.example

import io.reactivex.Observable
import io.reactivex.rxkotlin.toObservable
import io.reactivex.subjects.AsyncSubject

class RxOperator {
    /**
     * map() 함수는 입력값을 어떤 함수에 넣어서 원하는 값으로 변환하는 함수 이다.
     * String을 String으로 변환할 수도 있고, String을 Int로 변환하거나 다른 객체로 변환할 수 있다.
     */
    fun map(){
        val foodList = arrayListOf("apple", "banana", "ice_cream")

        foodList.toObservable()
            .map { "My favorite food is $it" }
            .subscribe{ println(it) }
    }


    fun mapTransInt(){
        val foodList = arrayListOf("apple", "banana", "ice_cream")

        foodList.toObservable()
            .map{transInt(it)}
            .subscribe(::println)

    }

    fun transInt(input: String): Int{
        when(input){
            "apple" -> return 1
            "banana" -> return 2
            else -> return -1
        }
    }

    /**
     * flatMap() 함수는 map()에서 발전시킨 함수로, map()이 일대일 함수라면
     * flatMap()은 일대다 혹은 일대일 observable 함수이다.
     * 결과값으로 Observable이 나오는 것이다.
     * @rxGugudanByFlatMap 구구단 예시로 파악하자
     */
    fun flatMap(){
    }

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
        val foodList = arrayOf("apple", "banana-cream", "cake")

        foodList.toObservable()
            .reduce{ food1, food2 -> "$food1 < $food2"}
            .subscribe(::println)
    }

    /**
     * 데이터 쿼리하기
     * TV : $1000
     * radio : $300
     * computer : $3000
     * iphone : $800
     *
     * 1. 전체 매출 데이터를 입력함
     * 2. 매출 데이터 중 특정 매출을 필터링함
     * 3. 특정 매출의 합을 구함.
     */
    fun dataQueryTest(product: String){
        val totalSales = arrayOf("radio", "computer", "TV", "TV", "iphone", "computer", "TV")

        totalSales.toObservable()
            .filter{ it.equals(product) }
            .map{ getProductPrice().get(it) }
            .reduce{ price1, price2 -> price1 + price2 }
            .subscribe{ println("$product 총 매출 : $it") }
    }

    fun getProductPrice(): Map<String, Int>{
        return mapOf(
            "TV" to 1000,
            "radio" to 300,
            "computer" to 3000,
            "iphone" to 800
        )
    }
}