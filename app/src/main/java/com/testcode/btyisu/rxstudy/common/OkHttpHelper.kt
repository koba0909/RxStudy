package com.testcode.btyisu.rxstudy.common

import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class OkHttpHelper {
    companion object{
        val client = OkHttpClient()

        fun get(url: String): String {
            var result: String = "not input in result"

            val request = Request.Builder()
                .url(url)
                .build()

            try{
                val response = client.newCall(request).execute()
                result = response.body().string()
            }catch (e: IOException){
                result = e.message.toString()
                e.printStackTrace()
            }

            return result
        }
    }

}