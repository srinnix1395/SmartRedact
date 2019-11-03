package com.example.smartredact.common.utils

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import com.example.smartredact.data.model.VideoMetadata

/**
 * Created by TuHA on 10/31/2019.
 */
object VideoUtils {

    fun extractFrames(context: Context?, uri: Uri, dstHeight: Float): VideoMetadata {
        val retriever = MediaMetadataRetriever()

        retriever.setDataSource(context, uri)

        val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION).toLong()
        val width = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH).toFloat()
        val height = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT).toFloat()

        val frames = ArrayList<Bitmap>()
        val frameCountNeed = (duration / 1000)
        val dstWidth = width * dstHeight / height
        for (i in 0 until frameCountNeed) {
            val bitmap = retriever.getScaledFrameAtTime(i * 1000000L, MediaMetadataRetriever.OPTION_CLOSEST_SYNC, dstWidth.toInt(), dstHeight.toInt())
            frames.add(bitmap)
        }
        val frame = VideoMetadata.Frame(dstWidth, dstHeight, frames)

        return VideoMetadata(uri, duration, width, height, frame)
    }
}