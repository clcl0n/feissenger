package com.feissenger.data.db.model

import androidx.room.*

@Entity(tableName = "messages")
data class MessageItem(
    @PrimaryKey
    @Embedded
    val id: MessageId,
    val contact: String,
    val message: String
)

data class MessageId(
    val uid: String,
    val time: String
)