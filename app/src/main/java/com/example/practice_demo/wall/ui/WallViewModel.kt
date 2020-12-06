package com.example.practice_demo.wall.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practice_demo.login.data.model.UserLoginResponse
import com.example.practice_demo.wall.data.WallRepository
import com.example.practice_demo.wall.data.model.PostItem
import kotlinx.coroutines.launch
import retrofit2.HttpException

class WallViewModel (
    private val wallRepository: WallRepository,
    private val userInstance: UserLoginResponse,
): ViewModel() {
    // Livedata na zoznam postov
    private val _postsList = MutableLiveData<List<PostItem>>()
    val postsList: LiveData<List<PostItem>> = _postsList

    // Livedata na status po ziskani postov (moze sa opakovane volat pri refreshnuti)
    // todo: toto bude handlovat observer (fail / success)
    private val _getPostsResult = MutableLiveData<Boolean>()
    val getPostsResult: LiveData<Boolean> = _getPostsResult

    private val _unauthorisedFlag = MutableLiveData<Boolean>()
    val unauthorisedFlag: LiveData<Boolean> = _unauthorisedFlag

    fun feedWall() {
        viewModelScope.launch {
            try {
                val result = wallRepository.getAllPosts(userInstance.token)

                _postsList.value = result.second
                // Dotiahli sa data z API, alebo z lokalu ??
                _getPostsResult.value = result.first
            } catch (e: HttpException) {
                // Odchytavame http exception az tu a setujeme flag ze
                // bol poslany neautorizovany request
                if (e.code() == 401) {
                    _unauthorisedFlag.value = true
                }
            }
        }
    }
}