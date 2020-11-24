package com.example.practice_demo.login.ui.login

import com.example.practice_demo.login.data.model.UserLoginResponse

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
    val success: UserLoginResponse? = null,
    val error: Int? = null
)