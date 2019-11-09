package com.feissenger.data.api.model

data class LoginRequest (
    val name: String,
    val password: String,
    val api_key: String = "c95332ee022df8c953ce470261efc695ecf3e784"
)

data class ContactMessageRequest(
    val message: String,
    val uid: String = "1",
    val contact: String = "2",
    val api_key: String = "c95332ee022df8c953ce470261efc695ecf3e784"
)

data class ContactReadRequest(
    val uid: String = "1",
    val contact: String = "2",
    val api_key: String = "c95332ee022df8c953ce470261efc695ecf3e784"
)

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