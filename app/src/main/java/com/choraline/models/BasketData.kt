package com.choraline.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by deepak Tyagi on 8/23/2017.
 */
class BasketData {

    @SerializedName("currencyId")
    @Expose
    var currency_id: String = ""

    @SerializedName("currency_code")
    @Expose
    var currency_code: String = ""

    @SerializedName("currency_symbol")
    @Expose
    var currency_symbol: String = ""

    @SerializedName("subtotal")
    @Expose
    var subtotal: String = ""

    @SerializedName("list")
    @Expose
    var list = ArrayList<BasketItemData>()
}