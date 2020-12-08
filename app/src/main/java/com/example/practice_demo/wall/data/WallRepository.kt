package com.example.practice_demo.wall.data

import com.example.practice_demo.wall.data.model.PostItem
import com.example.practice_demo.helper.Result
import com.example.practice_demo.profile.data.model.ChangePhotoResponse
import com.example.practice_demo.wall.data.model.PostDatabaseDao
import retrofit2.HttpException

class WallRepository(
    val dataSource: WallDataSource,
    val localDataSource: PostDatabaseDao,
) {
    /**
     * Vytiahni vsetky posty (caching do lokalnej databazy)
     *
     * @throws HttpException
     */
    suspend fun getAllPosts(token: String): Pair<Boolean, List<PostItem>> {
        val result = dataSource.getPosts(token)

        if (result is Result.Success) {
            // Podarilo sa dotiahnut posty z API

            // Vycisti aktualne zacachovane posty
            localDataSource.clearPosts()

            //Uloz do db nove
            localDataSource.savePosts(result.data)

            return Pair(true, result.data)
        }

        // Nepodarilo sa dotiahnut posty, vratime cache z lokalnej databazy
        return Pair(false, localDataSource.getPosts())
    }

    suspend fun deletePost(postId: Int, token: String): Result<ChangePhotoResponse> =
        dataSource.deletePost(postId, token)

}