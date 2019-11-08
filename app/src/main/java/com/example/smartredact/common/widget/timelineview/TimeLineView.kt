package com.example.smartredact.common.widget.timelineview

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartredact.data.model.VideoMetadata

class TimeLineView : RecyclerView {

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var frameAdapter: FrameAdapter

    private var enabledScrollToUpdate: Boolean = true

    private var scrollListener: TimeLineScrollListener? = null
    private var onProgressChanged: ((Float, Float) -> Unit)? = null
    private var onScrollStateChanged: ((Int) -> Unit)? = null

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

        frameAdapter = FrameAdapter(context, arrayListOf(), null)
        adapter = frameAdapter
    }

    fun setOnProgressChanged(listener: ((Float, Float) -> Unit)?) {
        this.onProgressChanged = listener
    }

    fun setOnScrollStateChanged(listener: ((Int) -> Unit)?) {
        this.onScrollStateChanged = listener
    }

    fun setData(frame: VideoMetadata.Frame) {
        frameAdapter.setData(frame)
        setupScrollListener(frame)
    }

    private fun setupScrollListener(frame: VideoMetadata.Frame) {
        if (scrollListener != null) {
            removeOnScrollListener(scrollListener!!)
            scrollListener = null
        }

        val padding = width / 2
        setPadding(padding, 0, padding, 0)

        val xPivot = width.toFloat() / 2
        val totalWidth = frame.width * frame.count
        scrollListener = TimeLineScrollListener(xPivot, frame.width, totalWidth, onProgressChanged, onScrollStateChanged)
        addOnScrollListener(scrollListener!!)
    }

    fun scrollToPositionWithOffset(position: Int, offset: Int) {
        linearLayoutManager.scrollToPositionWithOffset(position, offset)
    }

    fun setEnabledScrollToUpdate(isEnabled: Boolean) {
        enabledScrollToUpdate = isEnabled
        scrollListener?.setEnabled(isEnabled)
    }

    fun updateFrame(bitmap: Bitmap, position: Int) {
        frameAdapter.updateItem(bitmap, position)
    }
}