package com.example.smartredact.common.widget.faceview

import android.view.View
import com.example.smartredact.R
import com.example.smartredact.common.utils.TimeUtils
import com.example.smartredact.common.widget.expandablerecyclerview.CommonHeaderViewHolder
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_header_face.*

/**
 * Created by TuHA on 11/7/2019.
 */
class ListFaceHeaderViewHolder(override val containerView: View,
                               interactor: ListFaceAdapter.Interactor?) : CommonHeaderViewHolder(containerView), LayoutContainer {

    fun bindData(face: Face) {
        tvCount.text = face.face.size.toString()
        bindTextTime(face.startTime, face.endTime)
    }

    private fun bindTextTime(startTime: Long, endTime: Long) {
        val startText = TimeUtils.format(startTime * 1000, false)
        val endText = TimeUtils.format(endTime * 1000, false)

        tvTime.text = "$startText - $endText"
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