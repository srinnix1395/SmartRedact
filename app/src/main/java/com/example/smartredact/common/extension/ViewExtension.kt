package com.example.smartredact.common.extension

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.text.Editable
import android.text.TextWatcher
import android.view.TouchDelegate
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.StringRes
import com.bumptech.glide.Glide
import com.example.smartredact.common.utils.DimensionUtils

/**
 * The method to expand click area of view
 * unit: dp
 */
fun View.expandClickArea(left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0) {
    if (left == 0 && top == 0 && right == 0 && bottom == 0) {
        return
    }

    (this.parent as? View)?.post {
        val rect = Rect()
        this.getHitRect(rect)
        rect.left -= DimensionUtils.dpToPx(left.toFloat()).toInt()
        rect.top -= DimensionUtils.dpToPx(top.toFloat()).toInt()
        rect.right += DimensionUtils.dpToPx(right.toFloat()).toInt()
        rect.bottom += DimensionUtils.dpToPx(bottom.toFloat()).toInt()

        val touchDelegate = TouchDelegate(rect, this)
        if (View::class.java.isInstance(this.parent)) {
            (this.parent as View).touchDelegate = touchDelegate
        }
    }
}

fun View.showKeyBoard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun View.hideKeyBoard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun TextView.setTextNullable(text: String?) {
    this.text = text.orEmpty()
}

fun TextView.setText(@StringRes id: Int?, text: String) {
    if (id == null) {
        this.text = text
    } else {
        this.setText(id)
    }
}

fun View.hideKeyBoardWhenTouchOutside(activity: Activity) {
    if (this !is EditText) {
        this.setOnTouchListener({ _, _ ->
            hideKeyBoard()
            false
        })
    }
    if (this is ViewGroup) {
        for (i in 0 until this.childCount) {
            this.getChildAt(i).hideKeyBoardWhenTouchOutside(activity)
        }
    }
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
    })
}

fun ImageView.loadUrl(url: String) {
    Glide.with(context).load(url).into(this);
}

fun View.isVisible(): Boolean {
    return visibility == View.VISIBLE
}

fun View.isInvisible(): Boolean {
    return visibility == View.INVISIBLE
}

fun View.isGone(): Boolean {
    return visibility == View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}