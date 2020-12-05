package com.example.practice_demo.change_password.data

import com.example.practice_demo.helper.PasswordHasher
import com.example.practice_demo.helper.Result
import com.example.practice_demo.change_password.data.model.ChangePasswordResponse

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class PasswordRepository(val dataSource: PasswordDataSource) {

    // in-memory cache of the loggedInUser object
    var user: ChangePasswordResponse? = null
        private set


    suspend fun changePassword(oldPassword: String, newPassword: String, token: String): Result<ChangePasswordResponse> {

        val pass1 = PasswordHasher.hash(oldPassword)
        val pass2 = PasswordHasher.hash(newPassword)

        val result = dataSource.changePassword(
            PasswordHasher.hash(oldPassword),
            PasswordHasher.hash(newPassword),
            token
        )

        if (result is Result.Success) {
            setLoggedInUser(result.data)
        }

        return result
    }

    private fun setLoggedInUser(loggedInUser: ChangePasswordResponse) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}