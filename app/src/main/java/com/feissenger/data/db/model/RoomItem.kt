package com.example.viewmodel.data.db.model

import androidx.room.*
import com.example.viewmodel.data.db.Converters


@Entity(tableName = "rooms")
data class RoomItem(
    @PrimaryKey
    val bssid: String,
    val ssid: String
)