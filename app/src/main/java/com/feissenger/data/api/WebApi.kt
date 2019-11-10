package com.feissenger.data.api

import android.content.Context
import com.feissenger.data.api.model.*
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface WebApi {

    @POST("user/login.php")
    @Headers("Accept: application/json",
        "Cache-Control: no-cache",
        "Content-Type: application/json")
    suspend fun login(@Body login: LoginRequest): Response<LoginResponse>

    @POST("contact/message.php")
    @Headers("Accept: application/json",
        "Cache-Control: no-cache",
        "Content-Type: application/json")
    suspend fun contactMessage(@Header("Authorization") authorization:String, @Body contactMessageRequest: ContactMessageRequest) : Response<ResponseBody>

    @POST("contact/read.php")
    @Headers("Accept: application/json",
        "Cache-Control: no-cache",
        "Content-Type: application/json")
    suspend fun contactRead(@Header("Authorization") authorization:String, @Body contactReadRequest: ContactReadRequest): Response<List<ContactReadResponse>>

    @POST("/room/list.php")
    @Headers("Accept: application/json",
            "Cache-Control: no-cache",
            "Content-Type: application/json")
    suspend fun getRooms(@Header("Authorization") authorization:String?, @Body roomList: RoomListRequest): Response<List<RoomListResponse>>


    @POST("room/message.php")
    @Headers("Accept: application/json",
        "Cache-Control: no-cache",
        "Content-Type: application/json",
        "Authorization: Bearer 235357457bc9de273f1cdb3d4530f56b5d9aa4c9")
    suspend fun sendRoomMessage(@Body roomMessage: RoomMessageRequest)

    @POST("room/read.php")
    @Headers("Accept: application/json",
        "Cache-Control: no-cache",
        "Content-Type: application/json",
        "Authorization: Bearer 235357457bc9de273f1cdb3d4530f56b5d9aa4c9")
    suspend fun getRoomMessages(@Body roomRead: RoomReadRequest): Response<List<RoomReadResponse>>

    @POST("contact/list.php")
    @Headers("Accept: application/json",
        "Cache-Control: no-cache",
        "Content-Type: application/json")
    suspend fun getContactList(@Header("Authorization") authorization: String, @Body contactList: ContactListRequest): Response<List<ContactListResponse>>


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