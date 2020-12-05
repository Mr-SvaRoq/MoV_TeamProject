package com.example.practice_demo.change_password.change_password

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.practice_demo.R
import com.example.practice_demo.helper.SaveSharedPreference
import com.example.practice_demo.login.data.model.UserLoginResponse
import com.example.practice_demo.login.ui.login.LoginFragmentDirections
import java.io.IOException

class PasswordFragment : Fragment() {
    private lateinit var passwordViewModel: PasswordViewModel
    lateinit var user: UserLoginResponse

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        user = activity?.let { activity ->

            SaveSharedPreference.getUser(activity)
                ?: throw IOException("User not found")
        }!!

        return inflater.inflate(R.layout.fragment_change_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val oldPassword = view.findViewById<EditText>(R.id.old_password)
        val newPassword = view.findViewById<EditText>(R.id.new_password)
        val confirmPassword = view.findViewById<EditText>(R.id.confirm_password)
        val changeBtn = view.findViewById<Button>(R.id.change_btn)

        super.onViewCreated(view, savedInstanceState)

        passwordViewModel = ViewModelProvider(this, PasswordViewModelFactory(user))
            .get(PasswordViewModel::class.java)


        passwordViewModel.passwordFormState.observe(viewLifecycleOwner,
            Observer { passwordFormState ->
                if (passwordFormState == null) {
                    return@Observer
                }
                changeBtn.isEnabled = passwordFormState.isDataValid
                passwordFormState.passwordError?.let {
                    confirmPassword.error = getString(it)
                }
            })

        passwordViewModel.passwordResult.observe(viewLifecycleOwner,
            Observer { passwordResult ->
                passwordResult ?: return@Observer
                passwordResult.error?.let {
                    showError(it)
                }
                passwordResult.success?.let {
                    Toast.makeText(context, "Success changing password", Toast.LENGTH_LONG).show()
                }
            })

        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) { /*ignore*/ }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) { /*ignore*/ }

            override fun afterTextChanged(s: Editable) {
                passwordViewModel.passwordDataChanged(
                    newPassword.text.toString(),
                    confirmPassword.text.toString()
                )
            }
        }
        oldPassword.addTextChangedListener(afterTextChangedListener)
        newPassword.addTextChangedListener(afterTextChangedListener)
        confirmPassword.addTextChangedListener(afterTextChangedListener)
        newPassword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                passwordViewModel.changePassword(
                    oldPassword.text.toString(),
                    newPassword.text.toString()
                )
            }
            false
        }

        changeBtn.setOnClickListener {
            passwordViewModel.changePassword(
                oldPassword.text.toString(),
                newPassword.text.toString()
            )
        }
    }

    private fun showError(@StringRes errorString: Int) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
    }
}