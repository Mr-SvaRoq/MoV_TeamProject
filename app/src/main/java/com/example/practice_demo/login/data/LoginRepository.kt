package com.example.practice_demo.login.data

import com.example.practice_demo.helper.PasswordHasher
import com.example.practice_demo.helper.Result
import com.example.practice_demo.login.data.model.UserLoginResponse

class LoginRepository(val dataSource: LoginDataSource) {

    // in-memory cache of the loggedInUser object
    var user: UserLoginResponse? = null
        private set

    init {
        user = null
    }

    suspend fun refreshToken(refreshToken: String): Result<UserLoginResponse> {
        val result = dataSource.refreshToken(refreshToken)

        if (result is Result.Success) {
            setLoggedInUser(result.data)
        }

        return result
    }

    suspend fun login(username: String, password: String): Result<UserLoginResponse> {
        // Musime poslat hash hesla
        val result = dataSource.login(
            username,
            PasswordHasher.hash(password)
        )

        if (result is Result.Success) {
            setLoggedInUser(result.data)
        }

        return result
    }

    private fun setLoggedInUser(loggedInUser: UserLoginResponse) {
        this.user = loggedInUser
    }
}