package com.feissenger.data.db

import com.feissenger.data.db.DbDao
import com.feissenger.data.db.model.MessageItem

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

    fun getMessages(user: String, contact: String) = dao.getMessages(user, contact)
}