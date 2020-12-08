package com.example.practice_demo.wall.data
import com.example.practice_demo.helper.Result
import com.example.practice_demo.network.Api
import com.example.practice_demo.network.ServiceAction
import com.example.practice_demo.profile.data.model.ChangePhotoResponse
import com.example.practice_demo.wall.data.model.DeletePostRequest
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

    suspend fun deletePost(postId: Int, token: String): Result<ChangePhotoResponse> =
        Result.Success(Api.retrofitService.deletePostService(
            DeletePostRequest(
                id = postId,
                token = token,
                action = ServiceAction.DELETE_POST.action,
                apikey = Api.API_KEY
            )
        ))
}