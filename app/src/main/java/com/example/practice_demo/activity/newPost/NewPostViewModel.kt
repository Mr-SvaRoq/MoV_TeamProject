package com.example.practice_demo.activity.newPost

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practice_demo.activity.newPost.data.PostRepository
import com.example.practice_demo.activity.newPost.data.model.NewPostRequest
import com.example.practice_demo.login.data.model.UserLoginResponse
import kotlinx.coroutines.launch
import com.example.practice_demo.helper.Result


class NewPostViewModel(private val userInstance: UserLoginResponse, private val postRepository: PostRepository) : ViewModel() {

    private val _createdNewPost: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val createdNewPostFlag: LiveData<Boolean> = _createdNewPost

    fun createNewPost(filePath: String, fileName: String ) {
        viewModelScope.launch {
            //TODO newPostRepository.createNewPost
            val responseFromApi = postRepository.createPost(filePath, fileName, userInstance.token)
//            val result = profileRepository.changePhoto(filePath, userInstance.token)

            Log.e("TAG", responseFromApi.toString())

            //TODO get success value
            val success = responseFromApi is Result.Success && responseFromApi.data.status == "success"

            //TODO notification
            if (success) {
//                 Upozornime observer, ze doslo k zmene
                _createdNewPost.value = success
            }
        }
    }
}