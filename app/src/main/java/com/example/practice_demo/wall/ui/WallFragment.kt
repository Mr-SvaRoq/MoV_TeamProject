package com.example.practice_demo.wall.ui

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.practice_demo.R
import com.example.practice_demo.databinding.FragmentWallBinding
import com.example.practice_demo.helper.SaveSharedPreference
import java.io.IOException

class WallFragment : Fragment() {
    lateinit var binding: FragmentWallBinding
    lateinit var wallViewModel: WallViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Options menu
        setHasOptionsMenu(true)

        // Nasetuj viewModel
        activity?.let {activity ->
            val user = SaveSharedPreference.getUser(activity)
                ?: throw IOException("User not found")

            wallViewModel = ViewModelProvider(this, WallViewModelFactory(user))
                .get(WallViewModel::class.java)
        }
        val adapter = PostAdapter()

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_wall, container, false)
        binding.postList.adapter = adapter

        // Observujeme dotiahnutie postov do viewmodelu (refresh alebo zapnutie wallfragmentu)
        wallViewModel.postsList.observe(viewLifecycleOwner, Observer {postsList ->
            adapter.data = postsList
        })

        // Nafeeduj nastenku
        wallViewModel.feedWall()

        // Specify the current activity as the lifecycle owner of the binding.
        // This is necessary so that the binding can observe LiveData updates.
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        handleOptionsItemSelected(item)
        return super.onOptionsItemSelected(item)
    }

    private fun handleOptionsItemSelected(item: MenuItem) {
        when(item.itemId) {
            R.id.menu_logout -> {
                // Vycisti saved preference
                activity?.let { SaveSharedPreference.clearUsername(it) }
                // Presmeruj na login
                findNavController().navigate(WallFragmentDirections.actionWallFragmentToLoginFragment())
            }

            R.id.menu_viewProfile -> {
                findNavController().navigate(WallFragmentDirections.actionWallFragmentToProfileFragment())
            }
        }
    }

}