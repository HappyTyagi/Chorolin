package com.choraline.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by deepak Tyagi on 8/22/2017.
 */
class PurchasedMusicModel {

    @SerializedName("status")
    @Expose
    var status: Boolean = false

    @SerializedName("message")
    @Expose
    var message: String = ""

    @SerializedName("lastDate")
    @Expose
    var lastDate: String = "0"

    @SerializedName("response")
    @Expose
    var response: Response? = null


    class Response
    {
        @SerializedName("albumlist")
        @Expose
        var album = ArrayList<PurchasedMusicData>()
    }
}