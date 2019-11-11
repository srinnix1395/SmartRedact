package com.example.smartredact.data.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import com.example.smartredact.common.utils.FileUtils

/**
 * Created by TuHA on 11/11/2019.
 */
class Session() : Parcelable {

    companion object CREATOR : Parcelable.Creator<Session> {
        override fun createFromParcel(parcel: Parcel): Session {
            return Session(parcel)
        }

        override fun newArray(size: Int): Array<Session?> {
            return arrayOfNulls(size)
        }

        const val NEW = 0
        const val CONTINUTE = 1
    }

    var type: Int = NEW

    var data: Uri? = null

    val isImage: Boolean
        get() {
            return FileUtils.isImage(data)
        }

    constructor(type: Int, data: Uri?): this() {
        this.type = type
        this.data = data
    }

    constructor(parcel: Parcel) : this() {
        type = parcel.readInt()
        data = parcel.readParcelable(Uri::class.java.classLoader)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(type)
        parcel.writeParcelable(data, flags)
    }

    override fun describeContents(): Int {
        return 0
    }
}