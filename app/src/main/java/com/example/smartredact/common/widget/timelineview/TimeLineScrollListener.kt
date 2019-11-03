package com.example.smartredact.common.widget.timelineview

import androidx.recyclerview.widget.RecyclerView

class TimeLineScrollListener(private val xPivot: Float,
                             private val frameWidth: Float,
                             private val total: Float,
                             private var onProgressChanged: ((Float, Float) -> Unit)?) : RecyclerView.OnScrollListener() {

    private var isEnabled = true

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (!isEnabled) {
            return
        }

        val view = recyclerView.findChildViewUnder(xPivot, 0F) ?: return
        val viewHolder = recyclerView.findContainingViewHolder(view) ?: return
        val currentPosition = viewHolder.adapterPosition
        val progress = currentPosition * frameWidth + Math.abs(view.x - xPivot)

        onProgressChanged?.invoke(progress, total)
    }

    fun setEnabled(enabled: Boolean) {
        isEnabled = enabled
    }
}