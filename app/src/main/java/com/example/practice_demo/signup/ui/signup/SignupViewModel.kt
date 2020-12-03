package com.example.practice_demo.signup.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practice_demo.R
import com.example.practice_demo.helper.FieldValidator
import com.example.practice_demo.helper.Result
import com.example.practice_demo.signup.data.SignupRepository
import kotlinx.coroutines.launch

class SignupViewModel(private val signupRepository: SignupRepository) : ViewModel() {

    private val _signupForm = MutableLiveData<SignupFormState>()
    val signupFormState: LiveData<SignupFormState> = _signupForm

    private val _signupResult = MutableLiveData<SignupResult>()
    val signupResult: LiveData<SignupResult> = _signupResult

    fun signup(email: String, username: String, password: String) {
        viewModelScope.launch {
            val result = signupRepository.signup(email, username, password)

            if (result is Result.Success) {
                _signupResult.value =
                    SignupResult(success = result.data)
            } else {
                _signupResult.value = SignupResult(error = R.string.signup_failed)
            }
        }
    }

    fun signupDataChanged(email: String, username: String, password: String) {
        if (!FieldValidator.isEmailValid(email)) {
            _signupForm.value = SignupFormState(emailError = R.string.invalid_email)
        } else if (!FieldValidator.isUsernameValid(username)) {
            _signupForm.value = SignupFormState(usernameError = R.string.invalid_username)
        } else if (!FieldValidator.isPasswordValid(password)) {
            _signupForm.value = SignupFormState(passwordError = R.string.invalid_password)
        } else {
            _signupForm.value = SignupFormState(isDataValid = true)
        }
    }
}