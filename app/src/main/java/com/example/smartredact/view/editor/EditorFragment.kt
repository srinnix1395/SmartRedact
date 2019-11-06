package com.example.smartredact.view.editor

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.smartredact.R
import com.example.smartredact.common.facerecoginition.Classifier
import com.example.smartredact.common.facerecoginition.TensorFlowYoloDetector
import com.example.smartredact.common.facerecoginition.env.ImageUtils
import com.example.smartredact.common.utils.TimeUtils
import com.example.smartredact.common.utils.VideoUtils
import com.example.smartredact.common.utils.addToCompositeDisposable
import com.example.smartredact.data.model.VideoMetadata
import com.example.smartredact.view.dialog.ProgressCommonDialog
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SeekParameters
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util.getUserAgent
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_editor.*
import java.io.IOException


/**
 * Created by TuHA on 10/31/2019.
 */
class EditorFragment : Fragment() {

    companion object {
        const val REQUEST_CODE_SELECT_FILE = 1234
        const val FRAME_HEIGHT_FACTOR = 0.625F
        const val INTERVAL = 25L

        const val YOLO_MODEL_FILE = "yolo2_face.tflite"
        const val YOLO_INPUT_SIZE = 416
        const val YOLO_BLOCK_SIZE = 32

        const val MINIMUM_CONFIDENCE_YOLO = 0.25
    }

    private var player: SimpleExoPlayer? = null
    private var videoMetadata: VideoMetadata? = null

    private var updateSeekBarHandler: Handler = Handler()
    private val updateSeekBarRunnable: UpdateSeekBarRunnable = UpdateSeekBarRunnable()

    private var detector: Classifier? = null
    private var croppedBitmap: Bitmap? = null
    private var frameToCropTransform: Matrix? = null
    private var cropToFrameTransform: Matrix? = null

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    private val progressDialog: ProgressCommonDialog by lazy {
        return@lazy ProgressCommonDialog(context)
    }

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
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                if (isPlaying) {
                    timeLineView.setEnabledScrollToUpdate(false)
                    updateSeekBarHandler.post(updateSeekBarRunnable)
                    imvPlayPause.setImageResource(R.drawable.ic_pause)
                } else {
                    stopPlayer()
                    timeLineView.setEnabledScrollToUpdate(true)
                    updateSeekBarHandler.removeCallbacks(updateSeekBarRunnable)
                    imvPlayPause.setImageResource(R.drawable.ic_play)
                    updateSeekBar()
                }
            }
        })
        playerControlView.player = player

        timeLineView.apply {
            setOnProgressChanged { progress, total ->
                calculateTextCurrentTime(progress, total, true)
            }
            setOnScrollStateChanged { state ->
                if (state == RecyclerView.SCROLL_STATE_DRAGGING && player?.isPlaying == true) {
                    stopPlayer()
                }
            }
        }

        imvDetectFace.setOnClickListener {
            detectFaces()
        }

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
        val dataSourceFactory = DefaultDataSourceFactory(context, getUserAgent(context, "SmartRedact"))
        val videoSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(data)
        player?.prepare(videoSource, true, false)
    }

    private fun processVideo(data: Uri) {
        Single
            .fromCallable {
                return@fromCallable VideoUtils.extractMetadata(
                    context,
                    data,
                    timeLineView.height * FRAME_HEIGHT_FACTOR
                )
            }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                showProgress()
            }
            .doFinally {
                dismissProgress()
            }
            .subscribe({ videoMetadata ->
                this.videoMetadata = videoMetadata
                tvCurrentTime.text = TimeUtils.format(0L)
                tvDuration.text = TimeUtils.format(videoMetadata.duration)
                timeLineView.setData(videoMetadata.frame)
            }, {
                it.printStackTrace()
            })
            .addToCompositeDisposable(compositeDisposable)
    }

    private fun releasePlayer() {
        if (player == null) {
            return
        }

        player!!.release()
        player = null
    }

    private fun calculateTextCurrentTime(progress: Float, total: Float, enabledSeek: Boolean) {
        if (videoMetadata == null) {
            return
        }

        val currentTime = ((progress * videoMetadata!!.duration) / total).toLong()
        tvCurrentTime.text = TimeUtils.format(currentTime)
        if (enabledSeek) {
            player?.seekTo(currentTime)
        }
    }

    private fun playOrPause() {
        if (videoMetadata == null || player == null || player!!.playbackState != Player.STATE_READY) {
            return
        }

        player?.playWhenReady = !player!!.isPlaying
    }

    private fun stopPlayer() {
        player?.playWhenReady = false
    }

    private fun updateSeekBar() {
        val progressTime = player!!.currentPosition
        val totalWidth = videoMetadata!!.frame.width * videoMetadata!!.frame.frames.size
        val progressX = (progressTime * totalWidth) / videoMetadata!!.duration
        val position = (progressX / videoMetadata!!.frame.width).toInt()
        val offset = (progressX % videoMetadata!!.frame.width).toInt()
        timeLineView.scrollToPositionWithOffset(position, -offset)
        calculateTextCurrentTime(progressX, totalWidth, false)
    }

    private fun showProgress() {
        if (!progressDialog.isShowing) progressDialog.show()
    }

    private fun dismissProgress() {
        if (progressDialog.isShowing) progressDialog.dismiss()
    }

    private fun detectFaces() {
        if (videoMetadata == null) {
            return
        }

        if (detector == null) {
            initDetector()
        }

        Single
            .fromCallable {
                val frames = VideoUtils.extractFrames(context, videoMetadata!!.uri, videoMetadata!!.frame.frames.size)
                val results = arrayListOf<Classifier.Recognition>()

                frames.forEach {
                    Canvas(croppedBitmap!!).apply {
                        drawBitmap(it, frameToCropTransform!!, null)
                    }
                    detector?.recognizeImage(croppedBitmap)?.let {
                        results.addAll(it)
                    }
                }

                val mappedRecognitions = arrayListOf<Classifier.Recognition>()

                for (result in results         ) {
                    val location = result.location
                    if (location != null && result.confidence >= MINIMUM_CONFIDENCE_YOLO) {
                        cropToFrameTransform?.mapRect(location)
                        result.location = location
                        mappedRecognitions.add(result)
                    }
                }

                return@fromCallable mappedRecognitions
            }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                showProgress()
            }
            .doFinally {
                dismissProgress()
            }
            .subscribe({ results ->
                println(results.size)
            }, {
                it.printStackTrace()
            })
            .addToCompositeDisposable(compositeDisposable)
    }

    private fun initDetector() {
        try {
            detector = TensorFlowYoloDetector.create(
                context?.assets,
                YOLO_MODEL_FILE,
                YOLO_INPUT_SIZE,
                YOLO_BLOCK_SIZE)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val previewWidth = videoMetadata!!.width.toInt()
        val previewHeight = videoMetadata!!.height.toInt()
        val cropSize = YOLO_INPUT_SIZE
        croppedBitmap = Bitmap.createBitmap(cropSize, cropSize, Bitmap.Config.ARGB_8888)

        val sensorOrientation = 90
        frameToCropTransform = ImageUtils.getTransformationMatrix(
            previewWidth, previewHeight,
            cropSize, cropSize,
            sensorOrientation, true)

        cropToFrameTransform = Matrix()
        frameToCropTransform?.invert(cropToFrameTransform)
    }

    inner class UpdateSeekBarRunnable : Runnable {

        override fun run() {
            updateSeekBar()
            updateSeekBarHandler.postDelayed(this, INTERVAL)
        }
    }
}