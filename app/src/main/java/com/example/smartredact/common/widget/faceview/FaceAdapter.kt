package com.example.smartredact.common.widget.faceview;

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smartredact.R

/**
 * Created by TuHA on 11/6/2019.
 */
class FaceAdapter(context: Context?,
                  private val listItems: ArrayList<Any>,
                  private val interactor: Interactor?) : RecyclerView.Adapter<FaceViewHolder>() {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaceViewHolder {
        val view: View = layoutInflater.inflate(R.layout.item_face, parent, false)
        return FaceViewHolder(view, interactor)
    }

    override fun onBindViewHolder(holder: FaceViewHolder, position: Int) {
        holder.bindData(listItems[position])
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    fun setData(listItems: List<Any>) {
        this.listItems.clear()
        this.listItems.addAll(listItems)
        notifyDataSetChanged()
    }

    fun addAll(index: Int = this.listItems.size, listItems: List<Any>) {
        this.listItems.addAll(index, listItems)
        notifyItemRangeInserted(index, listItems.size)
    }

    interface Interactor {

        fun onClickItemListener(position: Int)
    }
}