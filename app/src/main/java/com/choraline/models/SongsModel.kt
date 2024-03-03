package com.choraline.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



/**
 * Created by deepak Tyagi on 8/4/2017.
 */
class SongsModel {

    @SerializedName("status")
    @Expose
    var status: Boolean = false
    @SerializedName("message")
    @Expose
    var message: String = ""
    @SerializedName("response")
    @Expose
    var response: Response? = null



    open class Response {

        @SerializedName("songlist")
        @Expose
        var songlist: ArrayList<SongsData>? = null

    }
}