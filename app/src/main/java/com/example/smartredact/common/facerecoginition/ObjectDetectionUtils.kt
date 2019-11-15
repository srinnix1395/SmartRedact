package com.example.smartredact.common.facerecoginition

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.RectF
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import androidx.collection.ArrayMap
import com.example.smartredact.common.constants.Constants
import com.example.smartredact.common.utils.ImageUtils
import com.example.smartredact.common.widget.faceview.Face
import java.io.IOException
import android.provider.MediaStore



/**
 * Created by TuHA on 11/11/2019.
 */
class ObjectDetectionUtils(private val context: Context) {

    private var detector: Classifier? = null
    private var croppedBitmap: Bitmap? = null
    private var fullToCropMatrix: Matrix? = null
    private var cropToFullMatrix: Matrix? = null

    init {
        try {
            detector = TensorFlowYoloDetector.create(
                context.assets,
                Constants.FaceDetection.YOLO_MODEL_FILE,
                Constants.FaceDetection.YOLO_INPUT_SIZE,
                Constants.FaceDetection.YOLO_BLOCK_SIZE
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val cropSize = Constants.FaceDetection.YOLO_INPUT_SIZE
        croppedBitmap = Bitmap.createBitmap(cropSize, cropSize, Bitmap.Config.ARGB_8888)
    }

    fun startSession(previewWidth: Int, previewHeight: Int, orientation: Int) {
        val cropSize = Constants.FaceDetection.YOLO_INPUT_SIZE
        fullToCropMatrix = ImageUtils.getTransformationMatrix(
            previewWidth, previewHeight,
            cropSize, cropSize,
            orientation, true
        )

        cropToFullMatrix = Matrix()
        fullToCropMatrix?.invert(cropToFullMatrix)
    }

    fun detectFacesVideo(
        uri: Uri,
        duration: Long,
        srcWidth: Float,
        srcHeight: Float,
        renderedWidth: Float,
        renderedHeight: Float,
        paddingHorizontal: Float,
        paddingVertical: Float
    ): List<Face> {
        val retriever = MediaMetadataRetriever().apply {
            setDataSource(context, uri)
        }
        val frameCount = duration / 1000
        val mappedRecognitions = ArrayMap<String, ArrayList<Classifier.Recognition>>()

        for (i in 0 until frameCount) {
            val bitmap =
                retriever.getFrameAtTime(i * 1000000, MediaMetadataRetriever.OPTION_CLOSEST)
            Canvas(croppedBitmap!!).apply {
                drawBitmap(bitmap, fullToCropMatrix!!, null)
            }
            bitmap.recycle()

            detector?.recognizeImage(croppedBitmap)?.let { listRecognitions ->
                listRecognitions.forEach {
                    val location = it.location
                    if (location != null && it.confidence > 0.5) {
                        it.location = mapRectToActualSize(
                            location,
                            srcWidth,
                            srcHeight,
                            renderedWidth,
                            renderedHeight,
                            paddingHorizontal,
                            paddingVertical
                        )

                        it.startTime = i
                        it.endTime = i + 1

                        val list = mappedRecognitions[it.id]
                        if (list == null) {
                            mappedRecognitions[it.id] = arrayListOf(it)
                        } else {
                            list.add(it)
                        }
                    }
                }
            }
        }

        retriever.release()

        mappedRecognitions.values.forEach { value ->
            value?.sortBy { it.startTime }
        }

        return mappedRecognitions.map { map ->
            val firstFace = map.value.first()
            val lastFace = map.value.last()

            Face(map.key, firstFace.startTime, lastFace.startTime, map.value)
        }
    }

    fun detectFacesImage(
        uri: Uri,
        srcWidth: Float,
        srcHeight: Float,
        renderedWidth: Float,
        renderedHeight: Float,
        paddingHorizontal: Float,
        paddingVertical: Float
    ): List<Face> {
        val mappedRecognitions = ArrayMap<String, ArrayList<Classifier.Recognition>>()
        val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        Canvas(croppedBitmap!!).apply {
            drawBitmap(bitmap, fullToCropMatrix!!, null)
        }
        bitmap.recycle()

        detector?.recognizeImage(croppedBitmap)?.let { listRecognitions ->
            listRecognitions.forEach {
                val location = it.location
                if (location != null && it.confidence > 0.5) {
                    it.location = mapRectToActualSize(
                        location,
                        srcWidth,
                        srcHeight,
                        renderedWidth,
                        renderedHeight,
                        paddingHorizontal,
                        paddingVertical
                    )

                    it.startTime = 0
                    it.endTime = 0

                    val list = mappedRecognitions[it.id]
                    if (list == null) {
                        mappedRecognitions[it.id] = arrayListOf(it)
                    } else {
                        list.add(it)
                    }
                }
            }
        }

        mappedRecognitions.values.forEach { value ->
            value?.sortBy { it.startTime }
        }

        return mappedRecognitions.map { map ->
            val firstFace = map.value.first()
            val lastFace = map.value.last()

            Face(map.key, firstFace.startTime, lastFace.startTime, map.value)
        }
    }

    private fun mapRectToActualSize(
        location: RectF,
        srcWidth: Float,
        srcHeight: Float,
        renderedWidth: Float,
        renderedHeight: Float,
        paddingHorizontal: Float,
        paddingVertical: Float
    ): RectF {
        cropToFullMatrix?.mapRect(location)
         val mapLeft = location.left * renderedWidth / srcWidth
        val mapRight = mapLeft + location.width()
        val mapTop = location.top * renderedHeight / srcHeight
        val mapBottom = mapTop + location.height()

        location.apply {
            left = mapLeft + paddingHorizontal
            right = mapRight + paddingHorizontal
            top = mapTop + paddingVertical
            bottom = mapBottom + paddingVertical
        }

        return location
    }
}