package com.example.smartredact.view.editor

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.smartredact.R
import com.example.smartredact.common.utils.TimeUtils
import com.example.smartredact.common.utils.VideoUtils
import com.example.smartredact.data.model.VideoMetadata
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SeekParameters
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util.getUserAgent
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_editor.*

/**
 * Created by TuHA on 10/31/2019.
 */
class EditorFragment : Fragment() {

    companion object {
        const val REQUEST_CODE_SELECT_FILE = 1234
        const val FRAME_HEIGHT_FACTOR = 0.625F
    }

    private var player: SimpleExoPlayer? = null
    private var videoMetadata: VideoMetadata? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_editor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        player = ExoPlayerFactory.newSimpleInstance(context)
        player?.seekParameters = SeekParameters.EXACT
        player?.addListener(object : Player.EventListener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
//                val currentPosition = player!!.currentPosition
//                val duration = player!!.duration
//                println("kamikaze $currentPosition/$duration")
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                if (isPlaying) {
                    imvPlayPause.setImageResource(R.drawable.ic_pause)
                } else {
                    imvPlayPause.setImageResource(R.drawable.ic_play)
                }
            }
        })
        playerControlView.player = player

        timeLineView.setOnProgressChanged(::onCurrentTimeChanged)

        imvPlayPause.setOnClickListener {
            playOrPause()
        }
        chooseFiles()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        releasePlayer()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null || resultCode != Activity.RESULT_OK) {
            return
        }

        when (requestCode) {
            REQUEST_CODE_SELECT_FILE -> {
                showVideo(data.data!!)
                processVideo(data.data!!)
            }
        }
    }

    private fun chooseFiles() {
        fun openIntentGetFile() {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "video/*"
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
            startActivityForResult(
                Intent.createChooser(intent, "Choose a file"),
                REQUEST_CODE_SELECT_FILE
            )
        }

        openIntentGetFile()
    }

    private fun showVideo(data: Uri) {
        val dataSourceFactory =
            DefaultDataSourceFactory(context, getUserAgent(context, "SmartRedact"))
        val videoSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(data)
        player?.prepare(videoSource, true, false)
    }

    private fun processVideo(data: Uri) {
        Single
            .fromCallable {
                return@fromCallable VideoUtils.extractFrames(
                    context,
                    data,
                    timeLineView.height * FRAME_HEIGHT_FACTOR
                )
            }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ videoMetadata ->
                this.videoMetadata = videoMetadata
                tvCurrentTime.text = TimeUtils.format(0L)
                tvDuration.text = TimeUtils.format(videoMetadata.duration)
                timeLineView.setData(videoMetadata.frame)
            }, {
                it.printStackTrace()
            })
    }

    private fun releasePlayer() {
        if (player == null) {
            return
        }

//        playbackPosition = player!!.currentPosition
//        currentWindow = player!!.currentWindowIndex
//        playWhenReady = player!!.playWhenReady
        player!!.release()
        player = null
    }

    private fun onCurrentTimeChanged(progress: Float, total: Float) {
        if (videoMetadata == null) {
            return
        }

        val currentTime = ((progress * videoMetadata!!.duration) / total).toLong()
        player?.seekTo(currentTime)
        tvCurrentTime.text = TimeUtils.format(currentTime)
    }

    private fun playOrPause() {
        if (videoMetadata == null || player == null || player!!.playbackState != Player.STATE_READY) {
            return
        }

        if (player!!.isPlaying) {
            pausePlayer()
        } else {
            startPlayer()
        }
    }

    private fun pausePlayer() {
        player?.playWhenReady = false
        player?.playbackState
    }

    private fun startPlayer() {
        player?.playWhenReady = true
        player?.playbackState
    }
}