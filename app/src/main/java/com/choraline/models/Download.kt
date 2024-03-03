package com.choraline.models

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by deepak Tyagi on 8/7/2017.
 */

class Download : Parcelable {
    constructor() {

    }

    var progress: Int = 0
    var currentFileSize: Int = 0
    var totalFileSize: Int = 0

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {

        dest.writeInt(progress)
        dest.writeInt(currentFileSize)
        dest.writeInt(totalFileSize)
    }

    private constructor(`in`: Parcel) {

        progress = `in`.readInt()
        currentFileSize = `in`.readInt()
        totalFileSize = `in`.readInt()
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Download> = object : Parcelable.Creator<Download> {
            override fun createFromParcel(`in`: Parcel): Download {
                return Download(`in`)
            }

            override fun newArray(size: Int): Array<Download?> {
                return arrayOfNulls(size)
            }
        }
    }
}
