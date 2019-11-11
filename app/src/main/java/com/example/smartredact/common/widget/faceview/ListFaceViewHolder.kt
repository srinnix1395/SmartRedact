package com.example.smartredact.common.widget.faceview

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer

/**
 * Created by TuHA on 11/6/2019.
 */
class ListFaceViewHolder(override val containerView: View,
                         interactor: ListFaceAdapter.Interactor?) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    init {
        itemView.setOnClickListener {
            interactor?.onClickItemListener(adapterPosition)
        }
    }

    fun bindData(item: Any) {
        //todo
    }
}