package com.example.smartredact.view.dialog

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import com.example.smartredact.R

class ProgressCommonDialog(context: Context?) : ProgressDialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_common_progress)
        setCancelable(false)
    }
}