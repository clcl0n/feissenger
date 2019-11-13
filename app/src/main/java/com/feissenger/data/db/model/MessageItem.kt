package com.feissenger.data.db.model

import androidx.room.*

@Entity(tableName = "messages")
data class MessageItem(
    @PrimaryKey
    @Embedded
    val id: MessageId,
    val recipient: String,
    val message: String,
    val gif: Boolean
)

data class MessageId(
    val uid: String,
    val sender: String,
    val time: String
)