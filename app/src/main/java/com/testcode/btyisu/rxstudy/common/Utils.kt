package com.testcode.btyisu.rxstudy.common

import java.lang.Exception

class Utils {
    companion object{
        val ALPHABET: String = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
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

        fun flip(string: String): String {
            return "(flipped) $string"
        }

        fun numToAlphabet(num: Int): String{
            return Character.toString(ALPHABET.get((num % ALPHABET.length)))
        }

    }

}