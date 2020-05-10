package com.testcode.btyisu.rxstudy.common

class NLog {
    companion object{
        fun it(obj: Any, startTime: Long){
            val currentTime = System.currentTimeMillis() - startTime
            println("time : $currentTime | object : $obj")
        }
    }
}