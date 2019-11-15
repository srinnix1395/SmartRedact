package com.example.smartredact.view.editor.video

import android.os.Bundle
import com.example.smartredact.common.constants.Constants
import com.example.smartredact.common.extension.addToCompositeDisposable
import com.example.smartredact.common.facerecoginition.ObjectDetectionUtils
import com.example.smartredact.common.utils.TimeUtils
import com.example.smartredact.common.utils.VideoUtils
import com.example.smartredact.data.model.Session
import com.example.smartredact.data.model.VideoMetadata
import com.example.smartredact.view.base.BasePresenter
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by TuHA on 11/11/2019.
 */
class EditorVideoPresenter @Inject constructor() : BasePresenter<EditorVideoView>() {

    @Inject
    lateinit var videoUtils: VideoUtils

    @Inject
    lateinit var objectDetectionUtils: ObjectDetectionUtils

    private lateinit var session: Session
    private lateinit var videoMetadata: VideoMetadata

    override fun getArguments(extras: Bundle?) {
        super.getArguments(extras)
        session = extras?.getParcelable(Constants.KEY_SESSION)!!
    }

    fun getMetadataVideo(frameHeight: Float) {
        Single
            .fromCallable {
                return@fromCallable videoUtils.extractMetadata(session.data!!, frameHeight)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                view?.showProgressDialog(true)
            }
            .doFinally {
                view?.showProgressDialog(false)
            }
            .subscribe({ videoMetadata ->
                this.videoMetadata = videoMetadata
                view?.showVideo(videoMetadata)
                extractFrames(videoMetadata)
                objectDetectionUtils.startSession(
                    videoMetadata.width.toInt(),
                    videoMetadata.height.toInt(),
                    videoMetadata.orientation
                )
            }, { error ->
                error.printStackTrace()
            })
            .addToCompositeDisposable(compositeDisposable)
    }

    private fun extractFrames(videoMetadata: VideoMetadata) {
        val frame = videoMetadata.frame

        videoUtils
            .extractFrames(
                videoMetadata.data,
                frame.count,
                frame.interval,
                frame.width.toInt(),
                frame.height.toInt()
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ (bitmap, position) ->
                view?.updateFrame(bitmap, position)
            }, { error ->
                error.printStackTrace()
            })
            .addToCompositeDisposable(compositeDisposable)
    }

    fun calculateTextCurrentTime(progress: Float, total: Float, enabledSeek: Boolean) {
        val currentTime = if (Math.abs(progress - total) <= 2) {
            videoMetadata.duration
        } else {
            ((progress * videoMetadata.duration) / total).toLong()
        }
        val currentTimeText = TimeUtils.format(currentTime, true)
        view?.showCurrentTime(currentTimeText)

        if (enabledSeek) {
            view?.seekTo(currentTime)
        }
    }

    fun updateSeekBar(currentPosition: Long) {
        var progressTime = currentPosition
        if (progressTime > videoMetadata.duration) {
            progressTime = videoMetadata.duration
        }
        val totalWidth = videoMetadata.frame.width * videoMetadata.frame.count
        val progressX = (progressTime * totalWidth) / videoMetadata.duration
        val position = (progressX / videoMetadata.frame.width).toInt()
        val offset = (progressX % videoMetadata.frame.width).toInt()

        view?.scrollTimeLineView(position, -offset)
        calculateTextCurrentTime(progressX, totalWidth, false)
    }

    fun detectFaces(
        renderedWidth: Float,
        renderedHeight: Float,
        paddingHorizontal: Float,
        paddingVertical: Float
    ) {
        Single
            .fromCallable {
                val srcWidth = videoMetadata.width
                val srcHeight = videoMetadata.height
                return@fromCallable objectDetectionUtils.detectFacesVideo(
                    videoMetadata.data,
                    videoMetadata.duration,
                    srcWidth,
                    srcHeight,
                    renderedWidth,
                    renderedHeight,
                    paddingHorizontal,
                    paddingVertical
                )
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                view?.showProgressDialog(true)
            }
            .doFinally {
                view?.showProgressDialog(false)
            }
            .subscribe({ listFace ->
                view?.showListFace(listFace)
            }, { error ->
                error.printStackTrace()
            })
            .addToCompositeDisposable(compositeDisposable)
    }
}