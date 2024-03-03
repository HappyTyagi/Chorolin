package com.choraline.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * Created by deepak Tyagi on 8/22/2017.
 */
class OrderHistoryItemData {

    @SerializedName("id")
    @Expose
    var id: String = ""

    @SerializedName("title")
    @Expose
    var title: String = ""

    @SerializedName("subtitle")
    @Expose
    var subtitle: String = ""

    @SerializedName("price")
    @Expose
    var price: String = ""

    @SerializedName("voiceType")
    @Expose
    var voiceType: String = ""



}