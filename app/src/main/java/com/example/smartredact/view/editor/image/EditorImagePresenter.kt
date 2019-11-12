package com.example.smartredact.view.editor.image

import android.os.Bundle
import com.example.smartredact.common.constants.Constants
import com.example.smartredact.data.model.Session
import com.example.smartredact.view.base.BasePresenter

class EditorImagePresenter @javax.inject.Inject constructor() : BasePresenter<EditorImageView>(){

    private lateinit var session: Session

    override fun getArguments(extras: Bundle?) {
        super.getArguments(extras)
        session = extras?.getParcelable(Constants.KEY_SESSION)!!
    }

    fun setImageURI(){
        view?.getImageViewPhoto()?.setImageURI(session.data)
    }

}