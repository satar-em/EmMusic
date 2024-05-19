package com.emami.emmusic.security.model

data class LoginResponse(val status: Int, var message: String, var data: MutableMap<String, Any>? = null)
