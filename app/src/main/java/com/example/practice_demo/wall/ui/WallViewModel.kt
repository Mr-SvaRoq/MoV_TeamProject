package com.example.practice_demo.wall.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practice_demo.login.data.model.UserLoginResponse
import com.example.practice_demo.wall.data.WallRepository
import com.example.practice_demo.wall.data.model.PostItem
import com.example.practice_demo.helper.Result
import kotlinx.coroutines.launch

class WallViewModel (
    private val wallRepository: WallRepository,
    private val userInstance: UserLoginResponse
): ViewModel() {
    // Livedata na zoznam postov
    private val _postsList = MutableLiveData<List<PostItem>>()
    val postsList: LiveData<List<PostItem>> = _postsList

    // Livedata na status po ziskani postov (moze sa opakovane volat pri refreshnuti)
    private val _getPostsResult = MutableLiveData<Boolean>()
    val getPostsResult: LiveData<Boolean> = _getPostsResult

    fun feedWall() {
        viewModelScope.launch {
            val result = wallRepository.getAllPosts(userInstance.token)

            if (result is Result.Success) {
                _postsList.value = result.data
                _getPostsResult.value = true
            }

            _getPostsResult.value = result is Result.Success
        }
    }
}