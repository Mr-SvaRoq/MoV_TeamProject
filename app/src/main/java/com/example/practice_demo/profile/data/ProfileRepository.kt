package com.example.practice_demo.profile.data

import com.example.practice_demo.helper.Result
import com.example.practice_demo.login.data.model.UserLoginResponse
import com.example.practice_demo.profile.data.model.ChangePhotoResponse

class ProfileRepository(val dataSource: ProfileDataSource) {
    var user: UserLoginResponse? = null
        private set

    init {
        user = null
    }

    suspend fun changePhoto(filePath: String, token: String): Result<ChangePhotoResponse> = dataSource.changePhoto(filePath, token)

    suspend fun userInfo(token: String): Result<UserLoginResponse> {
        val result = dataSource.userInfo(token)

        if (result is Result.Success) {
            setUserInfo(result.data)
        }

        return result
    }

    private fun setUserInfo(userInfo: UserLoginResponse) {
        this.user = userInfo
    }
}