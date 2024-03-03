package com.choraline.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by deepak Tyagi on 8/22/2017.
 */
class SongDetailData {

    @SerializedName("subtitle")
    @Expose
    var subtitle: String = ""

    @SerializedName("banner_image")
    @Expose
    var banner_image: String = ""

    @SerializedName("voiceType")
    @Expose
    var voiceType = ArrayList<VoiceTypeData>()

    @SerializedName("priceValue")
    @Expose
    var priceValue = ArrayList<CurrencyAndPriceData>()


}