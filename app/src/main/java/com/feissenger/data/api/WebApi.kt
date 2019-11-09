package com.feissenger.data.api

import android.content.Context
import com.feissenger.data.api.model.*
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface WebApi {
    @GET("realestate")
    suspend fun getProperties(): Response<List<MarsResponse>>



    @POST("/room/list.php")
    @Headers("Accept: application/json",
            "Cache-Control: no-cache",
            "Content-Type: application/json",
        "Authorization: Bearer 8b542f0dccbda5ad534936be49bc27e81e9eef26")
    suspend fun getRooms(@Body roomList: RoomListRequest): Response<List<RoomListResponse>>

    @POST("room/message.php")
    @Headers("Accept: application/json",
        "Cache-Control: no-cache",
        "Content-Type: application/json",
        "Authorization: Bearer 8b542f0dccbda5ad534936be49bc27e81e9eef26")
    suspend fun sendRoomMessage(@Body roomMessage: RoomMessageRequest)

    @POST("room/read.php")
    @Headers("Accept: application/json",
        "Cache-Control: no-cache",
        "Content-Type: application/json",
        "Authorization: Bearer 8b542f0dccbda5ad534936be49bc27e81e9eef26")
    suspend fun getRoomMessages(@Body roomRead: RoomReadRequest): Response<List<RoomReadResponse>>

    @POST("contact/list.php")
    @Headers("Accept: application/json",
        "Cache-Control: no-cache",
        "Content-Type: application/json",
        "Authorization: Bearer 364d712a5f5fdd4a26df4dc2f45d794e97fb0054")
    suspend fun getContactList(@Body contactList: ContactListRequest): Response<List<ContactListResponse>>


    companion object {
        private const val BASE_URL =
            "http://zadanie.mpage.sk"

        fun create(context: Context): WebApi {

            val client = OkHttpClient.Builder()
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(WebApi::class.java)
        }
    }
}