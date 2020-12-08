package com.example.practice_demo.wall.data.model

data class DeletePostRequest (
    val action: String,
    val apikey: String,
    val token: String,
    val id: Int,
)