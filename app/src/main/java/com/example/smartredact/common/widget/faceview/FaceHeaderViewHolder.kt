package com.example.smartredact.common.widget.faceview

import android.view.View
import com.example.smartredact.R
import com.example.smartredact.common.facerecoginition.Classifier
import com.example.smartredact.common.utils.TimeUtils
import com.example.smartredact.common.widget.expandablerecyclerview.CommonHeaderViewHolder
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_header_face.*

/**
 * Created by TuHA on 11/7/2019.
 */
class FaceHeaderViewHolder(override val containerView: View,
                           interactor: FaceAdapter.Interactor?) : CommonHeaderViewHolder(containerView), LayoutContainer {

    fun bindData(face: Face) {
        tvCount.text = face.face.size.toString()
        bindTextTime(face.face)
    }

    private fun bindTextTime(face: ArrayList<Classifier.Recognition>) {
        val startTime = TimeUtils.format(face.first().time, false)
        val endTime = TimeUtils.format(face.last().time, false)

        tvTime.text = "$startTime - $endTime"
    }

    override fun onHeaderExpandedChanged(expanded: Boolean) {
        super.onHeaderExpandedChanged(expanded)

        if (expanded) {
            imvExpand.setImageResource(R.drawable.ic_chevron_up)
        } else {
            imvExpand.setImageResource(R.drawable.ic_chevron_down)
        }
    }
}