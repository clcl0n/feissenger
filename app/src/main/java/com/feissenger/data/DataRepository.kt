/*
 * Copyright (C) 2019 Maros Cavojsky, mpage.sk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.feissenger.data


import androidx.lifecycle.LiveData
import android.util.Log
import com.feissenger.data.api.FCMApi
import com.feissenger.data.api.WebApi
import com.feissenger.data.api.model.ContactMessageRequest
import com.feissenger.data.api.model.ContactReadRequest
import com.feissenger.data.api.model.LoginRequest
import com.feissenger.data.api.model.ContactListRequest
import com.feissenger.data.api.model.RoomListRequest
import com.feissenger.data.api.model.*
import com.feissenger.data.db.LocalCache
import com.feissenger.data.db.model.*
import java.net.ConnectException

/**
 * Repository class that works with local and remote data sources.
 */
class DataRepository private constructor(
    private val fcm: FCMApi,
    private val api: WebApi,
    private val cache: LocalCache
) {

    companion object {
        const val TAG = "DataRepository"

        @Volatile
        private var INSTANCE: DataRepository? = null

        fun getInstance(fcm:FCMApi, api: WebApi, cache: LocalCache): DataRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: DataRepository(fcm, api, cache).also { INSTANCE = it }
            }
    }

    suspend fun login(userName: String, password: String): LoginResponse? {
        val loginResponse = api.login(LoginRequest(userName, password))
        if (loginResponse.isSuccessful)
            return loginResponse.body()
        return null
    }

//    Messages
    fun getMessages(user: String, contact: String): LiveData<List<MessageItem>>{

        return cache.getMessages(user, contact)
    }

    suspend fun loadMessages(onError: (error: String) -> Unit, contactReadRequest: ContactReadRequest, save: Boolean = true) {
        try {
            val contactReadResponse = api.getContactMessages(contactReadRequest)

            if(contactReadResponse.isSuccessful){
                if(save){
                    contactReadResponse.body()?.let {
                        return cache.insertMessages(it.map { item -> MessageItem(MessageId(contactReadRequest.uid, item.uid, item.time),item.contact, item.message, item.message.startsWith("https://giphy.com/gifs/")) })
                    }
                }else{
                    contactReadResponse.body()?.last().let {
                        return cache.insertMessage(MessageItem(MessageId(contactReadRequest.uid, it!!.uid, it.time),it.contact, it.message, it.message.startsWith("https://giphy.com/gifs/")))
                    }
                }
            }

            onError("Load images failed. Try again later please.")
        } catch (ex: ConnectException) {
            onError("Off-line. Check internet connection.")
            ex.printStackTrace()
            return
        } catch (ex: Exception) {
            onError("Oops...Change failed. Try again later please.")
            ex.printStackTrace()
            return
        }
    }

    suspend fun sendMessage(onError: (error: String) -> Unit, contactMessageRequest: ContactMessageRequest, contactReadRequest: ContactReadRequest) {
        try {

            val contactMessageResponse = api.sendContactMessage(contactMessageRequest)

            if(contactMessageResponse.isSuccessful)
                loadMessages(onError, contactReadRequest)

            onError("Load images failed. Try again later please.")
        } catch (ex: ConnectException) {
            onError("Off-line. Check internet connection.")
            ex.printStackTrace()
            return
        } catch (ex: Exception) {
            onError("Oops...Change failed. Try again later please.")
            ex.printStackTrace()
            return
        }
    }

//    RoomMessages
    fun getRoomMessages(roomId: String): LiveData<List<RoomMessageItem>> = cache.getRoomMessages(roomId)

    suspend fun register(userName: String, password: String): RegisterResponse? {
        val registerReponse = api.register(RegisterRequest(userName, password))

        if (registerReponse.isSuccessful)
            return RegisterResponse(
                uid = registerReponse.body()?.uid!!,
                access = registerReponse.body()?.access!!,
                refresh = registerReponse.body()?.refresh!!
            )
        return null
    }


    suspend fun loadRoomMessages(onError: (error: String) -> Unit, roomReadRequest: RoomReadRequest) {
        try {
            val roomReadResponse = api.getRoomMessages(roomReadRequest)

            if(roomReadResponse.isSuccessful){
                roomReadResponse.body()?.let {
                    return cache.insertRoomMessages(it.map { item -> RoomMessageItem(
                        RoomMessageItemId(item.uid,item.roomid, item.time), item.message
                    ) })
                }
            }

            onError("Load images failed. Try again later please.")
        } catch (ex: ConnectException) {
            onError("Off-line. Check internet connection.")
            ex.printStackTrace()
            return
        } catch (ex: Exception) {
            onError("Oops...Change failed. Try again later please.")
            ex.printStackTrace()
            return
        }
    }

    suspend fun sendRoomMessage(onError: (error: String) -> Unit, roomMessageRequest: RoomMessageRequest) {
        try {

            val roomMessageResponse = api.sendRoomMessage(roomMessageRequest)

            if(roomMessageResponse.isSuccessful)
                loadRoomMessages(onError, RoomReadRequest(roomMessageRequest.uid, roomMessageRequest.roomid))

            onError("Load images failed. Try again later please.")
        } catch (ex: ConnectException) {
            onError("Off-line. Check internet connection.")
            ex.printStackTrace()
            return
        } catch (ex: Exception) {
            onError("Oops...Change failed. Try again later please.")
            ex.printStackTrace()
            return
        }
    }

//    Rooms

    fun getRooms(user: String): LiveData<List<RoomItem>> = cache.getRooms(user)

    suspend fun getRoomList(onError: (error:String) -> Unit, roomListRequest: RoomListRequest){
        try {
            val roomListResponse = api.getRooms(roomListRequest)
            if(roomListResponse.isSuccessful){
                roomListResponse.body()?.let {
                    return cache.insertRooms(it.map { item -> RoomItem(RoomItemId(item.roomid,roomListRequest.uid),item.time) })
                }
            }
        }catch (ex: ConnectException){
            onError("Off-line. Check internet connection.")
        }catch (ex: Exception) {
            onError("Oops...Change failed. Try again later please.")
            ex.printStackTrace()
            return
        }
    }

//    Contacts
    fun getContacts(user: String): LiveData<List<ContactItem>> = cache.getContacts(user)

    suspend fun getContactList(onError: (error: String) -> Unit, contactListRequest: ContactListRequest){
        try {

            val contactListResponse = api.getContactList(contactListRequest)

            if(contactListResponse.isSuccessful){
                contactListResponse.body()?.let {
                    return cache.insertContacts(it.map { item -> ContactItem(ContactItemId(contactListRequest.uid, item.id),item.name) })
                }
            }
        }catch (ex: ConnectException){
            onError("Off-line. Check internet connection.")
        }catch (ex: Exception) {
            onError("Oops...Change failed. Try again later please.")
            ex.printStackTrace()
            return
        }
    }

    suspend fun notifyMessage(onError: (error: String) -> Unit, notifyMessage: NotificationRequest){
        try {
            val response = fcm.sendNotification(notifyMessage)
            if(response.isSuccessful)
                response.body()?.let{
                    Log.i("tag",it.toString())
//                    return cache.getRoomMessages(it.map{item-> RoomMessage(0,item.message, item.roomid, item.uid, item.time)})
                }
        }catch (ex: ConnectException){
            onError("Off-line. Check internet connection.")
        }catch (ex: Exception) {
            onError("Oops...Change failed. Try again later please.")
            ex.printStackTrace()
            return
        }
    }

//    suspend fun getRoomMessages(onError: (error: String) -> Unit, roomid: String){
//        try {
//            val response = api.getRoomMessages(RoomReadRequest("2",roomid,api_key))
//            if(response.isSuccessful)
//                response.body()?.let{
//                    return cache.getRoomMessages(it.map{item-> RoomMessage(0,item.message, item.roomid, item.uid, item.time)})
//                }
//        }catch (ex: ConnectException){
//            onError("Off-line. Check internet connection.")
//        }catch (ex: Exception) {
//            onError("Oops...Change failed. Try again later please.")
//            ex.printStackTrace()
//            return
//        }
//    }

}
