package com.feissenger.data.api.model

data class NotificationResponse(
    val messageId:String
)

//Login
data class LoginResponse(
    val uid: String,
    var access: String,
    val refresh: String
)

//Refresh
data class RefreshResponse(
    val uid: String,
    var access: String,
    val refresh: String
)

//Messages
data class ContactReadResponse(
    val uid: String,
    val contact: String,
    val message: String,
    val time: String
)

//RoomMessages
data class RoomReadResponse(
    val uid: String,
    val roomid: String,
    val message: String,
    val time: String
)

//Rooms
data class RoomListResponse(
    val roomid: String,
    val time: String
)

//Contacts
data class ContactListResponse(
    val name:String,
    val id: String
)

data class RegisterResponse (
    val uid: String,
    val access: String,
    val refresh: String
)