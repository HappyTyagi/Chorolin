package com.choraline.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by deepak Tyagi on 9/13/2017.
 */

class PurchaseData {

    @SerializedName("orderId")
    @Expose
    var orderId: String = ""

    @SerializedName("telephone")
    @Expose
    var telephone: String = ""

    @SerializedName("email")
    @Expose
    var email: String = ""

    @SerializedName("choir_name")
    @Expose
    var choir_name: String = ""

    @SerializedName("currencyId")
    @Expose
    var currencyId: String = ""

    @SerializedName("currency_code")
    @Expose
    var currency_code: String = ""

    @SerializedName("currency_symbol")
    @Expose
    var currency_symbol: String = ""

    @SerializedName("subtotal")
    @Expose
    var subTotal: String = ""

    @SerializedName("discount_code")
    @Expose
    var discount_code: String = ""

    @SerializedName("braintreeToken")
    @Expose
    var braintreeToken: String = ""

    @SerializedName("result")
    @Expose
    var result: String = ""
}
