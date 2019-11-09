package com.feissenger.data.db.model

import androidx.room.*

@Entity(tableName = "contacts")
data class ContactItem(
    @PrimaryKey
    val id: String,
    val name: String
)