package com.feissenger.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.feissenger.data.db.model.MessageItem

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
}