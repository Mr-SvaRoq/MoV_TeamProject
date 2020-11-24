package com.example.practice_demo.helper

import android.view.View
import androidx.navigation.NavController
import androidx.navigation.NavDirections

class CustomCallbackFactory {
    companion object {
        fun getButtonNavigateToId(navController: NavController, navigateAction: NavDirections): View.OnClickListener = View.OnClickListener {
            navController.navigate(navigateAction)
        }
    }
}