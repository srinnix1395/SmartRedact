package com.example.smartredact.view.editor.image

import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import com.example.smartredact.R
import com.example.smartredact.common.di.component.ActivityComponent
import com.example.smartredact.common.facerecoginition.Classifier
import com.example.smartredact.common.utils.TimeUtils
import com.example.smartredact.common.widget.faceview.Face
import com.example.smartredact.data.model.VideoMetadata
import com.example.smartredact.view.base.BaseFragment
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.fragment_editor.*
import kotlinx.android.synthetic.main.fragment_editor_image.*
import kotlinx.android.synthetic.main.fragment_editor_image.imvDetectFace
import javax.inject.Inject
import kotlinx.android.synthetic.main.fragment_editor_image.overlayView as overlayView1

/**
 * Created by TuHA on 11/11/2019.
 */
class EditorImageFragment : BaseFragment(), EditorImageView {

    private var renderedWidth: Float = 0F
    private var renderedHeight: Float = 0F
    private var srcWidth: Float = 0F
    private var srcHeight: Float = 0F

    override fun getActivityEditorImage(): FragmentActivity {
        return this.activity!!
    }

    override fun getImageViewPhoto(): ImageView {
        return imgPhoto
    }

    @Inject
    lateinit var mPresenter: EditorImagePresenter

    override fun inject(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_editor_image
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter.attachView(this)
    }

    override fun onDestroy() {
        mPresenter.detachView()
        super.onDestroy()
    }

    override fun initView() {
        imvDetectFace.setOnClickListener {
            srcWidth = imgPhoto.drawable.intrinsicWidth.toFloat()
            srcHeight = imgPhoto.drawable.intrinsicHeight.toFloat()
            renderedWidth = imgPhoto.width.toFloat()
            renderedHeight = (imgPhoto.width * fullScreen.height / fullScreen.width).toFloat()
            val paddingHorizontal = (imgPhoto.width - renderedWidth) / 2
            val paddingVertical = (imgPhoto.height - renderedHeight) / 2
            mPresenter.detectFaces(
                srcWidth,
                srcHeight,
                renderedWidth,
                renderedHeight,
                paddingHorizontal,
                paddingVertical
            )
        }
    }

    override fun initData() {
        mPresenter.getArguments(arguments)
        mPresenter.setImageURI()
    }

    override fun showListFace(listFace: List<Face>) {
        listFaceView.setData(listFace)

        val recognitions = arrayListOf<Classifier.Recognition>()
        listFace.forEach { face ->
            recognitions.addAll(face.face)
        }
        overlayView.setData(recognitions)
    }
}