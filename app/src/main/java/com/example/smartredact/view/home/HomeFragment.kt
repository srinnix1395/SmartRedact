package com.example.smartredact.view.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.smartredact.R
import com.example.smartredact.common.constants.Constants
import com.example.smartredact.common.di.component.ActivityComponent
import com.example.smartredact.common.extension.replaceFragment
import com.example.smartredact.common.utils.FileUtils
import com.example.smartredact.data.model.Session
import com.example.smartredact.view.base.BaseFragment
import com.example.smartredact.view.editor.image.EditorImageFragment
import com.example.smartredact.view.editor.video.EditorVideoFragment
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment() {

    companion object {
        const val REQUEST_CODE_SELECT_FILE = 1234
    }

    override fun inject(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun initView() {
        btnOpenLastEditSession.setOnClickListener {
            Toast.makeText(
                this.context,
                this.context!!.getString(R.string.open_last_edit_session),
                Toast.LENGTH_SHORT
            ).show()
        }
        btnAllPreviousSessions.setOnClickListener {
            openLastSession()
        }
        btnRecordANewVideo.setOnClickListener {
            recordVideo()
        }
        btnTakeAPicture.setOnClickListener {
            takePicture()
        }
        btnLoadMediaFromLocal.setOnClickListener {
            chooseMedias()
        }
    }

    private fun openLastSession() {

    }

    private fun takePicture() {
        //todo KienNT
    }

    private fun recordVideo() {
        //todo KienNT
    }

    private fun chooseMedias() {
        Intent(Intent.ACTION_OPEN_DOCUMENT)
            .apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "image/*"
                putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/*", "video/*"))
                putExtra(Intent.EXTRA_LOCAL_ONLY, true)
            }
            .let {
                startActivityForResult(Intent.createChooser(it, "Choose a file"), REQUEST_CODE_SELECT_FILE)
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data == null || resultCode != Activity.RESULT_OK) {
            return
        }

        when (requestCode) {
            REQUEST_CODE_SELECT_FILE -> {
                val session = Session(Session.NEW, data.data)
                val bundle = Bundle().apply {
                    putParcelable(Constants.KEY_SESSION, session)
                }
                if (FileUtils.isImage(data.data)) {
                    val fragment = EditorImageFragment().apply {
                        arguments = bundle
                    }
                    parentActivity.replaceFragment(fragment, android.R.id.content, true)
                } else {
                    val fragment = EditorVideoFragment().apply {
                        arguments = bundle
                    }
                    parentActivity.replaceFragment(fragment, android.R.id.content, true)
                }
            }
        }
    }
}