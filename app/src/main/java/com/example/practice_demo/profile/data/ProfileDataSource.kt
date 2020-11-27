package com.example.practice_demo.profile.data

import android.net.Uri
import com.example.practice_demo.helper.Result
import com.example.practice_demo.network.Api
import com.example.practice_demo.profile.data.model.ChangePhotoRequest
import com.example.practice_demo.profile.data.model.ChangePhotoResponse
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.IOException

class ProfileDataSource {
    suspend fun changePhoto(uri: Uri, token: String): Result<ChangePhotoResponse> {
        lateinit var result: Result<ChangePhotoResponse>
        try {
            val path = uri.path
            val picture = File(path.toString())
            val requestFile = RequestBody.create(MediaType.parse("image/*"), picture)
            val photo = MultipartBody.Part.createFormData("boundary", "image", requestFile)
            result = Result.Success(Api.retrofitService.changePhotoService(
                ChangePhotoRequest(
                    Api.API_KEY,
                    token
                ),
                photo
            ))
        } catch (e: Exception) {
            result =  Result.Error(IOException("Error changing photo", e))
        }

        return result
    }
}