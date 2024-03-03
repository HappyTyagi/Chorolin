package com.choraline.models

import android.os.Parcel
import android.os.Parcelable.Creator
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



/**
 * Created by deepak Tyagi on 8/3/2017.
 */
class FreeVoiceTypeData : Parcelable{

    @SerializedName("id")
    @Expose
    var id: String = ""
    @SerializedName("attr1")
    @Expose
    var attr1: String = ""
    @SerializedName("isSelected")
    @Expose
    var isSelected: Boolean = false
    val CREATOR: Parcelable.Creator<FreeVoiceTypeData> = object : Creator<FreeVoiceTypeData> {


        override fun createFromParcel(`in`: Parcel): FreeVoiceTypeData {
            val instance = FreeVoiceTypeData()
            instance.id = `in`.readValue(String::class.java.classLoader) as String
            instance.attr1 = `in`.readValue(String::class.java.classLoader) as String
            instance.isSelected = `in`.readValue(Boolean::class.java.classLoader) as Boolean
            return instance
        }

        override fun newArray(size: Int): Array<FreeVoiceTypeData?> {
            return arrayOfNulls(size)
        }

    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(id)
        dest.writeValue(attr1)
    }

    override fun describeContents(): Int {
        return 0
    }

}