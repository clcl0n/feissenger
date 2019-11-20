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

//    Register
    @POST("user/create.php")
    @Headers("Needs-Auth: false")
    suspend fun register(@Body register: RegisterRequest): Response<RegisterResponse>

//    Login
    @POST("user/login.php")
    @Headers("Needs-Auth: false")
    suspend fun login(@Body login: LoginRequest): Response<LoginResponse>

//  Refresh
    @POST("user/refresh.php")
    @Headers("Needs-Auth: false")
    fun refresh(@Body refresh: RefreshRequest): Call<RefreshResponse>

//    Messages
    @POST("contact/message.php")
    @Headers("Needs-Auth: true")
    suspend fun sendContactMessage(@Body contactMessageRequest: ContactMessageRequest) : Response<ResponseBody>

    @POST("contact/read.php")
    @Headers("Needs-Auth: true")
    suspend fun getContactMessages(@Body contactReadRequest: ContactReadRequest): Response<List<ContactReadResponse>>

//    RoomMessages
    @POST("room/message.php")
    @Headers("Needs-Auth: true")
    suspend fun sendRoomMessage(@Body roomMessage: RoomMessageRequest): Response<ResponseBody>

    @POST("room/read.php")
    @Headers("Needs-Auth: true")
    suspend fun getRoomMessages(@Body roomRead: RoomReadRequest): Response<List<RoomReadResponse>>

//    Contacts
    @POST("contact/list.php")
    @Headers("Needs-Auth: true")
    suspend fun getContactList(@Body contactList: ContactListRequest): Response<List<ContactListResponse>>

//    Rooms
    @POST("/room/list.php")
    @Headers("Needs-Auth: true")
    suspend fun getRooms(@Body roomList: RoomListRequest): Response<List<RoomListResponse>>


    companion object {
        private const val BASE_URL =
            "http://zadanie.mpage.sk"

        fun create(context: Context): WebApi {

            val client = OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor(context))
                .authenticator(TokenAuthenticator(context))
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