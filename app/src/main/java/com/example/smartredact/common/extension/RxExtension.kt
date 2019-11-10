package com.example.smartredact.common.extension

import io.reactivex.ObservableEmitter
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by TuHA on 11/6/2019.
 */

fun Disposable.addToCompositeDisposable(compositeDisposable: CompositeDisposable) {
    compositeDisposable.add(this)
}

fun <T> ObservableEmitter<T>.checkDisposed(): ObservableEmitter<T>? {
    return if (this.isDisposed) {
        null
    } else {
        this
    }
}