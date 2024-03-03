package com.choraline.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * Created by deepak Tyagi on 9/14/2017.
 */
class OrderHistoryModel {

    @SerializedName("status")
    @Expose
    open var status: Boolean = false

    @SerializedName("lastDate")
    @Expose
    open var lastDate: String = "0"






    @SerializedName("message")
    @Expose
    var message: String = ""
    @SerializedName("response")
    @Expose
    var response: Response? = null


    inner class Response
    {
        @SerializedName("orderlist")
        @Expose
        var orderlist = ArrayList<OrderHistoryData>()
        fun a()
        {
            status
        }
    }

}