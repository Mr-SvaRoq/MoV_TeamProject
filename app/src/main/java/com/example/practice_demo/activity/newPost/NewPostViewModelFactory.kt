package com.example.practice_demo.activity.newPost

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.practice_demo.activity.newPost.data.PostDataSource
import com.example.practice_demo.activity.newPost.data.PostRepository
import com.example.practice_demo.login.data.model.UserLoginResponse
import com.example.practice_demo.profile.data.ProfileDataSource
import com.example.practice_demo.profile.data.ProfileRepository
import com.example.practice_demo.profile.ui.ProfileViewModel

class NewPostViewModelFactory (
    private val userInstance: UserLoginResponse?
): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewPostViewModel::class.java)) {
            return userInstance?.let {
                NewPostViewModel(
                    postRepository = PostRepository(
                        dataSourcePost = PostDataSource()
                    ),
                    userInstance = it
                )
            } as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}