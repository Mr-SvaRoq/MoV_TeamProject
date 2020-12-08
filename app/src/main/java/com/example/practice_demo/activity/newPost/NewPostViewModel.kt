package com.example.practice_demo.activity.newPost

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practice_demo.activity.newPost.data.PostRepository
import com.example.practice_demo.login.data.model.UserLoginResponse
import kotlinx.coroutines.launch
import com.example.practice_demo.helper.Result


class NewPostViewModel(private val userInstance: UserLoginResponse, private val postRepository: PostRepository) : ViewModel() {

    private val _createdNewPost: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val createdNewPostFlag: LiveData<Boolean> = _createdNewPost

    fun createNewPost(filePath: String, fileName: String ) {
        viewModelScope.launch {
            //post repo
            val responseFromApi = postRepository.createPost(filePath, fileName, userInstance.token)

            Log.e("TAG", responseFromApi.toString())

            //resolve response
            val success = responseFromApi is Result.Success && responseFromApi.data.status == "success"

            //send resolved response
            if (success) {
                _createdNewPost.value = success
            }
        }
    }
}