package com.example.smartredact.view.editor.video

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import androidx.recyclerview.widget.RecyclerView
import com.example.smartredact.R
import com.example.smartredact.common.di.component.ActivityComponent
import com.example.smartredact.common.utils.TimeUtils
import com.example.smartredact.data.model.VideoMetadata
import com.example.smartredact.view.base.BaseFragment
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SeekParameters
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util.getUserAgent
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

    private var player: SimpleExoPlayer? = null

    private var updateTimeLineBarHandler: Handler = Handler()
    private val updateTimeLineRunnable: UpdateSeekBarRunnable = UpdateSeekBarRunnable()

//    private var detector: Classifier? = null
//    private var croppedBitmap: Bitmap? = null
//    private var frameToCropTransform: Matrix? = null
//    private var cropToFrameTransform: Matrix? = null

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
        releasePlayer()
        mPresenter.detachView()
        super.onDestroy()
    }

    override fun initView() {
        player = ExoPlayerFactory.newSimpleInstance(context)
        player?.seekParameters = SeekParameters.EXACT
        player?.addListener(object : Player.EventListener {
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
                    mPresenter.updateSeekBar(player!!.currentPosition)
                }
            }
        })
        playerControlView.player = player

        timeLineView.apply {
            setOnProgressChanged { progress, total ->
                mPresenter.calculateTextCurrentTime(progress, total, true)
            }
            setOnScrollStateChanged { state ->
                if (state == RecyclerView.SCROLL_STATE_DRAGGING && player?.isPlaying == true) {
                    pausePlayer()
                }
            }
        }

        imvDetectFace.setOnClickListener {
            detectFaces()
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
        player?.prepare(videoSource, true, false)

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
        player?.seekTo(currentTime)
    }

    override fun scrollTimeLineView(position: Int, offset: Int) {
        timeLineView.scrollToPositionWithOffset(position, offset)
    }
    //endregion implement EditorVideoView

    private fun releasePlayer() {
        if (player == null) {
            return
        }

        player!!.release()
        player = null
    }

    private fun playOrPause() {
        if (player == null) {
            return
        }

        if (player!!.isPlaying) {
            pausePlayer()
        } else {
            if (player!!.playbackState == Player.STATE_ENDED) {
                player!!.seekTo(0)
            }
            playPlayer()
        }
    }

    private fun playPlayer() {
        player?.playWhenReady = true
    }

    private fun pausePlayer() {
        player?.playWhenReady = false
    }

    private fun seekToNextFrame() {
        if (player == null) {
            return
        }

        val currentPosition = player!!.currentPosition
        player!!.seekTo(currentPosition + 1000)
    }

    private fun seekToPreviousFrame() {
        if (player == null) {
            return
        }

        val currentPosition = player!!.currentPosition
        player!!.seekTo(currentPosition - 1000)
    }

    private fun detectFaces() {
//        if (videoMetadata == null) {
//            return
//        }
//
//        if (detector == null) {
//            initDetector()
//        }
//
//        Single
//                .fromCallable {
//                    val frames = VideoUtils.extractFrames(context, videoMetadata!!.uri, videoMetadata!!.frame.count)
//                    val mappedRecognitions = ArrayMap<String, ArrayList<Classifier.Recognition>>()
//
//                    frames.forEachIndexed { index, bitmap ->
//                        Canvas(croppedBitmap!!).apply {
//                            drawBitmap(bitmap, frameToCropTransform!!, null)
//                        }
//                        detector?.recognizeImage(croppedBitmap)?.let { listRecognitions ->
//                            listRecognitions.forEach {
//                                val location = it.location
//                                if (location != null && it.confidence >= Constant.MINIMUM_CONFIDENCE_YOLO) {
//                                    cropToFrameTransform?.mapRect(location)
//                                    it.location = location
//
//                                    it.time = index.toLong()
//
//                                    val list = mappedRecognitions[it.id]
//                                    if (list == null) {
//                                        mappedRecognitions[it.id] = arrayListOf(it)
//                                    } else {
//                                        list.add(it)
//                                    }
//                                }
//                            }
//                        }
//                    }
//
//                    mappedRecognitions.values.forEach { value ->
//                        value?.sortBy { it.time }
//                    }
//
//                    return@fromCallable mappedRecognitions.map { map ->
//                        val firstFace = map.value.first()
//                        val lastFace = map.value.last()
//
//                        Face(map.key, firstFace.time, lastFace.time, map.value)
//                    }
//                }
//                .subscribeOn(Schedulers.computation())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnSubscribe {
//                    showProgress()
//                }
//                .doFinally {
//                    dismissProgress()
//                }
//                .subscribe({ results ->
//                    faceView.setData(results)
//                }, {
//                    it.printStackTrace()
//                })
//                .addToCompositeDisposable(compositeDisposable)
    }

//    private fun initDetector() {
//        try {
//            detector = TensorFlowYoloDetector.create(
//                context?.assets,
//                Constant.YOLO_MODEL_FILE,
//                Constant.YOLO_INPUT_SIZE,
//                Constant.YOLO_BLOCK_SIZE)
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//
//        val previewWidth = videoMetadata.width.toInt()
//        val previewHeight = videoMetadata.height.toInt()
//        val cropSize = Constant.YOLO_INPUT_SIZE
//        croppedBitmap = Bitmap.createBitmap(cropSize, cropSize, Bitmap.Config.ARGB_8888)
//
//        val sensorOrientation = 90
//        frameToCropTransform = ImageUtils.getTransformationMatrix(
//            previewWidth, previewHeight,
//            cropSize, cropSize,
//            sensorOrientation, true)
//
//        cropToFrameTransform = Matrix()
//        frameToCropTransform?.invert(cropToFrameTransform)
//    }

    private fun startUpdateTimeLineView() {
        updateTimeLineBarHandler.removeCallbacks(updateTimeLineRunnable)
        updateTimeLineBarHandler.post(updateTimeLineRunnable)
    }

    private fun stopUpdateTimeLineView() {
        updateTimeLineBarHandler.removeCallbacks(updateTimeLineRunnable)
    }

    inner class UpdateSeekBarRunnable : Runnable {

        override fun run() {
            mPresenter.updateSeekBar(player!!.currentPosition)
            updateTimeLineBarHandler.postDelayed(this, INTERVAL)
        }
    }
}