package com.example.smartredact.view.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.smartredact.SmartRedactApplication
import com.example.smartredact.common.di.component.ActivityComponent
import com.example.smartredact.view.dialog.AlertDialog
import com.example.smartredact.view.dialog.ProgressCommonDialog
import timber.log.Timber

/**
 * Created by TuHA on 6/6/2019.
 */
abstract class BaseFragment : Fragment(), BaseView {

    val application: SmartRedactApplication
        get() {
            return activity?.application as SmartRedactApplication
        }

    val parentActivity: BaseActivity
        get() {
            return activity as BaseActivity
        }

    private val progressDialog: ProgressCommonDialog by lazy {
        return@lazy ProgressCommonDialog(context)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject(parentActivity.activityComponent)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
    }

    open fun initView() {

    }

    open fun initData() {

    }

    open fun onBackPressed(): Boolean {
        return false
    }

    private fun showProgress() {
        if (!progressDialog.isShowing) progressDialog.show()
    }

    private fun dismissProgress() {
        if (progressDialog.isShowing) progressDialog.dismiss()
    }

    //region implement base view
    override fun showProgressDialog(isShow: Boolean) {
        if (isShow) {
            showProgress()
        } else {
            dismissProgress()
        }
    }

    override fun handleError(error: Throwable) {
        Timber.e(error)
    }

    protected fun showSuccessDialog(message: String, onDismissDialogListener: (() -> Unit)? = null) {
        AlertDialog.Builder(context)
                .setMessage(message)
                .setAutoDismiss(true)
                .setTimeAutoDismiss(1000L)
                .setOnDismissListener(onDismissDialogListener)
                .build()
                .show(this.fragmentManager!!)
    }

    override fun showErrorDialog(title: String, message: String, icon: Int, onDismissDialogListener: (() -> Unit)?) {
        AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setIcon(icon)
                .setOnDismissListener(onDismissDialogListener)
                .build()
                .show(this.fragmentManager!!)
    }

    override fun showErrorDialog(title: Int, message: Int, icon: Int, onDismissDialogListener: (() -> Unit)?) {
        AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setIcon(icon)
                .setOnDismissListener(onDismissDialogListener)
                .build()
                .show(this.fragmentManager!!)
    }
    //endregion implement base view

    abstract fun inject(activityComponent: ActivityComponent)

    abstract fun getLayoutId(): Int
}