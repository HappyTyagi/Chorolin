package com.choraline.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



/**
 * Created by deepak Tyagi on 8/22/2017.
 */
class CurrencyAndPriceData {

    @SerializedName("curid")
    @Expose
    var curid: String = ""
    @SerializedName("price")
    @Expose
    var price: Float = 0f
    @SerializedName("currency_symbol")
    @Expose
    var currencySymbol: String = ""
    @SerializedName("currency_code")
    @Expose
    var currencyCode: String = ""
    @SerializedName("currency_name")
    @Expose
    var currencyName: String = ""


}