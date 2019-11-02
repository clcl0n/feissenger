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

package com.example.viewmodel.data

import androidx.lifecycle.LiveData
import com.example.viewmodel.data.api.WebApi
import com.example.viewmodel.data.db.LocalCache
import com.example.viewmodel.data.db.model.MessageItem
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

    fun getMessages(): LiveData<List<MessageItem>> = cache.getMessages()

//    suspend fun loadMars(onError: (error: String) -> Unit) {
//
//        try {
//            val response = api.getProperties()
//            if (response.isSuccessful) {
//                response.body()?.let {
//                    return cache.insertImages(it.map { item -> MarsItem(item.price, item.id, item.type, item.img_src) })
//                }
//            }
//            onError("Load images failed. Try again later please.")
//        } catch (ex: ConnectException) {
//            onError("Off-line. Check internet connection.")
//            ex.printStackTrace()
//            return
//        } catch (ex: Exception) {
//            onError("Oops...Change failed. Try again later please.")
//            ex.printStackTrace()
//            return
//        }
//    }
}
