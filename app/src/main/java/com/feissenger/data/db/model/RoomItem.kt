package com.feissenger.data.db.model

import androidx.room.*

@Entity(tableName = "rooms")
data class RoomItem(
    @PrimaryKey
    @Embedded
    val id: RoomItemId,
    val time: String
)

data class RoomItemId(
    val ssid: String,
    val uid: String
)