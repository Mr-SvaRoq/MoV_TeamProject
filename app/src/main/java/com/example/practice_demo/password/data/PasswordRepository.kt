package com.example.practice_demo.password.data

import com.example.practice_demo.helper.PasswordHasher
import com.example.practice_demo.helper.Result
import com.example.practice_demo.login.data.model.UserLoginResponse

class PasswordRepository(val dataSource: PasswordDataSource) {
    suspend fun changePassword(token: String, oldpassword: String, newpassword: String): Result<UserLoginResponse>{
        return dataSource.changePassword(token, PasswordHasher.hash(oldpassword), PasswordHasher.hash(newpassword))
    }

}