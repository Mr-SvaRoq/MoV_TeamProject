package com.example.practice_demo.wall.ui

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.practice_demo.R
import com.example.practice_demo.databinding.FragmentWallBinding
import com.example.practice_demo.helper.SaveSharedPreference
import com.example.practice_demo.wall.data.model.PostDatabase
import com.example.practice_demo.wall.data.model.PostItemRecycler
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kohii.v1.exoplayer.Kohii
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
        activity?.let { activity ->
            val user = SaveSharedPreference.getUser(activity)
                ?: throw IOException("User not found")

            val localDataSource = PostDatabase.getInstance(
                activity.application
            ).postDatabaseDao

            wallViewModel = ViewModelProvider(this, WallViewModelFactory(user, localDataSource))
                .get(WallViewModel::class.java)
        }

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_wall, container, false)

        wallViewModel.unauthorisedFlag.observe(viewLifecycleOwner, {isUnauthorised ->
            if (isUnauthorised) {
                logout()
            }
        })

        setupRecyclerView()

        // Specify the current activity as the lifecycle owner of the binding.
        // This is necessary so that the binding can observe LiveData updates.
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val actionButton = view.findViewById<FloatingActionButton>(R.id.floatingActionButton)

        actionButton.setOnClickListener {
//            Log.d("TAG", "CLICKED MUHAHAHHA")
            view.findNavController().navigate(R.id.action_wallFragment_to_newPostActivity)
        }
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
        when (item.itemId) {
            R.id.menu_viewProfile -> {
                findNavController().navigate(WallFragmentDirections.actionWallFragmentToProfileFragment())
            }
        }
    }

    private fun setupRecyclerView() {
        val kohii = Kohii[this]
        kohii.register(this).addBucket(binding.postList)

        val adapter = PostAdapter(
            kohii,
            this,
            object: DiffUtil.ItemCallback<PostItemRecycler>() {
                override fun areItemsTheSame(
                    oldItem: PostItemRecycler,
                    newItem: PostItemRecycler
                ): Boolean =
                    oldItem == newItem

                override fun areContentsTheSame(
                    oldItem: PostItemRecycler,
                    newItem: PostItemRecycler
                ): Boolean =
                    oldItem.index == newItem.index
            }
        )

        binding.postList.adapter = adapter

        // Observujeme dotiahnutie postov do viewmodelu (refresh alebo zapnutie wallfragmentu)
        wallViewModel.postsList.observe(viewLifecycleOwner, { postsList ->
            val newLists = arrayListOf<PostItemRecycler>()

            for ((index, post) in postsList.withIndex()) {
                newLists.add(PostItemRecycler(post, index))
            }

            adapter.submitList(newLists)
        })

        // Snap helper zabezpeci, ze pri scrollnuti nas rovno hodi na dalsi prispevok (Parametrom
        // vieme urcit ci swipe ma byt vertikalny alebo horizontalny
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val snapHelper = PagerSnapHelper()

        binding.postList.layoutManager = layoutManager
        snapHelper.attachToRecyclerView(binding.postList)

        // Nafeeduj nastenku
        wallViewModel.feedWall()
    }

    private fun logout() {
        activity?.let { SaveSharedPreference.clearUsername(it) }
        findNavController().navigate(WallFragmentDirections.actionWallFragmentToLoginFragment())
    }
}