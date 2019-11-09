package com.feissenger.data.db

import com.feissenger.data.db.DbDao
import com.feissenger.data.db.model.MessageItem
import com.feissenger.data.db.model.ContactItem
import com.feissenger.data.db.model.RoomItem

class LocalCache(private val dao: DbDao) {

    //Messages
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
    
    fun getMessages() = dao.getMessages()

    //Rooms
    fun getRooms() = dao.getRooms()

    suspend fun insertRoom(roomItem: RoomItem) {
        dao.insertRoom(roomItem)
    }

    suspend fun insertRooms(roomItems: List<RoomItem>) {
        dao.insertRooms(roomItems)

    }

    //contacts
    fun getContacts() = dao.getContacts()

    suspend fun insertContacts(contactList: List<ContactItem>) {
        dao.insertContacts(contactList)
    }
}