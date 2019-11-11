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
    private val api: WebApi,
    private val cache: LocalCache
) {

    companion object {
        const val TAG = "DataRepository"

        @Volatile
        private var INSTANCE: DataRepository? = null

        fun getInstance(api: WebApi, cache: LocalCache): DataRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: DataRepository(api, cache).also { INSTANCE = it }
            }
    }

    suspend fun login(userName: String, password: String): LoginResponse? {
        val loginResponse = api.login(LoginRequest(userName, password))

        if (loginResponse.isSuccessful)
            return LoginResponse(
                uid = loginResponse.body()?.uid!!,
                access = loginResponse.body()?.access!!,
                refresh = loginResponse.body()?.access!!
            )
        return null
    }

//    Messages
    fun getMessages(user: String, contact: String): LiveData<List<MessageItem>> = cache.getMessages(user, contact)

    suspend fun loadMessages(onError: (error: String) -> Unit, contactReadRequest: ContactReadRequest, access: String) {
        try {
            val contactReadResponse = api.getContactMessages(access, contactReadRequest)

            if(contactReadResponse.isSuccessful){
                contactReadResponse.body()?.let {
                    return cache.insertMessages(it.map { item -> MessageItem(MessageId(item.uid, item.time),item.contact, item.message) })
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

    suspend fun sendMessage(onError: (error: String) -> Unit, contactMessageRequest: ContactMessageRequest, contactReadRequest: ContactReadRequest, access: String) {
        try {

            val contactMessageResponse = api.sendContactMessage(access, contactMessageRequest)

            if(contactMessageResponse.isSuccessful)
                loadMessages(onError, contactReadRequest, access)

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

    suspend fun loadRoomMessages(onError: (error: String) -> Unit, roomReadRequest: RoomReadRequest, access: String) {
        try {
            val roomReadResponse = api.getRoomMessages(access, roomReadRequest)

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

    suspend fun sendRoomMessage(onError: (error: String) -> Unit, roomMessageRequest: RoomMessageRequest, roomReadRequest: RoomReadRequest, access: String) {
        try {

            val roomMessageResponse = api.sendRoomMessage(access, roomMessageRequest)

            if(roomMessageResponse.isSuccessful)
                loadRoomMessages(onError, roomReadRequest, access)

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

    suspend fun getRoomList(onError: (error:String) -> Unit, roomListRequest: RoomListRequest, access: String){
        try {

            val roomListResponse = api.getRooms(access, roomListRequest)
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

    suspend fun getContactList(onError: (error: String) -> Unit, contactListRequest: ContactListRequest, access: String){
        try {

            val contactListResponse = api.getContactList(access, contactListRequest)

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

}
