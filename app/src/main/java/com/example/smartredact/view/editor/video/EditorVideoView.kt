package com.example.smartredact.view.editor.video

import android.graphics.Bitmap
import com.example.smartredact.common.widget.faceview.Face
import com.example.smartredact.data.model.VideoMetadata
import com.example.smartredact.view.base.BaseView

/**
 * Created by TuHA on 11/11/2019.
 */
interface EditorVideoView : BaseView {

    fun showVideo(videoMetadata: VideoMetadata)

    fun updateFrame(bitmap: Bitmap, position: Int)

    fun showCurrentTime(currentTimeText: String)

    fun seekTo(currentTime: Long)

    fun scrollTimeLineView(position: Int, offset: Int)

    fun showListFace(listFace: List<Face>)
}