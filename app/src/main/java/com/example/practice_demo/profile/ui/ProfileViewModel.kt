package com.example.practice_demo.profile.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practice_demo.helper.Result
import com.example.practice_demo.login.data.model.UserLoginResponse
import com.example.practice_demo.profile.data.ProfileRepository
import kotlinx.coroutines.launch

class ProfileViewModel (
    private val profileRepository: ProfileRepository,
    private val userInstance: UserLoginResponse,
): ViewModel() {
    /** Zmena dat profilovky
     *
     * true - bola pridana,
     * false - bola odstranena
     */
    private val _profilePhotoChangedFlag: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val profilePhotoChangedFlag: LiveData<Boolean> = _profilePhotoChangedFlag


    fun changePhoto(filePath: String) {
        viewModelScope.launch {
            val result = profileRepository.changePhoto(filePath, userInstance.token)

            val success = result is Result.Success && result.data.status == "success"

            if (success) {
                // Upozornime observer, ze doslo k zmene
                _profilePhotoChangedFlag.value = success
            }
        }
    }
}