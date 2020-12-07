package com.example.practice_demo.password.ui.password

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practice_demo.helper.Result
import com.example.practice_demo.login.data.model.UserLoginResponse
import com.example.practice_demo.password.data.PasswordRepository
import kotlinx.coroutines.launch

class PasswordViewModel (
    private val passwordRepository: PasswordRepository,
    private val userInstance: UserLoginResponse,
): ViewModel() {
    /** Zmena hesla
     *
     * true - bola pridana,
     * false - bola odstranena
     */
    private val _passwordChangeFlag: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val passwordChangeFlag: LiveData<Boolean> = _passwordChangeFlag


    fun changePassword(oldpassword: String, newpassword: String) {
        viewModelScope.launch {
            val result = passwordRepository.changePassword(userInstance.token, oldpassword, newpassword)

            val success = result is Result.Success

            if (success) {
                // Upozornime observer, ze doslo k zmene
                _passwordChangeFlag.value = success
            }
        }
    }
}