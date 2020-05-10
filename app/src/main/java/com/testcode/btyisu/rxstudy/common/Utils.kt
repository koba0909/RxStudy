package com.testcode.btyisu.rxstudy.common

import java.lang.Exception

class Utils {
    companion object{
        var time: Long = 0L

        fun sleep(millis: Long){
            try{
                Thread.sleep(millis)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }

        fun setTime(){
            time = System.currentTimeMillis()
        }

    }

}