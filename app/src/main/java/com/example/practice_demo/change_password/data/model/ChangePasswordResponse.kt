package com.example.practice_demo.change_password.data.model

data class ChangePasswordResponse(
    val id: Int,
    val username: String,
    val email: String,
    val token: String,
    val refresh: String,
    val profile: String,
)

