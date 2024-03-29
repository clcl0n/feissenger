package com.feissenger.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.feissenger.data.db.model.MessageItem
import com.feissenger.data.db.model.ContactItem
import com.feissenger.data.db.model.RoomItem
import com.feissenger.data.db.model.RoomMessageItem

@Dao
interface DbDao {
    //    Messages
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messageItems: List<MessageItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(messageItem: MessageItem)

    @Query("SELECT * FROM messages WHERE uid LIKE :uid AND ((sender LIKE :uid AND recipient LIKE :contact) OR (sender LIKE :contact AND recipient LIKE :uid))")
    fun getMessages(uid: String, contact: String): LiveData<List<MessageItem>>

    @Query("SELECT * FROM messages WHERE uid LIKE :uid AND sender LIKE :sender AND time LIKE :time LIMIT 1")
    fun getMessage(uid: String, sender: String, time: String):MessageItem


    //    Rooms
    @Query("SELECT * FROM roomsList WHERE uid LIKE :user  AND ssid NOT LIKE 'XsTDHS3C2YneVmEW5Ry7' AND ssid NOT LIKE :activeRoom ORDER BY ssid COLLATE NOCASE ")
    suspend fun getMutableRooms(user: String, activeRoom: String):  List<RoomItem>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRooms(roomList: List<RoomItem>)

    //    Contacts
    @Query("SELECT * FROM contacts WHERE uid LIKE :uid ORDER BY name COLLATE NOCASE ")
    fun getContacts(uid: String): LiveData<List<ContactItem>>

    @Query("SELECT * FROM contacts WHERE uid LIKE :uid and contactId LIKE :contactId LIMIT 1")
    suspend fun getContactById(uid: String, contactId: String): ContactItem

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertContacts(contactList: List<ContactItem>)

    //    RoomMessages
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRoomMessages(wordItems: List<RoomMessageItem>)

    @Query("SELECT * FROM posts WHERE roomid LIKE :roomid")
    fun getRoomMessages(roomid: String): LiveData<List<RoomMessageItem>>




}
