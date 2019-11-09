package com.feissenger.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.feissenger.data.db.model.MessageItem
import com.feissenger.data.db.model.ContactItem
import com.feissenger.data.db.model.MessageItem
import com.feissenger.data.db.model.RoomItem

@Dao
interface DbDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMessages(wordItems: List<MessageItem>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMessage(wordItem: MessageItem)

    @Update
    suspend fun updateMessage(wordItem: MessageItem)

    @Delete
    suspend fun deleteMessage(wordItem: MessageItem)

    @Query("SELECT * FROM messages WHERE (uid LIKE :user AND contact LIKE :contact) OR (uid LIKE :contact AND contact LIKE :user)")
    fun getMessages(user: String, contact: String): LiveData<List<MessageItem>>

    @Query("SELECT * FROM messages")
    fun getMessages(): LiveData<List<MessageItem>>

    @Query("SELECT * FROM rooms")
    fun getRooms(): LiveData<List<RoomItem>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRoom(roomItem: RoomItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRooms(roomItems: List<RoomItem>)

    @Query("SELECT * FROM contacts")
    fun getContacts(): LiveData<List<ContactItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContacts(contactList: List<ContactItem>)
}