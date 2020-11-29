package com.example.practice_demo.helper

import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import java.io.IOException

// extension function for show toast
fun Context.toast(text: String){
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

class PlayerManager(
) {
    companion object {
        val playersHolder = mutableMapOf<Int, SimpleExoPlayer>()
        var currentPlayingVideo: Pair<Int, SimpleExoPlayer>? = null
        var context: Context? = null

        fun releaseAll() {
            playersHolder.map {
                it.value.release()
            }
        }

        fun releaseAtPosition(position: Int) {
            playersHolder[position]?.release()
        }

        fun pauseCurrentPlayingVideo() {
            currentPlayingVideo?.second?.let {
                it.playWhenReady = false
            }
        }

        fun playIndexThenPausePreviousPlayer(index: Int) {

            if (playersHolder[index]?.playWhenReady == false) {
                pauseCurrentPlayingVideo()

                playersHolder[index]?.playWhenReady = true
                currentPlayingVideo = Pair(index, playersHolder[index]!!)
            }

        }

        fun constructPlayer(index: Int, url: String) {
            if (context == null) {
                throw IOException("Set context to PlayerManager before playing")
            }

            val player = SimpleExoPlayer.Builder(context!!)
                .build()

            player.playWhenReady = false

            if (playersHolder.containsKey(index)) {
                playersHolder.remove(index)
            }

            playersHolder[index] = player

            val mediaSource: MediaSource =
                ExtractorMediaSource.Factory(DefaultHttpDataSourceFactory("exoplayer-useragent"))
                    .createMediaSource(Uri.parse(url))

            player.prepare(mediaSource)

            player.addListener(object: Player.EventListener {
                override fun onPlayerError(error: ExoPlaybackException) {
                    super.onPlayerError(error)
                    toast("Oops! Error occurred while playing media.")
                }

                override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                    super.onPlayerStateChanged(playWhenReady, playbackState)
                    if (playWhenReady) {
                        player.play()
                    }
                }
            })
        }

        fun toast(str: String) {
            context?.let {
                Toast.makeText(it, str, Toast.LENGTH_SHORT).show()
            }
        }
    }
}