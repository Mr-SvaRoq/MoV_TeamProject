package com.example.practice_demo.password.ui.password

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.practice_demo.R
import com.example.practice_demo.databinding.FragmentPasswordBinding
import com.example.practice_demo.helper.CustomCallbackFactory
import com.example.practice_demo.helper.SaveSharedPreference


import java.io.IOException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PasswordFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PasswordFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var binding: FragmentPasswordBinding
    lateinit var passwordViewModel: PasswordViewModel
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val user = activity?.let { activity ->

            SaveSharedPreference.getUser(activity)
                ?: throw IOException("User not found")
        }!!

        passwordViewModel = ViewModelProvider(this, PasswordViewModelFactory(user))
            .get(PasswordViewModel::class.java)

        passwordViewModel.passwordChangeFlag.observe(viewLifecycleOwner, {
            //toDo: nejaky event co sa zavola po zmene hesla
            //tutok vycistim data sharedpreferences
            //SaveSharedPreference.clearUsername(context)
            activity?.let { SaveSharedPreference.clearUsername(it) }
            Toast.makeText(context, "test password change", Toast.LENGTH_SHORT).show()
            //poslem usera na login
//            CustomCallbackFactory.getButtonNavigateToId(findNavController(), PasswordFragmentDirections.actionPasswordFragmentToLoginFragment())
            findNavController().navigate(PasswordFragmentDirections.actionPasswordFragmentToLoginFragment())
        })

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_password, container, false)
        binding.changePass.setOnClickListener{
            passwordViewModel.changePassword(binding.oldPass.text.toString(), binding.newPass.text.toString())
        }

        // Inflate the layout for this fragment
        return binding.root
//        return inflater.inflate(R.layout.fragment_password, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PasswordFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PasswordFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}