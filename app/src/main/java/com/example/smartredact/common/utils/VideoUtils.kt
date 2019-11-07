package com.example.smartredact.common.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.core.graphics.scale
import com.example.smartredact.data.model.VideoMetadata
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt
import kotlin.math.roundToLong

/**
 * Created by TuHA on 10/31/2019.
 */
object VideoUtils {

    fun extractFrames(context: Context?, uri: Uri, dstHeight: Float): VideoMetadata {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(context, uri)

        val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION).toLong()

        val frames = ArrayList<Bitmap>()
        val frameCountNeed = (duration / 1000)
        val interval = duration.toFloat()  / frameCountNeed

        for (i in 0 until frameCountNeed) {
            val realBitmap = retriever.getFrameAtTime((i * interval * 1000).toLong(), MediaMetadataRetriever.OPTION_CLOSEST)
            frames.add(realBitmap)
        }

        val width = frames[0].width.toFloat()
        val height = frames[0].height.toFloat()
        val dstWidth = width * dstHeight / height
        val frame = VideoMetadata.Frame(dstWidth, dstHeight, frames)

        return VideoMetadata(uri, duration, width, height, frame)
    }
}