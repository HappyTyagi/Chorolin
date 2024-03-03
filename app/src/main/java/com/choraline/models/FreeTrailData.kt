package com.choraline.models

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.ArrayList

/**
 * Created by deepak Tyagi on 8/9/2017.
 */

class FreeTrailData : Parcelable {
    @SerializedName("composer_name")
    @Expose
    var composerName: String = ""
    @SerializedName("category_name")
    @Expose
    var categoryName: String = ""
    @SerializedName("banner_image")
    @Expose
    var bannerImage: String = ""
    @SerializedName("voicetype")
    @Expose
    var voicetype = ArrayList<FreeVoiceTypeData>()

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(composerName)
        dest.writeValue(categoryName)
        dest.writeValue(bannerImage)
        dest.writeList(voicetype)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        val CREATOR: Parcelable.Creator<FreeTrailData> = object : Parcelable.Creator<FreeTrailData> {


            override fun createFromParcel(`in`: Parcel): FreeTrailData {
                val instance = FreeTrailData()
                instance.composerName = `in`.readValue(String::class.java.classLoader) as String
                instance.categoryName = `in`.readValue(String::class.java.classLoader) as String
                instance.bannerImage = `in`.readValue(String::class.java.classLoader) as String
                `in`.readList(instance.voicetype, FreeVoiceTypeData::class.java.classLoader)
                return instance
            }

            override fun newArray(size: Int): Array<FreeTrailData?> {
                return arrayOfNulls(size)
            }

        }
    }

}
