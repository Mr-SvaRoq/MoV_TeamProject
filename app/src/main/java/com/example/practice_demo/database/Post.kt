package com.example.practice_demo.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "post_table")
data class Post(
    @PrimaryKey(autoGenerate = true)
    var postId: Long = 0L,

    @ColumnInfo(name = "user_id")
    val userId: Long = 0L,

    @ColumnInfo(name = "username")
    var userName: String = "",

    @ColumnInfo(name = "post_url")
    var postUrl: String = "",

    @ColumnInfo(name = "date")
    var date: Long = System.currentTimeMillis()
)