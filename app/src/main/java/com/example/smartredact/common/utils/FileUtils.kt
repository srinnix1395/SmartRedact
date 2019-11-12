package com.example.smartredact.common.utils

import android.content.Context
import android.net.Uri
import android.os.Environment
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by TuHA on 11/11/2019.
 */
object FileUtils {

    fun isImage(uri: Uri?): Boolean {
        requireNotNull(uri) { "Uri must not be null!" }
        return uri.toString().contains("image")
    }

    @Throws(IOException::class)
    fun createImageFile(context: Context?): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        )
    }
}