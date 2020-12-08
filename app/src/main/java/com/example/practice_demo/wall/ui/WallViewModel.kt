package com.example.practice_demo.wall.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practice_demo.login.data.model.UserLoginResponse
import com.example.practice_demo.helper.Result
import com.example.practice_demo.wall.data.WallRepository
import com.example.practice_demo.wall.data.model.PostItem
import kotlinx.coroutines.launch
import retrofit2.HttpException

class WallViewModel(
    private val wallRepository: WallRepository,
    private val userInstance: UserLoginResponse,
): ViewModel() {
    // Livedata na zoznam postov
    private val _postsList = MutableLiveData<List<PostItem>>()
    val postsList: LiveData<List<PostItem>> = _postsList

    // Livedata na status po ziskani postov (moze sa opakovane volat pri refreshnuti)
    private val _postDeletedFlag = MutableLiveData<Boolean>()
    val postDeletedFlag: LiveData<Boolean> = _postDeletedFlag

    private val _unauthorisedFlag = MutableLiveData<Boolean>()
    val unauthorisedFlag: LiveData<Boolean> = _unauthorisedFlag

    fun deletePost(postId: Int) {
        viewModelScope.launch {
            try {
                val result = wallRepository.deletePost(postId, userInstance.token)
                _postDeletedFlag.value = result is Result.Success
                // Setneme null aby sa hodnota nedostala do observera
                // pri backstackovom vrateni do fragmentu
                _postDeletedFlag.value = null
            } catch (e: HttpException) {
                // Odchytavame http exception az tu a setujeme flag ze
                // bol poslany neautorizovany request
                if (e.code() == 401) {
                    _unauthorisedFlag.value = true
                }
            }
        }
    }

    fun feedWall() {
        viewModelScope.launch {
            try {
                val result = wallRepository.getAllPosts(userInstance.token)
                _postsList.value = result.second
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