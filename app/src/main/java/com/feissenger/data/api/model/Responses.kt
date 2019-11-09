package com.feissenger.data.api.model

data class MarsResponse(
    val price: Long,
    val id: String,
    val type: String,
    val img_src: String
)

data class RoomListResponse(
    val roomid: String,
    val time: String
)

data class RoomReadResponse(
    val uid: String,
    val roomid: String,
    val message: String,
    val time: String
)

data class ContactListResponse(
    val name:String,
    val id: String
)