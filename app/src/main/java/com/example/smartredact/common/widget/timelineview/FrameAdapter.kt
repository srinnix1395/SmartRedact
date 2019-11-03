package com.example.smartredact.common.widget.timelineview

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smartredact.R
import java.util.*

class FrameAdapter(context: Context?,
                   private val listItems: ArrayList<Bitmap>,
                   private val interactor: Interactor?) : RecyclerView.Adapter<FrameViewHolder>() {

    companion object {
        const val FIRST_ITEM = 1
        const val NORMAL_ITEM = 2
        const val LAST_ITEM = 3
    }

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FrameViewHolder {
        val view: View = layoutInflater.inflate(R.layout.item_frame, parent, false)

        return when (viewType) {
            FIRST_ITEM -> FrameViewHolder.FirstItem(view, interactor)
            LAST_ITEM -> FrameViewHolder.LastItem(view, interactor)
            else -> FrameViewHolder(view, interactor)
        }
    }

    override fun onBindViewHolder(holder: FrameViewHolder, position: Int) {
        holder.bindData(listItems[position])
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> FIRST_ITEM
            listItems.lastIndex -> LAST_ITEM
            else -> NORMAL_ITEM
        }
    }

    fun setData(listItems: List<Bitmap>) {
        this.listItems.clear()
        this.listItems.addAll(listItems)
        notifyDataSetChanged()
    }

    fun addAll(index: Int = this.listItems.size, listItems: List<Bitmap>) {
        this.listItems.addAll(index, listItems)
        notifyItemRangeInserted(index, listItems.size)
    }

    interface Interactor {

        fun onClickItemListener(position: Int)
    }
}