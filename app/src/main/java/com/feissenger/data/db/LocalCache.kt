package com.feissenger.data.db

import com.feissenger.data.db.DbDao
import com.feissenger.data.db.model.MessageItem
import com.feissenger.data.db.model.ContactItem
import com.feissenger.data.db.model.RoomItem
import com.feissenger.data.db.model.RoomMessageItem

class LocalCache(private val dao: DbDao) {

    //Messages
    suspend fun insertMessages(messageItems: List<MessageItem>) {
        dao.insertMessages(messageItems)
    }

    suspend fun insertMessage(messageItem: MessageItem) {
        dao.insertMessage(messageItem)
    }

    fun getMessages(user: String, contact : String) = dao.getMessages(user, contact)

    //Rooms

    fun getRooms(user: String, activeRoom: String) = dao.getRooms(user,activeRoom)

    suspend fun getMutableRooms(user: String, activeRoom: String) = dao.getMutableRooms(user, activeRoom)

    suspend fun insertRooms(roomItems: List<RoomItem>) {
        dao.insertRooms(roomItems)
    }

    //Contacts
    fun getContacts(user: String) = dao.getContacts(user)
    suspend fun getContactById(user: String, contactId: String) = dao.getContactById(user, contactId)

    suspend fun insertContacts(contactList: List<ContactItem>) {
        dao.insertContacts(contactList)
    }

    //Messages
    suspend fun insertRoomMessages(roomMessageItems: List<RoomMessageItem>) {
        dao.insertRoomMessages(roomMessageItems)
    }

    fun getRoomMessages(roomId: String) = dao.getRoomMessages(roomId)

    suspend fun getContactFid(contact: String) = dao.getContactFid(contact)

}

