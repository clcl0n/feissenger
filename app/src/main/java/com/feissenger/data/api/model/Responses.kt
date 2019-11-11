package com.feissenger.data.api.model

//Login
data class LoginResponse(
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