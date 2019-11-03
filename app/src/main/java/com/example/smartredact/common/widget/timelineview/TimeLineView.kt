package com.example.smartredact.common.widget.timelineview

import android.content.Context
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

    fun setData(frame: VideoMetadata.Frame) {
        frameAdapter.setData(frame.frames)
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
        val totalWidth = frame.width * frame.frames.size
        scrollListener = TimeLineScrollListener(xPivot, frame.width, totalWidth, onProgressChanged)
        addOnScrollListener(scrollListener!!)
    }

    fun scrollToPositionWithOffset(position: Int, offset: Int) {
        linearLayoutManager.scrollToPositionWithOffset(position, offset)
    }

    fun setEnabledScrollToUpdate(isEnabled: Boolean) {
        enabledScrollToUpdate = isEnabled
        scrollListener?.setEnabled(isEnabled)
    }
}