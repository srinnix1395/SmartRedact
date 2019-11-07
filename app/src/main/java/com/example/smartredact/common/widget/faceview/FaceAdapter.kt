package com.example.smartredact.common.widget.faceview;

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.smartredact.R
import com.example.smartredact.common.widget.expandablerecyclerview.CommonExpandableAdapter

/**
 * Created by TuHA on 11/6/2019.
 */
class FaceAdapter(context: Context?,
                  private val listItems: ArrayList<Face>,
                  private val interactor: Interactor?) : CommonExpandableAdapter<FaceHeaderViewHolder, FaceChildViewHolder>(false, true) {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getHeaderViewHolder(parent: ViewGroup, headerType: Int): FaceHeaderViewHolder {
        val view = layoutInflater.inflate(R.layout.item_header_face, parent, false)
        return FaceHeaderViewHolder(view, interactor)
    }

    override fun getChildViewHolder(parent: ViewGroup, childType: Int): FaceChildViewHolder {
        val view = layoutInflater.inflate(R.layout.item_child_face, parent, false)
        return FaceChildViewHolder(view, interactor)
    }

    override fun getHeaderCount(): Int {
        return listItems.size
    }

    override fun getChildCount(header: Int): Int {
        return listItems[header].face.size
    }

    override fun onBindHeaderViewHolder(headerHolder: FaceHeaderViewHolder, headerIndex: Int, headerType: Int) {
        headerHolder.bindData(listItems[headerIndex])
    }

    override fun onBindChildViewHolder(childHolder: FaceChildViewHolder, headerIndex: Int, childIndex: Int, childType: Int) {
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