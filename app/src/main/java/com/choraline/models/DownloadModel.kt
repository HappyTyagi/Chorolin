package com.choraline.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DownloadModel() :Parcelable
{

    @Expose
    var name:String=""
    @Expose
    var path:String=""
    @SerializedName("isPlaying")
    @Expose
    var isPlaying: Boolean = false

    constructor(parcel: Parcel) : this() {
        name = parcel.readString()!!
        path = parcel.readString()!!
        isPlaying = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(path)
        parcel.writeByte(if (isPlaying) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DownloadModel> {
        override fun createFromParcel(parcel: Parcel): DownloadModel {
            return DownloadModel(parcel)
        }

        override fun newArray(size: Int): Array<DownloadModel?> {
            return arrayOfNulls(size)
        }
    }

}