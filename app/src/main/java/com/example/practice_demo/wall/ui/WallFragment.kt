package com.example.practice_demo.wall.ui

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.practice_demo.R
import com.example.practice_demo.databinding.FragmentWallBinding
import com.example.practice_demo.helper.SaveSharedPreference
import com.example.practice_demo.wall.data.model.PostDatabase
import com.example.practice_demo.wall.data.model.PostItem
import kohii.v1.exoplayer.Kohii
import java.io.IOException

class WallFragment : Fragment() {
    private lateinit var binding: FragmentWallBinding
    private lateinit var wallViewModel: WallViewModel

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

        wallViewModel.postDeletedFlag.observe(viewLifecycleOwner, {isDeleted: Boolean? ->

            if (isDeleted != null) {
                if (isDeleted) {
                    wallViewModel.feedWall()
                    Toast.makeText(context, getString(R.string.post_delete_toast), Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, getString(R.string.post_delete_fail_toast), Toast.LENGTH_LONG).show()
                }
            }
        })

        wallViewModel.networkNotFoundFlag.observe(viewLifecycleOwner, {networkErr: Boolean? ->
            if (networkErr != null && networkErr) {
                Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_LONG).show()
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

        binding.floatingActionButton.setOnClickListener {
            view.findNavController().navigate(WallFragmentDirections.actionWallFragmentToNewPostActivity())
        }
    }

    /**
     * On resume vzdy dotiahneme cerstve data
     */
    override fun onResume() {
        super.onResume()
        wallViewModel.feedWall()
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

        val userInstance =  SaveSharedPreference.getUser(requireContext()) ?: throw RuntimeException("User not found")

        val adapter = PostAdapter(
            userInstance,
            kohii,
            this,
            object: DiffUtil.ItemCallback<PostItem>() {
                override fun areItemsTheSame(
                    oldItem: PostItem,
                    newItem: PostItem
                ): Boolean =
                    oldItem == newItem

                override fun areContentsTheSame(
                    oldItem: PostItem,
                    newItem: PostItem
                ): Boolean =
                    oldItem.postid == newItem.postid
            }
        )

        binding.postList.adapter = adapter

        // Observujeme dotiahnutie postov do viewmodelu (refresh alebo zapnutie wallfragmentu)
        wallViewModel.postsList.observe(viewLifecycleOwner, { postsList ->
            adapter.submitList(postsList)
        })

        // Snap helper zabezpeci, ze pri scrollnuti nas rovno hodi na dalsi prispevok (Parametrom
        // vieme urcit ci swipe ma byt vertikalny alebo horizontalny
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val snapHelper = PagerSnapHelper()

        binding.postList.layoutManager = layoutManager
        snapHelper.attachToRecyclerView(binding.postList)

        setupListChangeEvents(adapter, layoutManager)
    }

    private fun setupListChangeEvents(adapter: PostAdapter, manager: RecyclerView.LayoutManager) {
        adapter.registerAdapterDataObserver(object: RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                manager.scrollToPosition(0)
            }
            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                manager.scrollToPosition(0)
            }
            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                manager.scrollToPosition(0)
            }
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                manager.scrollToPosition(0)
            }
            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                manager.scrollToPosition(0)
            }
            override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
                manager.scrollToPosition(0)
            }
        })
    }

    private fun logout() {
        activity?.let { SaveSharedPreference.clearUsername(it) }
        findNavController().navigate(WallFragmentDirections.actionWallFragmentToLoginFragment())
    }

    fun deletePost(postId: Int) {
        wallViewModel.deletePost(postId)
    }
}