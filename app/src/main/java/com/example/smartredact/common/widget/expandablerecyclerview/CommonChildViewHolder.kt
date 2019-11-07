package com.example.smartredact.common.widget.expandablerecyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by TuHA on 11/7/2019.
 */
open class CommonChildViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var mClickCallback: ((holder: CommonChildViewHolder, position: Int) -> Unit)? = null

    var headerIndex: Int = 0

    var childIndex: Int = 0

    init {
        itemView.setOnClickListener { mClickCallback?.invoke(this, adapterPosition) }
    }

    @Suppress("Don't use this method")
    fun setOnClickChildListener(cb: (holder: CommonChildViewHolder, position: Int) -> Unit): CommonChildViewHolder {
        mClickCallback = cb
        return this
    }

    @Suppress("Unused")
    open fun onChildClicked(headerIndex: Int, childIndex: Int) {

    }
}