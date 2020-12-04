package com.example.practice_demo.password.ui.password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.practice_demo.login.data.model.UserLoginResponse
import com.example.practice_demo.password.data.PasswordDataSource
import com.example.practice_demo.password.data.PasswordRepository
import com.example.practice_demo.profile.data.ProfileDataSource
import com.example.practice_demo.profile.data.ProfileRepository


class PasswordViewModelFactory (
    private val userInstance: UserLoginResponse?
): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PasswordViewModel::class.java)) {
            return userInstance?.let {
                PasswordViewModel(
                    passwordRepository = PasswordRepository(
                        dataSource = PasswordDataSource()
                    ),
                    userInstance = it
                )
            } as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}