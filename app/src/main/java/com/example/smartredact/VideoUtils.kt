package com.example.smartredact

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import wseemann.media.FFmpegMediaMetadataRetriever

/**
 * Created by TuHA on 10/31/2019.
 */
object VideoUtils {

    fun extractFrames(context: Context, uri: Uri): Array<Bitmap> {
        val retriever = FFmpegMediaMetadataRetriever()

        retriever.setDataSource(context, uri)

        return arrayOf()
    }
}