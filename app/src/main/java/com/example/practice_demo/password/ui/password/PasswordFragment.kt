package com.example.practice_demo.password.ui.password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.practice_demo.R
import com.example.practice_demo.databinding.FragmentPasswordBinding
import com.example.practice_demo.helper.SaveSharedPreference
import java.io.IOException

class PasswordFragment : Fragment() {
    private lateinit var binding: FragmentPasswordBinding
    private lateinit var passwordViewModel: PasswordViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val user = activity?.let { activity ->

            SaveSharedPreference.getUser(activity)
                ?: throw IOException("User not found")
        }!!

        passwordViewModel = ViewModelProvider(this, PasswordViewModelFactory(user))
            .get(PasswordViewModel::class.java)

        passwordViewModel.passwordChangeFlag.observe(viewLifecycleOwner, {
            //tutok vycistim data sharedpreferences
            activity?.let { SaveSharedPreference.clearUsername(it) }
            Toast.makeText(context, "test password change", Toast.LENGTH_SHORT).show()
            //poslem usera na login
            findNavController().navigate(PasswordFragmentDirections.actionPasswordFragmentToLoginFragment())
        })

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_password, container, false)
        binding.changePass.setOnClickListener {
            passwordViewModel.changePassword(
                binding.oldPass.text.toString(),
                binding.newPass.text.toString()
            )
        }

        // Inflate the layout for this fragment
        return binding.root
    }
}