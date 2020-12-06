package com.example.practice_demo.wall.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.practice_demo.helper.Constants
import org.ocpsoft.prettytime.PrettyTime
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "posts_cache")
data class PostItem(
    @ColumnInfo(name = "postid")
    @PrimaryKey(autoGenerate = false)
    val postid: Int,
    @ColumnInfo(name = "created")
    val created: String,
    @ColumnInfo(name = "videourl")
    val videourl: String,
    @ColumnInfo(name = "username")
    val username: String,
    @ColumnInfo(name = "profile")
    val profile: String,
) {
    /**
     * Formatovanie casu pridania postu
     */
    val formattedCreated: String
        get() {
            val formatter = SimpleDateFormat(Constants.DATE_DEFAULT_FORMAT, Locale.ROOT)
            val now = PrettyTime(Date())
            return now.format(formatter.parse(created))
        }
}