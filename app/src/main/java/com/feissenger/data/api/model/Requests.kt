package com.feissenger.data.api.model

data class NotificationRequest(
    val to: String,
    val data: NotificationBody
)

data class NotificationBody(
    val title:String,
    val message: String,
    val value: String
)

//Login
data class LoginRequest (
    val name: String,
    val password: String,
    val api_key: String = "c95332ee022df8c953ce470261efc695ecf3e784"
)

//Messages
data class ContactMessageRequest(
    val uid: String,
    val contact: String,
    val message: String,
    val api_key: String = "c95332ee022df8c953ce470261efc695ecf3e784"
)

data class ContactReadRequest(
    val uid: String,
    val contact: String,
    val api_key: String = "c95332ee022df8c953ce470261efc695ecf3e784"
)

//RoomMessages
data class RoomMessageRequest(
    val uid: String,
    val roomid: String,
    val message: String,
    val api_key: String = "c95332ee022df8c953ce470261efc695ecf3e784"
)

data class RoomReadRequest(
    val uid: String,
    val roomid: String,
    val api_key: String = "c95332ee022df8c953ce470261efc695ecf3e784"
)

//Rooms
data class RoomListRequest(
    val uid: String,
    val api_key: String = "c95332ee022df8c953ce470261efc695ecf3e784"
)

//Contacts
data class ContactListRequest(
    val uid:String,
    val api_key: String = "c95332ee022df8c953ce470261efc695ecf3e784"
)