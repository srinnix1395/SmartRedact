package com.example.smartredact.view.editor.video

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.smartredact.R
import com.example.smartredact.common.di.component.ActivityComponent
import com.example.smartredact.common.facerecoginition.Classifier
import com.example.smartredact.common.utils.TimeUtils
import com.example.smartredact.common.widget.faceview.Face
import com.example.smartredact.data.model.VideoMetadata
import com.example.smartredact.view.base.BaseFragment
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SeekParameters
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util.getUserAgent
import com.google.android.exoplayer2.video.VideoListener
import kotlinx.android.synthetic.main.fragment_editor.*
import javax.inject.Inject


/**
 * Created by TuHA on 10/31/2019.
 */
class EditorVideoFragment : BaseFragment(), EditorVideoView {

    companion object {
        const val FRAME_HEIGHT_FACTOR = 0.625F
        const val INTERVAL = 30L
    }

    @Inject
    lateinit var mPresenter: EditorVideoPresenter

    private lateinit var player: SimpleExoPlayer
    private var renderedWidth: Float = 0F
    private var renderedHeight: Float = 0F

    private var updateTimeLineBarHandler: Handler = Handler()
    private val updateTimeLineRunnable: UpdateSeekBarRunnable = UpdateSeekBarRunnable()

    override fun inject(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_editor
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter.attachView(this)
    }

    override fun onDestroy() {
        releaseHandler()
        releasePlayer()
        mPresenter.detachView()
        super.onDestroy()
    }

    override fun initView() {
        player = ExoPlayerFactory.newSimpleInstance(context)
        player.seekParameters = SeekParameters.EXACT
        player.addListener(object : Player.EventListener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                if (isPlaying) {
                    timeLineView.setEnabledScrollToUpdate(false)
                    startUpdateTimeLineView()
                    imvPlayPause.setImageResource(R.drawable.ic_pause)
                } else {
                    pausePlayer()
                    timeLineView.setEnabledScrollToUpdate(true)
                    stopUpdateTimeLineView()
                    imvPlayPause.setImageResource(R.drawable.ic_play)
                    mPresenter.updateSeekBar(player.currentPosition)
                }
            }
        })
        player.addVideoListener(object : VideoListener {
            override fun onSurfaceSizeChanged(width: Int, height: Int) {
                renderedWidth = width.toFloat()
                renderedHeight = height.toFloat()
            }
        })
        playerControlView.player = player

        timeLineView.apply {
            setOnProgressChanged { progress, total ->
                mPresenter.calculateTextCurrentTime(progress, total, true)
            }
            setOnScrollStateChanged { state ->
                if (state == RecyclerView.SCROLL_STATE_DRAGGING && player.isPlaying) {
                    pausePlayer()
                }
            }
        }

        imvDetectFace.setOnClickListener {
            mPresenter.detectFaces(renderedWidth, renderedHeight)
        }
        imvPlayPause.setOnClickListener {
            playOrPause()
        }
        imvNextFrame.setOnClickListener {
            seekToNextFrame()
        }
        imvPrevFrame.setOnClickListener {
            seekToPreviousFrame()
        }
    }

    override fun initData() {
        mPresenter.getArguments(arguments)

        Handler().post {
            val frameHeight = timeLineView.height * FRAME_HEIGHT_FACTOR
            mPresenter.getMetadataVideo(frameHeight)
        }
    }

    //region implement EditorVideoView
    override fun showVideo(videoMetadata: VideoMetadata) {
        val dataSourceFactory = DefaultDataSourceFactory(context, getUserAgent(context, "SmartRedact"))
        val videoSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(videoMetadata.data)
        player.prepare(videoSource, true, false)

        tvCurrentTime.text = TimeUtils.format(0L, true)
        tvDuration.text = TimeUtils.format(videoMetadata.duration, true)
        timeLineView.setData(videoMetadata.frame)
    }

    override fun updateFrame(bitmap: Bitmap, position: Int) {
        timeLineView.updateFrame(bitmap, position)
    }

    override fun showCurrentTime(currentTimeText: String) {
        tvCurrentTime.text = currentTimeText
    }

    override fun seekTo(currentTime: Long) {
        player.seekTo(currentTime)
    }

    override fun scrollTimeLineView(position: Int, offset: Int) {
        timeLineView.scrollToPositionWithOffset(position, offset)
    }

    override fun showListFace(listFace: List<Face>) {
        listFaceView.setData(listFace)

        val recognitions = arrayListOf<Classifier.Recognition>()
        listFace.forEach { face ->
            recognitions.addAll(face.face)
        }
        overlayView.setData(recognitions)
        Toast.makeText(context, recognitions[0].startTime.toString(), Toast.LENGTH_LONG).show()
    }
    //endregion implement EditorVideoView

    private fun releasePlayer() {
        player.release()
    }

    private fun releaseHandler() {
        updateTimeLineBarHandler.removeCallbacks(updateTimeLineRunnable)
    }

    private fun playOrPause() {
        if (player.isPlaying) {
            pausePlayer()
        } else {
            if (player.playbackState == Player.STATE_ENDED) {
                player.seekTo(0)
            }
            playPlayer()
        }
    }

    private fun playPlayer() {
        player.playWhenReady = true
    }

    private fun pausePlayer() {
        player.playWhenReady = false
    }

    private fun seekToNextFrame() {
        val nextPosition = Math.min(player.currentPosition + 1000, player.duration)
        player.seekTo(nextPosition)
        if (!player.isPlaying) {
            mPresenter.updateSeekBar(nextPosition)
        }
    }

    private fun seekToPreviousFrame() {
        val previousPosition = Math.max(0, player.currentPosition - 1000)
        player.seekTo(previousPosition)
        if (!player.isPlaying) {
            mPresenter.updateSeekBar(previousPosition)
        }
    }

    private fun startUpdateTimeLineView() {
        updateTimeLineBarHandler.removeCallbacks(updateTimeLineRunnable)
        updateTimeLineBarHandler.post(updateTimeLineRunnable)
    }

    private fun stopUpdateTimeLineView() {
        updateTimeLineBarHandler.removeCallbacks(updateTimeLineRunnable)
    }

    inner class UpdateSeekBarRunnable : Runnable {

        override fun run() {
            if (player.playbackState == Player.STATE_IDLE) {
                return
            }

            mPresenter.updateSeekBar(player.currentPosition)
            updateTimeLineBarHandler.postDelayed(this, INTERVAL)
        }
    }
}