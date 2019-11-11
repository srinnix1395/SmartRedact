package com.example.smartredact.data.model

import android.graphics.Bitmap
import android.net.Uri

class VideoMetadata() : Metadata() {

    var duration: Long = 0L

    lateinit var frame: Frame

    constructor(data: Uri, duration: Long, orientation: Int, width: Float, height: Float, frame: Frame) : this() {
        this.data = data
        this.duration = duration
        this.orientation = orientation
        this.width = width
        this.height = height
        this.frame = frame
    }

    data class Frame(var width: Float,
                     var height: Float,
                     var count: Int,
                     var interval: Float,
                     var firstItem: Bitmap)
}
