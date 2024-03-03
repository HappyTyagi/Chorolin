package com.choraline.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by deepak Tyagi on 8/21/2017.
 */
class ComposerData {

    @SerializedName("scid")
    @Expose
    var scid: String = ""

    @SerializedName("title")
    @Expose
    var title: String = ""

    @SerializedName("composer_image")
    @Expose
    var composerImage: String = ""

    @SerializedName("banner_image")
    @Expose
    var bannerImage: String = ""

    @SerializedName("free_status")
    @Expose
    var freeStatus: String = ""

}