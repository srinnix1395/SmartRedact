package com.example.smartredact.view.editor.image

import android.net.Uri
import com.example.smartredact.view.base.BaseView

interface EditorImageView : BaseView {

    fun setImageViewPhoto(uri: Uri)

}