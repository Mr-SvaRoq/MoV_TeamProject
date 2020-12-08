package com.example.practice_demo.helper

import android.util.Patterns

class FieldValidator {
    companion object {
        fun isEmailValid(email: String): Boolean {
            return if (email.contains("@")) {
                Patterns.EMAIL_ADDRESS.matcher(email).matches()
            } else {
                email.isNotBlank()
            }
        }

        // A placeholder username validation check
        fun isUsernameValid(username: String): Boolean {
            return minLength(username, minLength = 4)
        }

        // A placeholder password validation check
        fun isPasswordValid(password: String): Boolean {
            return minLength(password)
        }

        private fun minLength(fieldValue: String, minLength: Int = 5) =
            fieldValue.length >= minLength
    }
}