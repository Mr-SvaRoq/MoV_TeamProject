package com.example.practice_demo.signup.ui.signup

import com.example.practice_demo.login.data.model.UserLoginResponse

/**
 * Authentication result : success (user details) or error message.
 */
data class SignupResult(
    val success: UserLoginResponse? = null,
    val error: Int? = null,
)