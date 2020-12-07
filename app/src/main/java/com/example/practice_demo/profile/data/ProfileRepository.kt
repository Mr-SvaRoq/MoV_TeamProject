package com.example.practice_demo.profile.data

import com.example.practice_demo.helper.Result
import com.example.practice_demo.profile.data.model.ChangePhotoResponse

class ProfileRepository(val dataSource: ProfileDataSource) {
    suspend fun changePhoto(filePath: String, token: String): Result<ChangePhotoResponse> = dataSource.changePhoto(filePath, token)
}