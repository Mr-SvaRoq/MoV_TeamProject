package com.example.practice_demo.profile.data

import android.net.Uri
import com.example.practice_demo.helper.Result
import com.example.practice_demo.profile.data.model.ChangePhotoResponse

class ProfileRepository(val dataSource: ProfileDataSource) {
    suspend fun changePhoto(uri: Uri, token: String): Result<ChangePhotoResponse> = dataSource.changePhoto(uri, token)
}