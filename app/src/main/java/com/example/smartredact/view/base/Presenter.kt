package com.example.smartredact.view.base

/**
 * Created by TuHA on 06/06/2019.
 */
interface Presenter<V: BaseView> {

    fun attachView(view: V)

    fun detachView()
}