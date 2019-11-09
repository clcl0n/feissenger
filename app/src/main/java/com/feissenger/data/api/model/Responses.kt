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

data class ContactMessageResponse(
    val message: String
)