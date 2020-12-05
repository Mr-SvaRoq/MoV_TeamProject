package com.example.practice_demo.change_password.change_password

/**
 * Data validation state of the login form.
 */
data class PasswordFormState(
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
)