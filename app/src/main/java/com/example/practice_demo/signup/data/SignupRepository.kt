package com.example.practice_demo.signup.data

import com.example.practice_demo.helper.PasswordHasher
import com.example.practice_demo.login.data.Result
import com.example.practice_demo.login.data.model.UserLoginResponse

class SignupRepository(val dataSource: SignupDataSource) {
    suspend fun signup(email: String, username: String, password: String): Result<UserLoginResponse> {
        return dataSource.signup(email, username, PasswordHasher.hash(password))
    }
}