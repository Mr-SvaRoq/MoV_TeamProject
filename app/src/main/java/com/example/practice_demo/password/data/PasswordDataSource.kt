package com.example.practice_demo.password.data

import com.example.practice_demo.network.Api
import com.example.practice_demo.helper.Result
import com.example.practice_demo.login.data.model.UserLoginResponse
import com.example.practice_demo.network.ServiceAction
import com.example.practice_demo.password.data.model.ChangePasswordRequest
import java.io.IOException
import java.lang.Exception

class PasswordDataSource {
    suspend fun changePassword(token: String, oldpassword:String, newpassword:String): Result<UserLoginResponse> =
        try {
            Result.Success(
                Api.retrofitService.changePasswordService(
                ChangePasswordRequest(
                    ServiceAction.CHANGE_PASSWORD.action,
                    Api.API_KEY,
                    token,
                    oldpassword,
                    newpassword,
                )
            ))
        } catch (e: Exception){
            Result.Error(IOException("Error while changing password", e))
        }
}