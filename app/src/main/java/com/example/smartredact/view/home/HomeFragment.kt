package com.example.smartredact.view.home

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
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
import java.io.File
import java.io.IOException
import javax.inject.Inject

class HomeFragment : BaseFragment(), HomeView {

    @Inject
    lateinit var mPresenter: HomePresenter

    override fun getActivityHome(): FragmentActivity {
        return this.activity!!
    }

    companion object {
        const val REQUEST_CODE_SELECT_FILE = 1234
        const val REQUEST_IMAGE_CAPTURE = 2345
        const val REQUEST_VIDEO_CAPTURE = 3456
    }

    override fun inject(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter.attachView(this)
    }

    override fun onDestroy() {
        mPresenter.detachView()
        super.onDestroy()
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
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(this.activity?.packageManager!!).also {
                val photoFile: File? = try {
                    mPresenter.createImageFile()
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this.context!!,
                        "com.example.smartredact.view.home",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    private fun recordVideo() {
        Intent(MediaStore.ACTION_VIDEO_CAPTURE).also { takeVideoIntent ->
            takeVideoIntent.resolveActivity(this.activity?.packageManager!!)?.also {
                startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE)
            }
        }
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
                startActivityForResult(
                    Intent.createChooser(it, "Choose a file"),
                    REQUEST_CODE_SELECT_FILE
                )
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
                when (requestCode) {
                    REQUEST_IMAGE_CAPTURE -> {
                        val session = Session(Session.NEW, Uri.parse(mPresenter.currentPhotoPath))
                        val bundle = Bundle().apply {
                            putParcelable(Constants.KEY_SESSION, session)
                        }
                        val fragment = EditorImageFragment().apply {
                            arguments = bundle
                        }
                        parentActivity.replaceFragment(fragment, android.R.id.content, true)
                    }
                }
            } else {
                when (requestCode) {
                    REQUEST_VIDEO_CAPTURE,
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
        } else {
            //todo TuHA:
            Toast.makeText(this.context, "picture or video not taken", Toast.LENGTH_SHORT).show()
        }
    }
}