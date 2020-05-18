package com.testcode.btyisu.rxstudy.common

import java.util.*

class NLog {
    companion object{
        fun it(obj: Any, startTime: Long){
            val currentTime = System.currentTimeMillis() - startTime
            println("time : $currentTime | object : $obj")
        }

        fun getThreadName(): String{
            var thread = Thread.currentThread().name

            if(thread.length > 30){
                thread = thread.substring(0, 30) + "..."
            }

            return thread
        }

        fun i(obj: String){
            println("${getThreadName()} | value = $obj")
        }

        fun d(obj: String){
            println("${getThreadName()} | debug = $obj")
        }
    }
}