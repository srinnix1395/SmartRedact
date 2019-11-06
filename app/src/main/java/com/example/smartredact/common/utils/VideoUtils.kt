package com.example.smartredact.common.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.Image
import android.media.MediaMetadataRetriever
import android.net.Uri
import com.example.smartredact.data.model.VideoMetadata
import kotlin.math.max
import kotlin.math.min

/**
 * Created by TuHA on 10/31/2019.
 */
object VideoUtils {

    fun extractMetadata(context: Context?, uri: Uri, dstHeight: Float): VideoMetadata {
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

    fun extractFrames(context: Context?, uri: Uri, frameCount: Int): ArrayList<Bitmap> {
        val retriever = MediaMetadataRetriever()
        val frames = arrayListOf<Bitmap>()

        retriever.setDataSource(context, uri)

        for (i in 0 until frameCount) {
            val bitmap = retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST)
            frames.add(bitmap)
        }

        return frames
    }

    fun convertImageToBitmap(image: Image, matrix: Matrix): Bitmap {
        val planes = image.planes

        val cachedYuvBytes = Array(3) {
            val buffer = planes[it].buffer
            val r = ByteArray(buffer.capacity())
            buffer.get(r)
            return@Array r
        }

        val output = convertYUV420ToARGB8888(
            cachedYuvBytes,
            image.width, image.height, planes[0].rowStride, planes[1].rowStride, planes[1].pixelStride
        )
        return Bitmap.createBitmap(
            Bitmap.createBitmap(output, image.width, image.height, Bitmap.Config.ARGB_8888),
            0, 0, image.width, image.height, matrix, true
        )
    }

    private fun convertYUV420ToARGB8888(yuvData: Array<ByteArray>, width: Int, height: Int, yRowStride: Int, uvRowStride: Int, uvPixelStride: Int): IntArray {
        val out = IntArray(height * width)
        var i = 0
        for (y in 0 until height) {
            val pY = yRowStride * y
            val uvRowStart = uvRowStride * (y shr 1)

            for (x in 0 until width) {
                val uvOffset = (x shr 1) * uvPixelStride

                val nY = max((yuvData[0][pY + x].toInt() and 0xff) - 16, 0) * 1192
                val nU = (yuvData[1][uvRowStart + uvOffset].toInt() and 0xff) - 128
                val nV = (yuvData[2][uvRowStart + uvOffset].toInt() and 0xff) - 128

                // This is the floating point equivalent. We do the conversion in integer
                // because some Android devices do not have floating point in hardware.
                // nR = (int)(1.164 * nY + 2.018 * nU);
                // nG = (int)(1.164 * nY - 0.813 * nV - 0.391 * nU);
                // nB = (int)(1.164 * nY + 1.596 * nV);

                val nR = min(262143, max(0, nY + 1634 * nV)) shr 10 and 0xff
                val nG = min(262143, max(0, nY - 833 * nV - 400 * nU)) shr 10 and 0xff
                val nB = min(262143, max(0, nY + 2066 * nU)) shr 10 and 0xff

                out[i++] =
                    (0xff000000 or ((nR shl 16).toLong()) or ((nG shl 8).toLong()) or nB.toLong()).toInt()
            }
        }
        return out
    }
}