package com.example.practice_demo.wall.data.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PostDatabaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePosts(posts: List<PostItem>)

    @Query("SELECT * FROM posts_cache")
    suspend fun getPosts(): List<PostItem>

    @Query("DELETE FROM posts_cache")
    suspend fun clearPosts()
}