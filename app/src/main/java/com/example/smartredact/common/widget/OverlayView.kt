package com.example.smartredact.common.widget

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import java.util.*

/**
 * Created by TuHA on 11/7/2019.
 */

/**
 * A simple View providing a render callback to other classes.
 */
class OverlayView : View {

    private val callbacks = LinkedList<DrawCallback>()

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun addCallback(callback: DrawCallback) {
        callbacks.add(callback)
    }

    @Synchronized
    override fun draw(canvas: Canvas) {
        for (callback in callbacks) {
            callback.drawCallback(canvas)
        }
    }

    /**
     * Interface defining the callback for client classes.
     */
    interface DrawCallback {
        fun drawCallback(canvas: Canvas)
    }
}
