package com.choraline.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by deepak Tyagi on 8/22/2017.
 */
class BasketItemData {

    @SerializedName("id")
    @Expose
    var id: String = ""

    @SerializedName("songId")
    @Expose
    var songId: String = ""

    @SerializedName("title")
    @Expose
    var title: String = ""

    @SerializedName("subtitle")
    @Expose
    var subtitle: String = ""

    @SerializedName("voiceType")
    @Expose
    var voiceType: String = ""

    @SerializedName("price")
    @Expose
    var price: String = ""

    @SerializedName("barcode")
    @Expose
    var barcode: String = ""
    //with_singer

    @SerializedName("with_singer")
    @Expose
    var withSinger: String = ""

}