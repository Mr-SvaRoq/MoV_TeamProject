package com.example.practice_demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.practice_demo.helper.SaveSharedPreference
import com.example.practice_demo.login.ui.login.LoginFragmentDirections

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.nav_host_fragment)
//        NavigationUI.setupActionBarWithNavController(this, navController)

        val appBarConfiguration = AppBarConfiguration
            .Builder(setOf(R.id.wallFragment, R.id.loginFragment))
            .build()
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)

        if (SaveSharedPreference.hasUser(this)) {
            navController.navigate(LoginFragmentDirections.actionLoginFragmentToWallFragment())
//            navController.popBackStack()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp()
    }
}