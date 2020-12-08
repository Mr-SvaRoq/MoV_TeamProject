package com.example.practice_demo.activity.newPost.data

import com.example.practice_demo.activity.newPost.data.model.NewPostResponse
import com.example.practice_demo.helper.Result

class PostRepository(val dataSourcePost: PostDataSource) {
    suspend fun createPost(filePath: String, fileName: String,userToken: String): Result<NewPostResponse> {
        return dataSourcePost.createPost(filePath, fileName, userToken)
    }
}