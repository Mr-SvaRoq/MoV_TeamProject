package com.example.practice_demo.wall.data

import com.example.practice_demo.wall.data.model.PostItem
import com.example.practice_demo.helper.Result

class WallRepository(val dataSource: WallDataSource) {

    suspend fun getAllPosts(token: String): Result<List<PostItem>> =
        dataSource.getPosts(token)
}