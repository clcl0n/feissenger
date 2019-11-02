package com.example.viewmodel.data.db.model

import androidx.room.*
import com.example.viewmodel.data.db.Converters


@Entity(tableName = "messages")
data class MessageItem(
//    @Embedded val id: MessageId,
    @PrimaryKey
    val text: String
)

data class MessageId(
    val senderId: Int,
    val recipientId: Int
)