package com.example.practice_demo.profile.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.example.practice_demo.R
import com.example.practice_demo.databinding.FragmentProfileBinding
import com.example.practice_demo.helper.SaveSharedPreference
import com.example.practice_demo.profile.model.UserProfile


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private lateinit var binding: FragmentProfileBinding

class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private fun getSharedPreferences(ctx: Context): SharedPreferences? {
        return PreferenceManager.getDefaultSharedPreferences(ctx)
    }

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

        // Options menu
        setHasOptionsMenu(true)
        val user = activity?.let {
                mainActivity -> SaveSharedPreference.getUser(mainActivity)
        }

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        if (user != null) {
            binding.profile = UserProfile(user.username, user.email)
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu, menu)
    }
}