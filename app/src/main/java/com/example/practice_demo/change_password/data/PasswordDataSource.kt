package com.example.practice_demo.change_password.data

import com.example.practice_demo.change_password.data.model.ChangePasswordRequest
import com.example.practice_demo.helper.Result
import com.example.practice_demo.change_password.data.model.ChangePasswordResponse
import com.example.practice_demo.network.Api
import com.example.practice_demo.network.ServiceAction
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class PasswordDataSource {

    suspend fun changePassword(oldPassword: String, newPassword: String, token: String): Result<ChangePasswordResponse> {
        lateinit var result: Result<ChangePasswordResponse>
        try {
            result = Result.Success(Api.retrofitService.passwordService(
                ChangePasswordRequest(
                    ServiceAction.CHANGE_PASSWORD.action,
                    Api.API_KEY,
                    token,
                    oldpassword = oldPassword,
                    newpassword = newPassword
                )
            ))
        } catch (e: Exception) {
            result =  Result.Error(IOException("Error logging in", e))
        }

        return result
    }
}