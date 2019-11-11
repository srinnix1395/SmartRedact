package com.example.smartredact.common.utils

import android.net.Uri

/**
 * Created by TuHA on 11/11/2019.
 */
object FileUtils {

    fun isImage(uri: Uri?): Boolean {
        requireNotNull(uri) { "Uri must not be null!" }
        return uri.toString().contains("image")
    }
}