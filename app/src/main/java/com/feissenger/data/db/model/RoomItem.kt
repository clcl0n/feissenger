package com.feissenger.data.db.model

import androidx.room.*

@Entity(tableName = "rooms")
data class RoomItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val ssid: String,
    val time: String
)