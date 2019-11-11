package com.example.smartredact.common.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.Image
import android.media.MediaMetadataRetriever
import android.net.Uri
import com.example.smartredact.data.model.VideoMetadata
import io.reactivex.Observable
import io.reactivex.disposables.Disposables
import kotlin.math.max
import kotlin.math.min

/**
 * Created by TuHA on 10/31/2019.
 */
class VideoUtils(private val context: Context) {

    companion object {
        const val MINUTE = 60 * 1000
    }

    fun extractMetadata(uri: Uri, frameHeight: Float): VideoMetadata {
        val retriever = MediaMetadataRetriever()

        retriever.setDataSource(context, uri)

        val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION).toLong()
        val orientation = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION).toInt()
        val frameCount = getFrameCount(duration)
        val interval = duration.toFloat() / frameCount

        val bitmap = retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST)
        val width = bitmap.width.toFloat()
        val height = bitmap.height.toFloat()

        val frameWidth = width * frameHeight / height
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, frameWidth.toInt(), frameHeight.toInt(), false)
        val frame = VideoMetadata.Frame(frameWidth, frameHeight, frameCount, interval, scaledBitmap)

        retriever.release()

        return VideoMetadata(uri, duration, orientation, width, height, frame)
    }

    private fun getFrameCount(duration: Long): Int {
        return when {
            duration <= 1.5 * MINUTE -> (duration / 1000).toInt()
            duration <= 4 * MINUTE -> (duration / 2000).toInt()
            duration <= 6 * MINUTE -> (duration / 3000).toInt()
            duration <= 8 * MINUTE -> (duration / 4000).toInt()
            duration <= 10 * MINUTE -> (duration / 5000).toInt()
            else -> (duration / 6000).toInt()
        }
    }

    fun extractFrames(uri: Uri, frameCount: Int, interval: Float, dstWidth: Int, dstHeight: Int): Observable<Pair<Bitmap, Int>> {
        return Observable.create<Pair<Bitmap, Int>> { emitter ->
            val retriever = MediaMetadataRetriever()
            var isRelease = false

            emitter.setDisposable(Disposables.fromAction {
                retriever.release()
                isRelease = true
            })

            retriever.setDataSource(context, uri)
            for (i in 1 until frameCount) {
                if (isRelease) {
                    return@create
                }
                val bitmap = retriever.getFrameAtTime((i * interval * 1000).toLong(), MediaMetadataRetriever.OPTION_CLOSEST)
                val scaledBitmap = Bitmap.createScaledBitmap(bitmap, dstWidth, dstHeight, false)
                bitmap.recycle()
                emitter.onNext(Pair(scaledBitmap, i))
            }
            retriever.release()

            emitter.onComplete()
        }
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
                image.width,
                image.height,
                planes[0].rowStride,
                planes[1].rowStride,
                planes[1].pixelStride
        )
        return Bitmap.createBitmap(
                Bitmap.createBitmap(output, image.width, image.height, Bitmap.Config.ARGB_8888),
                0, 0, image.width, image.height, matrix, true
        )
    }

    private fun convertYUV420ToARGB8888(
            yuvData: Array<ByteArray>,
            width: Int,
            height: Int,
            yRowStride: Int,
            uvRowStride: Int,
            uvPixelStride: Int
    ): IntArray {
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