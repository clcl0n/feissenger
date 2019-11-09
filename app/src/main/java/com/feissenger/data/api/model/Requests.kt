package com.feissenger.data.api.model

data class RoomListRequest(
    val uid: String,
    val api_key: String
)

data class RoomMessageRequest(
    val uid: String,
    val roomid: String,
    val message: String,
    val api_key: String
)

data class RoomReadRequest(
    val uid: String,
    val roomid: String,
    val api_key: String
)

data class ContactListRequest(
    val uid:String,
    val api_key: String
)