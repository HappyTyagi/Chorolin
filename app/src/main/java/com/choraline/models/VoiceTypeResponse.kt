package com.choraline.models

import android.os.Parcelable
import android.os.Parcel
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



/**
 * Created by deepak Tyagi on 8/3/2017.
 */
class VoiceTypeResponse : Parcelable {

    @SerializedName("voicetype")
    @Expose
    var voicetype = ArrayList<FreeVoiceTypeData>()
    val CREATOR: Parcelable.Creator<VoiceTypeResponse> = object : Parcelable.Creator<VoiceTypeResponse> {


        override fun createFromParcel(`in`: Parcel): VoiceTypeResponse {
            val instance = VoiceTypeResponse()
            `in`.readList(instance.voicetype, FreeVoiceTypeData::class.java!!.getClassLoader())
            return instance
        }

        override fun newArray(size: Int): Array<VoiceTypeResponse?> {
            return arrayOfNulls<VoiceTypeResponse>(size)
        }

    }


    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeList(voicetype)
    }

    override fun describeContents(): Int {
        return 0
    }

}