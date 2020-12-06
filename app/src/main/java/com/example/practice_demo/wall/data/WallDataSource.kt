package com.example.practice_demo.wall.data
import com.example.practice_demo.helper.Result
import com.example.practice_demo.network.Api
import com.example.practice_demo.network.ServiceAction
import com.example.practice_demo.wall.data.model.GetPostsRequest
import com.example.practice_demo.wall.data.model.PostItem

class WallDataSource {

    suspend fun getPosts(token: String): Result<List<PostItem>> =
        Result.Success(Api.retrofitService.getPostsService(
            GetPostsRequest(
                ServiceAction.GET_POSTS.action,
                Api.API_KEY,
                token
            )
        ))
}