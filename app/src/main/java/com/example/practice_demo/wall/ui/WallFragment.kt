package com.example.practice_demo.wall.ui

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.practice_demo.R
import com.example.practice_demo.databinding.FragmentWallBinding
import com.example.practice_demo.helper.SaveSharedPreference
import com.example.practice_demo.wall.data.model.PostItem

class WallFragment : Fragment() {
    lateinit var binding: FragmentWallBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Options menu
        setHasOptionsMenu(true)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_wall, container, false)

        val adapter = PostAdapter()

        binding.postList.adapter = adapter

        adapter.data = getDummyShitData()

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

    private fun getDummyShitData(): List<PostItem> {
        return listOf(
            PostItem(
                1,
                "2020-04-05 02:12:33",
                "video23.mp4",
                "Dalai lama",
                ""
            ),
            PostItem(
                2,
                "2020-04-01 13:14:33",
                "video.mp4",
                "Vladimir Putin",
                ""
            ),
            PostItem(
                3,
                "2020-01-01 12:12:33",
                "video.mp4",
                "Vladimir Putin",
                ""
            )
        )
    }
}