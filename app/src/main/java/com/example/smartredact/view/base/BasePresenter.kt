package com.example.smartredact.view.base

import android.os.Bundle
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * Created by TuHA on 06/06/2019.
 */
open class BasePresenter<V : BaseView> @Inject constructor() : Presenter<V> {

    protected var view: V? = null
    protected val compositeDisposable = CompositeDisposable()

    override fun attachView(view: V) {
        this.view = view
    }

    override fun detachView() {
        compositeDisposable.clear()
        this.view = null
    }

    open fun getArguments(extras: Bundle?) {

    }
}