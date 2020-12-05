package com.example.practice_demo.change_password.change_password

import com.example.practice_demo.change_password.data.model.ChangePasswordResponse

/**
 * Authentication result : success (user details) or error message.
 */
data class ChangePasswordResult(
    val success: ChangePasswordResponse? = null,
    val error: Int? = null
)