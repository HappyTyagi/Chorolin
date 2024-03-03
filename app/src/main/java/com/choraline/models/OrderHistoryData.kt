package com.choraline.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * Created by deepak Tyagi on 8/22/2017.
 */
class OrderHistoryData  {
    @SerializedName("order_id")
    @Expose
    var order_id: String = ""

    @SerializedName("order_date")
    @Expose
    var order_date: String = ""

    @SerializedName("subtotal")
    @Expose
    var subtotal: String = ""

    @SerializedName("discount_percentage")
    @Expose
    var discount_percentage: String = ""

    @SerializedName("currency_symbol")
    @Expose
    var currency_symbol: String = ""

    @SerializedName("list")
    @Expose
    var list = ArrayList<OrderHistoryItemData>()

}