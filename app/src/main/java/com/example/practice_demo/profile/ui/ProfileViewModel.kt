package com.example.practice_demo.profile.ui

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practice_demo.helper.Result
import com.example.practice_demo.login.data.model.UserLoginResponse
import com.example.practice_demo.profile.data.ProfileRepository
import kotlinx.coroutines.launch

class ProfileViewModel (
    private val profileRepository: ProfileRepository,
    private val userInstance: UserLoginResponse
): ViewModel() {
    fun changePhoto(uri: Uri) {
        viewModelScope.launch {
            val result = profileRepository.changePhoto(uri, userInstance.token)

            if (result is Result.Success<*>) {

            }
        }
    }
}