package com.choraline.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by deepak Tyagi on 8/22/2017.
 */
class PurchasedMusicData {

    @SerializedName("id")
    @Expose
    var id: String = ""

    @SerializedName("title")
    @Expose
    var title: String = ""

    @SerializedName("subtitle")
    @Expose
    var subtitle: String = ""

    @SerializedName("voiceType")
    @Expose
    var voiceType: String = ""

    @SerializedName("date_created")
    @Expose
    var dateCreated: String = ""

    @SerializedName("songlist")
    @Expose
    var songlist = ArrayList<SongsData>()

    @SerializedName("isSelected")
    @Expose
    var isSelected: Boolean = false
}