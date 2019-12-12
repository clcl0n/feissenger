package com.feissenger.data.api

import android.content.Context
import com.feissenger.data.api.model.NotificationRequest
import com.feissenger.data.api.model.NotificationResponse
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface FCMApi {
    @POST("fcm/send")
    @Headers(
        "Content-Type: application/json",
        "Authorization: key=AAAAYPLZlqE:APA91bGuR8ciX8KTUcebEgoW4hzX4mhHO8pip7Tz6W2WY9AeJQG26bm6S_elYI7UE9oQDZPEiS_b3wxcgwSOeU0x7f4ySRbu-sVy2RHq7J2C67CW5LWUrKr4G-a6yTtGJCohK2M9HsLf"
    )
    suspend fun sendNotification(@Body notification: NotificationRequest): Response<NotificationResponse>

    companion object {
        private const val BASE_URL =
            "https://fcm.googleapis.com"

        fun create(): FCMApi {

            val client = OkHttpClient.Builder()
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(FCMApi::class.java)
        }
    }
}

