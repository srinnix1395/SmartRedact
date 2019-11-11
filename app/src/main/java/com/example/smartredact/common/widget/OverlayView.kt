package com.example.smartredact.common.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.smartredact.common.facerecoginition.Classifier
import com.example.smartredact.common.utils.DimensionUtils

/**
 * Created by TuHA on 11/7/2019.
 */

/**
 * A simple View providing a render callback to other classes.
 */
class OverlayView : View {

    private val listFace = arrayListOf<Classifier.Recognition>()

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    constructor(context: Context) : super(context) {
        initialize()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initialize()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initialize()
    }

    private fun initialize() {
        paint.style = Paint.Style.STROKE
        paint.color = Color.YELLOW
        paint.strokeWidth = DimensionUtils.dpToPx(2F)
    }

    override fun onDraw(canvas: Canvas?) {
        if (listFace.isEmpty()) {
            return
        }

        canvas?.drawRect(listFace[0].location, paint)
    }

    fun setData(listFace: ArrayList<Classifier.Recognition>) {
        this.listFace.clear()
        this.listFace.addAll(listFace)
        invalidate()
    }

}
