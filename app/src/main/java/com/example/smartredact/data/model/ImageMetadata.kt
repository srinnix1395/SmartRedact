package com.example.smartredact.data.model

import android.graphics.Bitmap
import android.net.Uri

/**
 * Created by TuHA on 11/11/2019.
 */
class ImageMetadata() : Metadata() {

    lateinit var frame: Frame

    constructor(data: Uri, orientation: Int, width: Float, height: Float, frame: Frame) : this() {
        this.data = data
        this.orientation = orientation
        this.width = width
        this.height = height
        this.frame = frame
    }

    data class Frame(var width: Float,
                     var height: Float,
                     var bitmap: Bitmap
    )
}