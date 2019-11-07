package com.example.smartredact.common.widget.faceview

import com.example.smartredact.common.facerecoginition.Classifier

/**
 * Created by TuHA on 11/7/2019.
 */
data class Face (var id: String,
                 var startTime: Long,
                 var endTime: Long,
                 var face: ArrayList<Classifier.Recognition>)