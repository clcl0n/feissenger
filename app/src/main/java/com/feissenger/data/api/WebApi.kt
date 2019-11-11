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

//    Login
    @POST("user/login.php")
    @Headers("Accept: application/json",
        "Cache-Control: no-cache",
        "Content-Type: application/json")
    suspend fun login(@Body login: LoginRequest): Response<LoginResponse>

//    Messages
    @POST("contact/message.php")
    @Headers("Accept: application/json",
        "Cache-Control: no-cache",
        "Content-Type: application/json")
    suspend fun sendContactMessage(@Header("Authorization") authorization:String, @Body contactMessageRequest: ContactMessageRequest) : Response<ResponseBody>

    @POST("contact/read.php")
    @Headers("Accept: application/json",
        "Cache-Control: no-cache",
        "Content-Type: application/json")
    suspend fun getContactMessages(@Header("Authorization") authorization:String, @Body contactReadRequest: ContactReadRequest): Response<List<ContactReadResponse>>

//    RoomMessages
    @POST("room/message.php")
    @Headers("Accept: application/json",
        "Cache-Control: no-cache",
        "Content-Type: application/json")
    suspend fun sendRoomMessage(@Header("Authorization") authorization:String, @Body roomMessage: RoomMessageRequest): Response<ResponseBody>

    @POST("room/read.php")
    @Headers("Accept: application/json",
        "Cache-Control: no-cache",
        "Content-Type: application/json")
    suspend fun getRoomMessages(@Header("Authorization") authorization: String, @Body roomRead: RoomReadRequest): Response<List<RoomReadResponse>>

//    Contacts
    @POST("contact/list.php")
    @Headers("Accept: application/json",
        "Cache-Control: no-cache",
        "Content-Type: application/json")
    suspend fun getContactList(@Header("Authorization") authorization: String, @Body contactList: ContactListRequest): Response<List<ContactListResponse>>

//    Rooms
    @POST("/room/list.php")
    @Headers("Accept: application/json",
        "Cache-Control: no-cache",
        "Content-Type: application/json")
    suspend fun getRooms(@Header("Authorization") authorization:String?, @Body roomList: RoomListRequest): Response<List<RoomListResponse>>


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