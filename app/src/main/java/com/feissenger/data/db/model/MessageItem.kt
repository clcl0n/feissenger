package com.feissenger.data.db.model

import androidx.room.*
import com.feissenger.data.db.Converters
import com.giphy.sdk.core.models.Media
import java.util.*

@Entity(tableName = "messages")
@TypeConverters(Converters::class)
data class MessageItem(
    @PrimaryKey
    @Embedded
    val id: MessageId,
    val recipient: String,
    val message: String,
    val uid_fid: String,
    val contact_fid: String,
    val uid_name: String,
    val contact_name: String,
    val isGif: Boolean
)

data class MessageId(
    val uid: String,
    val sender: String,
    val time: String
)