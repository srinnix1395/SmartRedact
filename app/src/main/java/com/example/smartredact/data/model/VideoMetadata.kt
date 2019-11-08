package com.example.smartredact.data.model

import android.graphics.Bitmap
import android.net.Uri

data class VideoMetadata(var uri: Uri,
                         var duration: Long,
                         var width: Float,
                         var height: Float,
                         var frame: Frame) {

    data class Frame(var width: Float,
                     var height: Float,
                     var count: Int,
                     var interval: Float,
                     var firstItem: Bitmap)
}
