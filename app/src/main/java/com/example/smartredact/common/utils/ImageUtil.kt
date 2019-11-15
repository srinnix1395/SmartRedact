package com.example.smartredact.common.utils

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import com.example.smartredact.data.model.ImageMetadata
import com.example.smartredact.data.model.VideoMetadata

class ImageUtil(private val context: Context){
    fun extractMetadata(uri: Uri, frameHeight: Float): ImageMetadata {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(context, uri)
        val orientation = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION).toInt()
        val bitmap = retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST)
        val width = bitmap.width.toFloat()
        val height = bitmap.height.toFloat()

        val frameWidth = width * frameHeight / height
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, frameWidth.toInt(), frameHeight.toInt(), false)
        val frame = ImageMetadata.Frame(frameWidth, frameHeight, scaledBitmap)

        retriever.release()

        return ImageMetadata(uri, orientation, width, height, frame)
    }
}