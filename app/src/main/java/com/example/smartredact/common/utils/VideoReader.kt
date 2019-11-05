package com.example.smartredact.common.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.*
import android.net.Uri
import java.util.*


class VideoReader(context: Context,
                  mFileUri: Uri,
                  private var mBufferSize: Int = 5) {


    companion object {
        internal const val TAG = "VideoDecoder"
        internal const val VIDEO_MIME_PREFIX = "video/"
    }

    private var mMediaExtractor: MediaExtractor? = null
    private var mMediaCodec: MediaCodec? = null
    private var mVideoTrackIndex = -1
    private var mTransformMatrix = Matrix()
    private val mFrameBuffer = Collections.synchronizedList(ArrayList<Bitmap>())
    private var mStopFlag = false

    init {
        mMediaExtractor = MediaExtractor()
        mMediaExtractor!!.setDataSource(context, mFileUri, null)

        val trackCount = mMediaExtractor!!.trackCount
        for (i in 0 until trackCount) {
            val mf = mMediaExtractor!!.getTrackFormat(i)
            val mime = mf.getString(MediaFormat.KEY_MIME)
            if (mime!!.startsWith(VIDEO_MIME_PREFIX)) {
                mVideoTrackIndex = i
                break
            }
        }
        check(mVideoTrackIndex >= 0) { "Media file don't have video track" }
        mMediaExtractor!!.selectTrack(mVideoTrackIndex)

        val mf = mMediaExtractor!!.getTrackFormat(mVideoTrackIndex)
        val codec = MediaCodecList(MediaCodecList.ALL_CODECS).findDecoderForFormat(mf)
        mMediaCodec = MediaCodec.createByCodecName(codec)

        mMediaCodec!!.configure(mf, null, null, 0)

        val m = MediaMetadataRetriever()
        m.setDataSource(context, mFileUri)
        val orientation =
            m.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION).toFloat()
        mTransformMatrix.postRotate(orientation)
        m.release()
        mMediaCodec!!.start()
    }

    fun extractFrames() {
//        mFrameBuffer.clear()
//        mStopFlag = true
//
//        mMediaExtractor!!.seekTo(startTimeUs, MediaExtractor.SEEK_TO_CLOSEST_SYNC)
//        val info = BufferInfo()
//        while (mStopFlag) {
//            if (mFrameBuffer.size >= mBufferSize) {
//                continue
//            }
//            val inputIndex = mMediaCodec!!.dequeueInputBuffer(10000)
//            if (inputIndex >= 0) {
//                val inputBuffer = mMediaCodec!!.getInputBuffer(inputIndex)!!
//                val sampleSize = mMediaExtractor!!.readSampleData(inputBuffer, 0)
//                if (sampleSize > 0) {
//                    mMediaCodec!!.queueInputBuffer(
//                        inputIndex,
//                        0,
//                        sampleSize,
//                        mMediaExtractor!!.sampleTime,
//                        0
//                    )
//                    mMediaExtractor!!.advance()
//                } else {
//                    mMediaCodec!!.queueInputBuffer(
//                        inputIndex,
//                        0,
//                        0,
//                        0,
//                        MediaCodec.BUFFER_FLAG_END_OF_STREAM
//                    )
//                }
//            }
//            val outputIndex = mMediaCodec!!.dequeueOutputBuffer(info, 10000)
//            if (outputIndex >= 0) {
//                if (info.presentationTimeUs > startTimeUs) {
//                    val frameImage = mMediaCodec!!.getOutputImage(outputIndex)
//                    if (frameImage != null) {
//                        val frame = VideoUtils.convertImageToBitmap(frameImage, mTransformMatrix)
//                        mFrameBuffer.add(frame)
//                    }
//                }
//                mMediaCodec!!.releaseOutputBuffer(outputIndex, false)
//            }
//
//            if (info.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM != 0) {
//                break
//            }
//        }
//        mMediaCodec!!.flush()
    }

    fun stop() {
        mStopFlag = false
    }

    fun release() {
        stop()
        mMediaCodec?.stop()
        mMediaCodec?.release()
        mMediaExtractor?.release()
    }
}