package com.choraline.models

import android.os.Parcel
import android.os.Parcelable.Creator
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



/**
 * Created by deepak Tyagi on 8/4/2017.
 */
class SongsData() : Parcelable {

    @SerializedName("id")
    @Expose
    var id: String = ""
    @SerializedName("title")
    @Expose
    var title: String = ""
    @SerializedName("subtitle")
    @Expose
    var subtitle: String = ""
    @SerializedName("song_url")
    @Expose
    var songUrl: String = ""
    @SerializedName("song_title")
    @Expose
    var songTitle: String = ""


    @SerializedName("voiceType")
    @Expose
    var voiceType: String = ""


    @SerializedName("isPlaying")
    @Expose
    var isPlaying: Boolean = false

    @Expose
    var dbUrl: String = ""

    @Expose
    var localFileUri : String = ""

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()!!
        title = parcel.readString()!!
        subtitle = parcel.readString()!!
        songUrl = parcel.readString()!!
        songTitle = parcel.readString()!!
        voiceType = parcel.readString()!!
        isPlaying = parcel.readByte() != 0.toByte()
        dbUrl = parcel.readString()!!
        localFileUri = parcel.readString()!!
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(subtitle)
        parcel.writeString(songUrl)
        parcel.writeString(songTitle)
        parcel.writeString(voiceType)
        parcel.writeByte(if (isPlaying) 1 else 0)
        parcel.writeString(dbUrl)
        parcel.writeString(localFileUri)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Creator<SongsData> {
        override fun createFromParcel(parcel: Parcel): SongsData {
            return SongsData(parcel)
        }

        override fun newArray(size: Int): Array<SongsData?> {
            return arrayOfNulls(size)
        }
    }


}