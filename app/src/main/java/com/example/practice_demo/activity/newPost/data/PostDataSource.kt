package com.example.practice_demo.activity.newPost.data

import com.example.practice_demo.activity.newPost.data.model.NewPostRequest
import com.example.practice_demo.activity.newPost.data.model.NewPostResponse
import com.example.practice_demo.helper.Result
import com.example.practice_demo.network.Api
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.IOException

class PostDataSource {
    suspend fun createPost(filePath: String, fileName: String, token: String): Result<NewPostResponse> {
        lateinit var result: Result<NewPostResponse>
        result = try {
            val post = File(filePath)
            val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), post)
            val video = MultipartBody.Part.createFormData("video", fileName, requestFile)
            Result.Success(
                Api.retrofitService.createPostService(
                NewPostRequest(
                    Api.API_KEY,
                    token
                ),
                video
            ))
        } catch (e: Exception) {
            Result.Error(IOException("Error posting new video", e))
        }

        return result
    }
}