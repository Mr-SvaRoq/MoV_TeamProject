package com.example.practice_demo.login.data.model

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 *
 * {"id":"1","username":"test","email":"test@test.com","token":"83f44bb41da47ee85c33a9b1d1a33a71","refresh":"6c79c3449f2b175d59467d6e8384072b","profile":""}
 */
data class UserLoginResponse(
    val id: Int,
    val username: String,
    val email: String,
    val token: String,
    val refresh: String,
    val profile: String,
)

