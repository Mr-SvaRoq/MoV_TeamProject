package com.example.practice_demo.wall.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.practice_demo.databinding.PostViewBinding
import com.example.practice_demo.helper.PlayerManager
import com.example.practice_demo.wall.data.model.PostItem

const val mediaUrlPrefix = "http://api.mcomputing.eu/mobv/uploads/"

class PostViewHolder(val binding: PostViewBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: PostItem, position: Int, context: Context) {
        PlayerManager.playIndexThenPausePreviousPlayer(position)

        binding.itemVideoExoplayer.player = PlayerManager.currentPlayingVideo?.second

        binding.dataModel = item
        binding.executePendingBindings()
    }
}

class PostAdapter(val context: Context): RecyclerView.Adapter<PostViewHolder>() {
    var data = listOf<PostItem>()
        set(value) {
            field = value
            //todo: Toto neskor prec, lebo to updatuje cely list pri zmene
            notifyDataSetChanged()
        }

    init {
        PlayerManager.context = context
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val item = data[position]
        PlayerManager.constructPlayer(position, mediaUrlPrefix + item.videourl)
        holder.bind(item, position, context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val binding = PostViewBinding.inflate(layoutInflater, parent, false)

        return PostViewHolder(binding)
    }

    override fun onViewRecycled(holder: PostViewHolder) {
        val position = holder.adapterPosition

        // Uvolnime player na konkretnej pozciii
        PlayerManager.releaseAtPosition(position)

        super.onViewRecycled(holder)
    }
}
