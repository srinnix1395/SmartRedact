package com.example.smartredact.view.editor.image

import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import com.example.smartredact.R
import com.example.smartredact.common.di.component.ActivityComponent
import com.example.smartredact.view.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_editor_image.*
import javax.inject.Inject

/**
 * Created by TuHA on 11/11/2019.
 */
class EditorImageFragment : BaseFragment() , EditorImageView {
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
        //do nothing
    }

    override fun initData() {
        mPresenter.getArguments(arguments)
        mPresenter.setImageURI()
    }

}