package com.choraline.models

import android.os.Parcel
import android.os.Parcelable.Creator
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



/**
 * Created by deepak Tyagi on 8/3/2017.
 */
class FreeTrailModel {



        @SerializedName("status")
        @Expose
        var status: Boolean = false
        @SerializedName("message")
        @Expose
        var message: String = ""
        @SerializedName("response")
        @Expose
        var response: FreeTrailData? = null




}