package com.example.practice_demo.change_password.change_password

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practice_demo.helper.Result

import com.example.practice_demo.R
import com.example.practice_demo.helper.FieldValidator
import com.example.practice_demo.change_password.data.PasswordRepository
import com.example.practice_demo.login.data.model.UserLoginResponse
import kotlinx.coroutines.launch

class PasswordViewModel(
    private val passwordRepository: PasswordRepository,
    private val userInstance: UserLoginResponse
) : ViewModel() {

    private val _passwordForm = MutableLiveData<PasswordFormState>()
    val passwordFormState: LiveData<PasswordFormState> = _passwordForm

    private val _passwordResult = MutableLiveData<ChangePasswordResult>()
    val passwordResult: LiveData<ChangePasswordResult> = _passwordResult

    fun changePassword(oldPassword: String, newPassword: String) {
        // can be launched in a separate asynchronous job
        viewModelScope.launch {
            val result = passwordRepository.changePassword(oldPassword, newPassword, userInstance.token)

            if (result is Result.Success) {
                _passwordResult.value =
                    ChangePasswordResult(success = result.data)
            } else {
                _passwordResult.value = ChangePasswordResult(error = R.string.change_password_error)
            }
        }
    }

    fun passwordDataChanged(newPassword: String, confirmPassword: String) {
        if (!FieldValidator.isPasswordValid(newPassword) || newPassword != confirmPassword) {
            _passwordForm.value = PasswordFormState(passwordError = R.string.not_match_password)
        } else {
            _passwordForm.value = PasswordFormState(isDataValid = true)
        }
    }
}