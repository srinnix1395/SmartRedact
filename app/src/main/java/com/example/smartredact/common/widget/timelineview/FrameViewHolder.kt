package com.example.smartredact.common.widget.timelineview

import android.graphics.Bitmap
import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_frame.*

open class FrameViewHolder(override val containerView: View,
                           interactor: FrameAdapter.Interactor?) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    companion object {

        const val DEFAULT_RADIUS = 15F
    }

    init {
        itemView.setOnClickListener {
            interactor?.onClickItemListener(adapterPosition)
        }
    }

    fun bindData(bitmap: Bitmap) {
        Glide
                .with(containerView)
                .load(bitmap)
                .into(imvFrame)
    }

    class FirstItem(containerView: View,
                    interactor: FrameAdapter.Interactor?) : FrameViewHolder(containerView, interactor) {

        init {
            imvFrame.outlineProvider = object : ViewOutlineProvider() {
                override fun getOutline(view: View?, outline: Outline?) {
                    outline?.setRoundRect(0, 0, (view!!.width + DEFAULT_RADIUS).toInt(), view.height, DEFAULT_RADIUS)
                }
            }
            imvFrame.clipToOutline = true
        }
    }

    class LastItem(containerView: View,
                   interactor: FrameAdapter.Interactor?) : FrameViewHolder(containerView, interactor) {

        init {
            imvFrame.outlineProvider = object : ViewOutlineProvider() {
                override fun getOutline(view: View?, outline: Outline?) {
                    outline?.setRoundRect((0 - DEFAULT_RADIUS).toInt(), 0, view!!.width, view.height, DEFAULT_RADIUS)
                }
            }
            imvFrame.clipToOutline = true
        }
    }
}