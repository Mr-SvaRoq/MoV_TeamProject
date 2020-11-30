package com.example.practice_demo.wall.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.practice_demo.R
import com.example.practice_demo.databinding.FragmentWallBinding
import com.example.practice_demo.helper.SaveSharedPreference
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

            wallViewModel = ViewModelProvider(this, WallViewModelFactory(user))
                .get(WallViewModel::class.java)
        }

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_wall, container, false)

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
            Log.d("TAG", "CLICKED MUHAHAHHA")
            view.findNavController().navigate(R.id.action_wallFragment_to_newPostFragment)
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
            //TODO(toto je len pre debug, prvy nahrany prispevok zopakuje 10x
            // pokial budeme mat nahranych viac prispevkov, moze sa dat do prdele)
            val newLists = arrayListOf<PostItemRecycler>()
            for (x in 0..10) {
                newLists.add(PostItemRecycler(postsList[0], x))
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
}