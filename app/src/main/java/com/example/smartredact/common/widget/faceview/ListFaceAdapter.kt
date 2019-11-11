package com.example.smartredact.common.widget.faceview;

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.smartredact.R
import com.example.smartredact.common.widget.expandablerecyclerview.CommonExpandableAdapter

/**
 * Created by TuHA on 11/6/2019.
 */
class ListFaceAdapter(context: Context?,
                      private val listItems: ArrayList<Face>,
                      private val interactor: Interactor?) : CommonExpandableAdapter<ListFaceHeaderViewHolder, ListFaceChildViewHolder>(false, true) {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getHeaderViewHolder(parent: ViewGroup, headerType: Int): ListFaceHeaderViewHolder {
        val view = layoutInflater.inflate(R.layout.item_header_face, parent, false)
        return ListFaceHeaderViewHolder(view, interactor)
    }

    override fun getChildViewHolder(parent: ViewGroup, childType: Int): ListFaceChildViewHolder {
        val view = layoutInflater.inflate(R.layout.item_child_face, parent, false)
        return ListFaceChildViewHolder(view, interactor)
    }

    override fun getHeaderCount(): Int {
        return listItems.size
    }

    override fun getChildCount(header: Int): Int {
        return listItems[header].face.size
    }

    override fun onBindHeaderViewHolder(headerHolder: ListFaceHeaderViewHolder, headerIndex: Int, headerType: Int) {
        headerHolder.bindData(listItems[headerIndex])
    }

    override fun onBindChildViewHolder(childHolder: ListFaceChildViewHolder, headerIndex: Int, childIndex: Int, childType: Int) {
        childHolder.bindData(listItems[headerIndex].face[childIndex])
    }

    fun setData(listItems: List<Face>) {
        this.listItems.clear()
        this.listItems.addAll(listItems)
        notifyDataSetChanged()
    }

    fun addAll(index: Int = this.listItems.size, listItems: List<Face>) {
        this.listItems.addAll(index, listItems)
        notifyItemRangeInserted(index, listItems.size)
    }

    interface Interactor {

        fun onClickItemListener(position: Int)
    }
}