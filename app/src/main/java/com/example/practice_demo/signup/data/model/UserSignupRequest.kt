package com.example.practice_demo.signup.data.model

data class UserSignupRequest (
    val action: String,
    val apikey: String,
    val email: String,
    val username: String,
    val password: String,
)