package com.example.practice_demo.password.data.model

data class ChangePasswordRequest(
    val action: String,
    val apikey: String,
    val token: String,
    val oldpassword: String,
    val newpassword: String,
)