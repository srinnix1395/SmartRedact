package com.example.smartredact.view.editor.image

import android.os.Bundle
import com.example.smartredact.common.constants.Constants
import com.example.smartredact.common.extension.addToCompositeDisposable
import com.example.smartredact.common.facerecoginition.ObjectDetectionUtils
import com.example.smartredact.data.model.ImageMetadata
import com.example.smartredact.data.model.Session
import com.example.smartredact.view.base.BasePresenter
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class EditorImagePresenter @javax.inject.Inject constructor() : BasePresenter<EditorImageView>() {

    private lateinit var session: Session

    private lateinit var imageMetadata: ImageMetadata

    @Inject
    lateinit var objectDetectionUtils: ObjectDetectionUtils

    override fun getArguments(extras: Bundle?) {
        super.getArguments(extras)
        session = extras?.getParcelable(Constants.KEY_SESSION)!!
    }

    fun setImageURI() {
        view?.getImageViewPhoto()?.setImageURI(session.data)
    }

    fun detectFaces(
        srcWidth: Float,
        srcHeight: Float,
        renderedWidth: Float,
        renderedHeight: Float,
        paddingHorizontal: Float,
        paddingVertical: Float
    ) {
        Single
            .fromCallable {
                objectDetectionUtils.startSession(srcWidth.toInt(), srcHeight.toInt(), 90)
                return@fromCallable objectDetectionUtils.detectFacesImage(
                    session.data!!,
                    srcWidth,
                    srcHeight,
                    renderedWidth,
                    renderedHeight,
                    paddingHorizontal,
                    paddingVertical
                )
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                view?.showProgressDialog(true)
            }
            .doFinally {
                view?.showProgressDialog(false)
            }
            .subscribe({ listFace ->
                view?.showListFace(listFace)
            }, { error ->
                error.printStackTrace()
            })
            .addToCompositeDisposable(compositeDisposable)
    }

}