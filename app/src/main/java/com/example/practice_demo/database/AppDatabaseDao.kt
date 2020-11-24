package com.example.practice_demo.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface AppDatabaseDao {
    @Insert
    suspend fun insert(post: Post)

    /**
     * When updating a row with a value already set in a column,
     * replaces the old value with the new one.
     *
     * @param post new value to write
     */
    @Update
    suspend fun update(post: Post)

    /**
     * Selects and returns the row that matches the supplied start time, which is our key.
     *
     * @param key postId to match
     */
    @Query("SELECT * from post_table WHERE postId = :key")
    suspend fun get(key: Long): Post?

    /**
     * Selects and returns all rows in the table,
     *
     * sorted by postId in descending order.
     */
    @Query("SELECT * FROM post_table ORDER BY postId DESC")
    fun getAllPosts(): LiveData<List<Post>>

    /**
     * Selects and returns the latest night.
     */
    @Query("SELECT * FROM post_table ORDER BY postId DESC LIMIT 1")
    suspend fun getLatestPost(): Post?

    /**
     * Selects and returns the night with given nightId.
     */
    @Query("SELECT * from post_table WHERE postId = :key")
    fun getPostWithId(key: Long): LiveData<Post>
}