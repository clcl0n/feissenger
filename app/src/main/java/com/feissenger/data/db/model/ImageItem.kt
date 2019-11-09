package com.feissenger.data.db.model

import androidx.room.*


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