package com.example.practice_demo.signup.data
import com.example.practice_demo.login.data.Result
import com.example.practice_demo.login.data.model.UserLoginResponse
import com.example.practice_demo.network.Api
import com.example.practice_demo.network.ServiceAction
import com.example.practice_demo.signup.data.model.UserSignupRequest
import java.lang.Exception

class SignupDataSource {
    suspend fun signup(email: String, username: String, password: String): Result<UserLoginResponse> =
        try {
            Result.Success(Api.retrofitService.signupService(
                UserSignupRequest(
                    ServiceAction.REGISTER.action,
                    Api.API_KEY,
                    email,
                    username,
                    password,
                )
            ))
        } catch (e: Exception) {
            //todo: Ohandlovat podla http statusu
            Result.Error(e)
        }

}