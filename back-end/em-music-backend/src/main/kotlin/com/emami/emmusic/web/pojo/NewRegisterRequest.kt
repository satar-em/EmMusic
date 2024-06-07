package com.emami.emmusic.web.pojo

import jakarta.validation.constraints.*

data class NewRegisterRequest(
    val firstname: String,
    val lastname: String,
    val username: String,
    val password: String,
    val phoneNumber: String,
    @field:NotEmpty(message = "E-mail is required")
    @field:Email(message = "E-mail address not correct")
    val email: String,
    var address: String,
    val gender: String
)
