package com.example.practice_demo.login.data.model

data class UserLoginRequest(
    val action: String,
    val apikey: String,
    val username: String,
    val password: String,
)