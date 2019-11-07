package com.example.smartredact.common.widget.expandablerecyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by TuHA on 11/7/2019.
 */
open class CommonHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var mClickCallback: ((holder: CommonHeaderViewHolder, position: Int) -> Unit)? = null

    var headerIndex: Int = 0

    var isExpanding: Boolean = false

    init {
        itemView.setOnClickListener { mClickCallback?.invoke(this, adapterPosition) }
    }

    @Suppress("Don't use this method")
    fun setOnClickHeaderListener(cb: (holder: CommonHeaderViewHolder, position: Int) -> Unit): CommonHeaderViewHolder {
        mClickCallback = cb
        return this
    }

    open fun onHeaderExpandedChanged(expanded: Boolean) {

    }

    open fun onHeaderClicked() {

    }
}
