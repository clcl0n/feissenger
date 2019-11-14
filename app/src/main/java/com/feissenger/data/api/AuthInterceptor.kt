package com.feissenger.data.api

import android.content.Context
import com.feissenger.MainActivity
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(val context: Context): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader("Accept","application/json")
            .addHeader("Cache-Control","no-cache")
            .addHeader("Content-Type","application/json")

        if(chain.request().header("Needs-Auth")?.compareTo("true") == 0){
            request.addHeader("Authorization", context.getSharedPreferences(MainActivity::class.java.simpleName,Context.MODE_PRIVATE).getString("access","")!!)
        }

        return chain.proceed(request.build())
    }
}