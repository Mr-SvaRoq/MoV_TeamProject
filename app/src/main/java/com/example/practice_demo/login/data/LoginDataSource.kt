package com.example.practice_demo.login.data

import com.example.practice_demo.helper.Result
import com.example.practice_demo.login.data.model.RefreshTokenRequest
import com.example.practice_demo.login.data.model.UserLoginRequest
import com.example.practice_demo.login.data.model.UserLoginResponse
import com.example.practice_demo.network.Api
import com.example.practice_demo.network.ServiceAction
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    suspend fun login(username: String, password: String): Result<UserLoginResponse> {
        lateinit var result: Result<UserLoginResponse>
        try {
            result = Result.Success(Api.retrofitService.loginService(
                UserLoginRequest(
                    ServiceAction.LOGIN.action,
                    Api.API_KEY,
                    username,
                    password,
                )
            ))
        } catch (e: Exception) {
            result =  Result.Error(IOException("Error logging in", e))
        }

        return result
    }

    suspend fun logout(refreshToken: String): Result<UserLoginResponse> {
        lateinit var result: Result<UserLoginResponse>

        try {
            result = Result.Success(Api.retrofitService.refreshTokenService(
                RefreshTokenRequest(
                    ServiceAction.REFRESH_TOKEN.action,
                    Api.API_KEY,
                    refreshToken,
                )
            ))
        } catch (e: Exception) {
            result =  Result.Error(IOException("Token refreshing failed", e))
        }

        return result
    }
}