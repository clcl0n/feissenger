package com.example.viewmodel.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.viewmodel.data.db.model.MessageItem

@Dao
interface DbDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(wordItems: List<MessageItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(wordItem: MessageItem)

    @Update
    suspend fun updateMessage(wordItem: MessageItem)

    @Delete
    suspend fun deleteMessage(wordItem: MessageItem)

    @Query("SELECT * FROM messages")
    fun getMessages(): LiveData<List<MessageItem>>
}