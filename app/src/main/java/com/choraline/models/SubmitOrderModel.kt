package com.choraline.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by deepak Tyagi on 9/13/2017.
 */
class SubmitOrderModel {

    @SerializedName("status")
    @Expose
    var status: Boolean = false

    @SerializedName("message")
    @Expose
    var message: String = ""

    @SerializedName("response")
    @Expose
    var response: Response? = null


    class Response
    {
        @SerializedName("orderId")
        @Expose
        var orderId: String = ""
    }

}