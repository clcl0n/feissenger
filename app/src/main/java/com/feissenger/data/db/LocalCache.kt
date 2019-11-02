package com.example.viewmodel.data.db

import androidx.lifecycle.LiveData
import com.example.viewmodel.data.db.model.MessageItem
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LocalCache(private val dao: DbDao) {

    suspend fun insertMessages(messageItems: List<MessageItem>) {
        dao.insertMessages(messageItems)
    }

    suspend fun insertMessage(messageItem: MessageItem) {
        dao.insertMessage(messageItem)
    }

    suspend fun updateMessage(messageItem: MessageItem) {
        dao.updateMessage(messageItem)
    }

    suspend fun deleteMessage(messageItem: MessageItem) {
        dao.deleteMessage(messageItem)
    }

    fun getMessages() = dao.getMessages()
}