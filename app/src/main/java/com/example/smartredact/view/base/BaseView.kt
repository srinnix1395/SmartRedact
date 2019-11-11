package com.example.smartredact.view.base

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/**
 * Created by TuHA on 06/06/2019.
 */
interface BaseView {

    fun showProgressDialog(isShow: Boolean)

    fun handleError(error: Throwable)

    fun showErrorDialog(title: String, message: String, @DrawableRes icon: Int, onDismissDialogListener: (() -> Unit)? = null)

    fun showErrorDialog(@StringRes title: Int, @StringRes message: Int, @DrawableRes icon: Int, onDismissDialogListener: (() -> Unit)? = null)
}