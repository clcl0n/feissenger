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
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMessages(messageItems: List<MessageItem>)

    @Query("SELECT * FROM messages WHERE (uid LIKE :user AND contact LIKE :contact) OR (uid LIKE :contact AND contact LIKE :user)")
    fun getMessages(user: String, contact: String): LiveData<List<MessageItem>>

    //    Rooms
    @Query("SELECT * FROM rooms WHERE uid LIKE :user")
    fun getRooms(user: String): LiveData<List<RoomItem>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRooms(roomList: List<RoomItem>)

    //    Contacts
    @Query("SELECT * FROM contacts WHERE uid LIKE :user")
    fun getContacts(user: String): LiveData<List<ContactItem>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertContacts(contactList: List<ContactItem>)

    //    RoomMessages
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRoomMessages(wordItems: List<RoomMessageItem>)

    @Query("SELECT * FROM posts WHERE roomId LIKE :roomId")
    fun getRoomMessages(roomId: String): LiveData<List<RoomMessageItem>>
}
