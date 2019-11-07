package com.example.smartredact.common.widget.faceview

import android.view.View
import com.example.smartredact.common.facerecoginition.Classifier
import com.example.smartredact.common.utils.TimeUtils
import com.example.smartredact.common.widget.expandablerecyclerview.CommonChildViewHolder
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_child_face.*

/**
 * Created by TuHA on 11/7/2019.
 */
class FaceChildViewHolder(override val containerView: View,
                          interactor: FaceAdapter.Interactor?) : CommonChildViewHolder(containerView), LayoutContainer {

    fun bindData(recognition: Classifier.Recognition) {
        tvTime.text = TimeUtils.format(recognition.time * 1000, false)
    }
}