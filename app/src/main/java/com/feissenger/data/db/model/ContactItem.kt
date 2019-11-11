package com.feissenger.data.db.model

import androidx.room.*

@Entity(tableName = "contacts")
data class ContactItem(
    @PrimaryKey
    @Embedded
    val id: ContactItemId,
    val name: String
)

data class ContactItemId(
    val uid: String,
    val id: String
)