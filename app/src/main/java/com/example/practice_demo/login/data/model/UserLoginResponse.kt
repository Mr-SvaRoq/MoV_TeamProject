package com.example.practice_demo.login.data.model

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class UserLoginResponse(
    val id: Int,
    val username: String,
    val email: String,
    val token: String,
    val refresh: String,
    var profile: String,
)

