package com.feissenger.data.api.model

data class LoginResponse(
    val uid: String,
    val access: String,
    val refresh: String
)

data class ContactReadResponse(
    val uid: String,
    val contact: String,
    val message: String,
    val time: String
)

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

data class ContactMessageResponse(
    val message: String
)

data class ContactListResponse(
    val name:String,
    val id: String
)