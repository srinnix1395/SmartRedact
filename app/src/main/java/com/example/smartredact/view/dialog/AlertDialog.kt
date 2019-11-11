package com.example.smartredact.view.dialog

import android.content.Context
import android.content.DialogInterface
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.smartredact.R
import com.example.smartredact.common.extension.gone
import kotlinx.android.synthetic.main.dialog_notification_alert.*

/**
 * Created by TuHA on 2019-06-09.
 */
class AlertDialog() : BaseDialog() {

    private var icon: Int = 0
    private var title: String = ""
    private var message: String = ""
    private var autoDismiss: Boolean = false
    private var timeAutoDismiss: Long = 0L
    private var onDismissListener: (() -> Unit)? = null

    private constructor(icon: Int,
                        title: String,
                        message: String,
                        autoDismiss: Boolean,
                        timeAutoDismiss: Long,
                        onDismissListener: (() -> Unit)? = null) : this() {

        this.icon = icon
        this.title = title
        this.message = message
        this.autoDismiss = autoDismiss
        this.timeAutoDismiss = timeAutoDismiss
        this.onDismissListener = onDismissListener
    }

    override val dialogLayout: Int = R.layout.dialog_notification_alert

    override fun initView() {
        imv_icon.setImageResource(icon)
        if (title.isEmpty()) {
            tvTitle.gone()
        } else {
            tvTitle.text = title
        }
        tvMessage.text = message
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        onDismissListener?.invoke()
    }

    class Builder(val context: Context?) {
        private var icon: Int = 0
        private var title: String = ""
        private var message: String = ""
        private var autoDismiss: Boolean = false
        private var timeAutoDismiss: Long = 0L
        private var onClickDialogListener: (() -> Unit)? = null

        fun setIcon(@DrawableRes iconRes: Int): Builder {
            this.icon = iconRes
            return this@Builder
        }

        fun setMessage(message: String): Builder {
            this.message = message
            return this@Builder
        }

        fun setMessage(@StringRes message: Int): Builder {
            this.message = context?.getString(message).orEmpty()
            return this@Builder
        }

        fun setTitle(title: String): Builder {
            this.title = title
            return this@Builder
        }

        fun setTitle(@StringRes title: Int): Builder {
            this.title = context?.getString(title).orEmpty()
            return this@Builder
        }

        fun setAutoDismiss(autoDismiss: Boolean): Builder {
            this.autoDismiss = autoDismiss
            return this@Builder
        }

        fun setTimeAutoDismiss(timeAutoDismiss: Long): Builder {
            this.timeAutoDismiss = timeAutoDismiss
            return this@Builder
        }

        fun setOnDismissListener(onClickDialogListener: (() -> Unit)? = null): Builder {
            this.onClickDialogListener = onClickDialogListener
            return this@Builder
        }

        fun build(): AlertDialog {
            return AlertDialog(
                    icon,
                    title,
                    message,
                    autoDismiss,
                    timeAutoDismiss,
                    onClickDialogListener
            )
        }
    }
}