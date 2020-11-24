package com.example.practice_demo.signup.ui.signup

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.practice_demo.R
import com.example.practice_demo.helper.CustomCallbackFactory
import com.example.practice_demo.helper.SaveSharedPreference
import com.example.practice_demo.login.data.model.UserLoginResponse

class SignupFragment : Fragment() {
    private lateinit var signupViewModel: SignupViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signupViewModel = ViewModelProvider(this, SignupViewModelFactory())
            .get(SignupViewModel::class.java)

        val emailEditText    = view.findViewById<EditText>(R.id.email)
        val usernameEditText = view.findViewById<EditText>(R.id.username)
        val passwordEditText = view.findViewById<EditText>(R.id.password)

        val signupButton = view.findViewById<Button>(R.id.signup)
        val loadingProgressBar = view.findViewById<ProgressBar>(R.id.loading)
        val navigateToLogin = view.findViewById<Button>(R.id.toLoginFragment)

            navigateToLogin.setOnClickListener(
                CustomCallbackFactory.getButtonNavigateToId(
                    findNavController(),
                    SignupFragmentDirections.actionSignupFragmentToLoginFragment()
                )
            )

        signupViewModel.signupFormState.observe(viewLifecycleOwner,
            Observer {  formState ->
                if (formState == null) {
                    return@Observer
                }
                // Enable action on valid fields
                signupButton.isEnabled = formState.isDataValid

                // Show error on invalid fields
                formState.emailError?.let {
                    emailEditText.error = getString(it)
                }

                formState.usernameError?.let {
                    usernameEditText.error = getString(it)
                }

                formState.passwordError?.let {
                    passwordEditText.error = getString(it)
                }
            })

        signupViewModel.signupResult.observe(viewLifecycleOwner,
            Observer {result ->
                result ?: return@Observer

                loadingProgressBar.visibility = View.GONE

                result.error?.let {err ->
                    afterSignupFailure(err)
                }

                result.success?.let {userLoginResponse ->

                    afterSignupSuccess(userLoginResponse)
                    // Po registracii automaticky prihlasime usera
                    findNavController().navigate(SignupFragmentDirections.actionSignupFragmentToWallFragment())
                    activity?.let {
                        mainActivity -> SaveSharedPreference.setUser(mainActivity, userLoginResponse)
                    }
                }
            })

        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) { /*ignore*/ }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) { /*ignore*/ }

            override fun afterTextChanged(s: Editable) {
                signupViewModel.signupDataChanged(
                    emailEditText.text.toString(),
                    usernameEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }
        }

        emailEditText.addTextChangedListener(afterTextChangedListener)
        usernameEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.addTextChangedListener(afterTextChangedListener)

        passwordEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                signupViewModel.signup(
                    emailEditText.text.toString(),
                    usernameEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }
            false
        }

        signupButton.setOnClickListener {
            loadingProgressBar.visibility = View.VISIBLE
            signupViewModel.signup(
                emailEditText.text.toString(),
                usernameEditText.text.toString(),
                passwordEditText.text.toString()
            )
        }
    }

    private fun afterSignupSuccess(model: UserLoginResponse) {
        val welcome = getString(R.string.welcome) + model.username
        // TODO : initiate successful logged in experience
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, welcome, Toast.LENGTH_LONG).show()
    }

    private fun afterSignupFailure(@StringRes errorString: Int) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
    }
}