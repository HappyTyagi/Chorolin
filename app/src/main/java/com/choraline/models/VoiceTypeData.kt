package com.choraline.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by deepak Tyagi on 8/22/2017.
 */
class VoiceTypeData {

    @SerializedName("songId")
    @Expose
    var songId: String = ""

    @SerializedName("type")
    @Expose
    var type : String = ""

    @SerializedName("isSelected")
    @Expose
    var isSelected: Boolean = false
}