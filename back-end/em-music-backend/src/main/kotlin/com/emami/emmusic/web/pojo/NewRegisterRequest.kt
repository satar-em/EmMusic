package com.emami.emmusic.web.pojo

data class NewRegisterRequest(
    val firstname: String,
    val lastname: String,
    val username: String,
    val password: String,
    val phoneNumber: String,
    //val email: String,
    var address: String,
    val gender: String
)
