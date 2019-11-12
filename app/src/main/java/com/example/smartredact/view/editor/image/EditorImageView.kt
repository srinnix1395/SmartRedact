package com.example.smartredact.view.editor.image

import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import com.example.smartredact.view.base.BaseView

interface EditorImageView : BaseView {

    fun getImageViewPhoto() : ImageView

    fun getActivityEditorImage() : FragmentActivity

}