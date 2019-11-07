package com.example.smartredact.common.widget.faceview

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by TuHA on 11/6/2019.
 */
class FaceView : RecyclerView {

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var faceAdapter: FaceAdapter

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun initView() {
        linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        layoutManager = linearLayoutManager

        faceAdapter = FaceAdapter(context, arrayListOf(), null)
        adapter = faceAdapter
    }

    fun setData(results: List<Face>) {
        faceAdapter.setData(results)
    }
}