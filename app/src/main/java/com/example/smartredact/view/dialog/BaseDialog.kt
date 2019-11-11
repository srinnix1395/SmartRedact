package com.example.smartredact.view.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.smartredact.R
import com.example.smartredact.common.utils.DimensionUtils

/**
 * Created by TuHA on 6/6/2019
 */
abstract class BaseDialog : DialogFragment() {

    protected var dialogListener: DialogListener? = null
    private var isSetSizeDialog = false

    internal abstract val dialogLayout: Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(dialogLayout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onResume() {
        if (!isSetSizeDialog) {
            isSetSizeDialog = true
            setSizeDialog()
            setBackgroundDialog()
        }
        super.onResume()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        if (dialogListener != null) {
            dialogListener!!.onDismissListener()
        }
    }

    private fun setSizeDialog() {
        val window = dialog?.window

        if (window == null || context == null) {
            return
        }

        val expectedWidth = DimensionUtils.getScreenWidth() - DimensionUtils.dpToPx(60F)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(window.attributes)
        lp.width = expectedWidth.toInt()
        window.attributes = lp
    }

    private fun setBackgroundDialog() {
        val window = dialog?.window
        if (window == null || context == null) {
            return
        }
        window.setBackgroundDrawable(ContextCompat.getDrawable(context!!, R.drawable.background_common_dialog))
    }

    open fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, javaClass.name)
    }

    open protected fun initView() {

    }

    fun setOnDialogDismissListener(dialogListener: DialogListener) {
        this.dialogListener = dialogListener
    }

    interface DialogListener {

        fun onDismissListener()
    }
}
