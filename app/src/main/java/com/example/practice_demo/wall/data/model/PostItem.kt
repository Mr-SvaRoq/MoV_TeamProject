package com.example.practice_demo.wall.data.model

import org.ocpsoft.prettytime.PrettyTime
import java.text.SimpleDateFormat
import java.util.*

data class PostItem(
    val postId: Int,
    val created: String,
    val videourl: String,
    val username: String,
    val profile: String,
) {
    /**
     * Formatovanie casu pridania postu
     */
    val formattedCreated: String
        get() {
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ROOT)
            val now = PrettyTime(Date())
            return now.format(formatter.parse(created))
        }
}

data class PostItemRecycler(
    val postItem: PostItem,
    var index: Int = -1
)