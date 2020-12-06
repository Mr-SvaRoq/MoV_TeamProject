package com.example.practice_demo.wall.ui

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.practice_demo.R
import com.example.practice_demo.databinding.PostViewBinding
import com.example.practice_demo.helper.Constants
import com.example.practice_demo.wall.data.model.PostItem
import com.google.android.exoplayer2.ui.PlayerView
import kohii.v1.core.Common
import kohii.v1.core.Playback
import kohii.v1.exoplayer.Kohii

class PostViewHolder(val binding: PostViewBinding) : RecyclerView.ViewHolder(binding.root) {
    val playerView: PlayerView = binding.itemVideoExoplayer
}

class PostAdapter(
    private val kohii: Kohii,
    private val fragment: Fragment,
    diffCallback: DiffUtil.ItemCallback<PostItem>
) : ListAdapter<PostItem, PostViewHolder>(diffCallback) {

    // Kohii si pri nacitani predoslych recycler itemov nedokazal zapamatat ktore
    // videa sme nechali stopnute a ktore nie. Na to sluzi tato mapa
    private val pausedVideos: MutableMap<Int, Boolean> = mutableMapOf()

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val item = getItem(position) ?: return
        kohii.setUp(Constants.Api.MEDIA_URL + item.videourl) {
            tag = "${Constants.Api.MEDIA_URL + item.videourl}+${position}"
            preload = true
            repeatMode = Common.REPEAT_MODE_ONE
            controller = object : Playback.Controller {
                override fun kohiiCanStart(): Boolean = true

                override fun kohiiCanPause(): Boolean = true

                override fun setupRenderer(playback: Playback, renderer: Any?) {
                    onSetupRenderer(holder, position, playback)
                }

                override fun teardownRenderer(playback: Playback, renderer: Any?) {
                    playback.container.setOnClickListener(null)
                }
            }
        }
            .bind(holder.playerView)


        // Ak ma autor profilovku, nacitame (Inak defaultny obrazok)
        if (item.profile != "") {
            Glide.with(fragment)
                .load(Constants.Api.MEDIA_URL + item.profile)
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(holder.binding.profilePicture)
        }

        // Setneme binding model
        holder.binding.dataModel = item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = PostViewBinding.inflate(layoutInflater, parent, false)

        return PostViewHolder(binding)
    }

    /**
     * Handlovanie stop a resume videa cez kliknutie
     */
    private fun onSetupRenderer(holder: PostViewHolder, position: Int, playback: Playback) {

        pausedVideos[position]?.let { isPlaying ->
            // Ak mame ulozenu hodnotu, vytiahneme z pamati
            holder.binding.pauseVisible = if (isPlaying) View.GONE else View.VISIBLE
        } ?: run {
            // Ak nie, video bezi, takze pauzu nezobrazujeme
            holder.binding.pauseVisible = View.GONE
        }

        holder.playerView.videoSurfaceView?.setOnClickListener {
            val playable = playback.playable ?: return@setOnClickListener

            // Video je prehravane, chceme ho zastavit
            val videoToastText = if (playable.isPlaying()) {
                holder.binding.pauseVisible = View.VISIBLE
                playback.manager.pause(playable)
                fragment.context?.getString(R.string.toast_video_stopped)
            } else { // Video je zastavene, pokracujeme v prehravani
                holder.binding.pauseVisible = View.GONE
                playback.manager.play(playable)
                fragment.context?.getString(R.string.toast_video_resumed)
            }

            // Zmenil sa stav, setujeme opacne
            pausedVideos[position] = !playable.isPlaying()

            val toast = Toast
                .makeText(fragment.context, videoToastText, Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 200)
            toast.show()
        }
    }
}
