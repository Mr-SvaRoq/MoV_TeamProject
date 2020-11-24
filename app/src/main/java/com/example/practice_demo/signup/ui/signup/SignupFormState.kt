package com.example.practice_demo.signup.ui.signup

data class SignupFormState (
    val emailError: Int? = null,
    val usernameError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
)