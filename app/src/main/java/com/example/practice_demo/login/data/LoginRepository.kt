package com.example.practice_demo.login.data

import com.example.practice_demo.login.data.model.UserLoginResponse

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(val dataSource: LoginDataSource) {

    // in-memory cache of the loggedInUser object
    var user: UserLoginResponse? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    //todo: Bude treba volat len pokial chceme odhlasit zo vsetkych zariadeni
    // lebo toto vola request an zmenu tokenu
    suspend fun logout(refreshToken: String): Result<UserLoginResponse> {
        // handle login
        val result = dataSource.logout(refreshToken)

        if (result is Result.Success) {
            setLoggedInUser(result.data)
        }

        return result
    }

    suspend fun login(username: String, password: String): Result<UserLoginResponse> {
        // handle login
        val result = dataSource.login(username, password)

        if (result is Result.Success) {
            setLoggedInUser(result.data)
        }

        return result
    }

    private fun setLoggedInUser(loggedInUser: UserLoginResponse) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}