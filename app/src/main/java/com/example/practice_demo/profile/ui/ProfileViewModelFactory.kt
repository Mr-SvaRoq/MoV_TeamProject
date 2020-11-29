package com.example.practice_demo.profile.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.practice_demo.login.data.model.UserLoginResponse
import com.example.practice_demo.profile.data.ProfileDataSource
import com.example.practice_demo.profile.data.ProfileRepository


class ProfileViewModelFactory (
    private val userInstance: UserLoginResponse?
): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return userInstance?.let {
                ProfileViewModel(
                    profileRepository = ProfileRepository(
                        dataSource = ProfileDataSource()
                    ),
                    userInstance = it
                )
            } as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}