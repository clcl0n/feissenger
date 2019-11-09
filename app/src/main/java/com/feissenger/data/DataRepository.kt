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
import com.feissenger.data.db.LocalCache
import com.feissenger.data.db.model.MessageId
import com.feissenger.data.db.model.MessageItem
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
        private var access: String = ""

        @Volatile
        private var INSTANCE: DataRepository? = null

        fun getInstance(api: WebApi, cache: LocalCache): DataRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: DataRepository(api, cache).also { INSTANCE = it }
            }
    }

    suspend fun insertMessages(messageItems: List<MessageItem>) {
        cache.insertMessages(messageItems)
    }

    suspend fun insertMessage(messageItem: MessageItem) {
        cache.insertMessage(messageItem)
    }

    suspend fun updateMessage(messageItem: MessageItem) {
        cache.updateMessage(messageItem)
    }

    suspend fun deleteMessage(messageItem: MessageItem) {
        cache.deleteMessage(messageItem)
    }

    fun getMessages(user: String, contact: String): LiveData<List<MessageItem>> = cache.getMessages(user, contact)

    suspend fun sendMessage(onError: (error: String) -> Unit, contactMessageRequest: ContactMessageRequest) {
        try {

            val contactMessageResponse = api.contactMessage("Bearer e2b9cb1b28897b2f855533d9a8d2df0cdca35ee9", contactMessageRequest)

            if(contactMessageResponse.isSuccessful)
                loadMessages(onError)

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

    suspend fun loadMessages(onError: (error: String) -> Unit) {
        try {
//            val loginResponse = api.login(LoginRequest("andi@test.com","heslo123"))
//
//            var access: String = "";
//
//            if(loginResponse.isSuccessful)
//                access = loginResponse.body()?.access!!

            val contactReadResponse = api.contactRead("Bearer e2b9cb1b28897b2f855533d9a8d2df0cdca35ee9", ContactReadRequest())

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
}
