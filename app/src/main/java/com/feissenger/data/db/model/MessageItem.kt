package com.feissenger.data.db.model

import androidx.room.*
import com.giphy.sdk.core.models.Media

@Entity(tableName = "messages")
data class MessageItem(
    @PrimaryKey
    @Embedded
    val id: MessageId,
    val recipient: String,
    val message: String,
    val gif: Boolean,
    val uid_fid: String,
    val contact_fid: String,
    val uid_name: String,
    val contact_name: String
)

data class MessageId(
    val uid: String,
    val sender: String,
    val time: String
)