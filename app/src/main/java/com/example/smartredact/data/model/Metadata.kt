package com.example.smartredact.data.model

import android.net.Uri

/**
 * Created by TuHA on 11/11/2019.
 */
abstract class Metadata {

    lateinit var data: Uri

    var orientation: Int = 90

    var width: Float = 0F

    var height: Float = 0F
}