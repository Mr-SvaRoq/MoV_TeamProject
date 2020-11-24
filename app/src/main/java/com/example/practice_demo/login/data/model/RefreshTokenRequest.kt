package com.example.practice_demo.login.data.model

data class RefreshTokenRequest (
    val action: String,
    val apikey: String,
    val refreshToken: String,
)