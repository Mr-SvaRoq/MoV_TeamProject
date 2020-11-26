package com.example.practice_demo.wall.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.practice_demo.login.data.model.UserLoginResponse
import com.example.practice_demo.wall.data.WallDataSource
import com.example.practice_demo.wall.data.WallRepository

class WallViewModelFactory(
    private val userInstance: UserLoginResponse): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WallViewModel::class.java)) {
            return WallViewModel(
                wallRepository = WallRepository(
                    dataSource = WallDataSource()
                ),
                userInstance = userInstance
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}