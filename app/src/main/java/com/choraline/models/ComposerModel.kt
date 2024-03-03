package com.choraline.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by deepak Tyagi on 8/21/2017.
 */
class ComposerModel {

    @SerializedName("status")
    @Expose
    var status: Boolean = false

    @SerializedName("message")
    @Expose
    var message: String = ""

    @SerializedName("response")
    @Expose
    var response: Response? = null

    @SerializedName("lastDate")
    @Expose
    var lastDate: String = "0"


    class Response
    {
        @SerializedName("composer")
        @Expose
        var composerList = ArrayList<ComposerData>()

        @SerializedName("updatedcomposer")
        @Expose
        var updatedList = ArrayList<ComposerData>()

        @SerializedName("deletedcomposer")
        @Expose
        var deletedList = ArrayList<ComposerData>()

    }
}