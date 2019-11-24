package com.feissenger.data.db.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class RoomMessageItem(
    @PrimaryKey(autoGenerate = true)
    @Embedded
    val id: RoomMessageItemId,
    val time: String
)

data class RoomMessageItemId(
    val uid: String,
    val roomid: String
)