package com.choraline.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by deepak Tyagi on 8/21/2017.
 */
 class ChoralWorksModel {

    @SerializedName("status")
    @Expose
    var status: Boolean = false;

    @SerializedName("message")
    @Expose
    var message: String = "";

    @SerializedName("response")
    @Expose
    var response: Response? = null;

    @SerializedName("lastDate")
    @Expose
    var lastDate: String = "0";


    class Response
    {
        @SerializedName("banner_image")
        @Expose
        var bannerImage: String = "";

        @SerializedName("paidsonglist")
        @Expose
        var paidsongList = ArrayList<ChoralWorksData>()

        @SerializedName("paidsonglistwithsinger")
        @Expose
        var paidsongwitsingerList = ArrayList<ChoralWorksData>()
       //------------------------------

        @SerializedName("deletedpaidsonglist")
        @Expose
        var deletedpaidsonglist = ArrayList<ChoralWorksData>()


        @SerializedName("deletedpaidsonglistwithsinger")
        @Expose
        var deletedpaidsonglistwithsinger = ArrayList<ChoralWorksData>()

        @SerializedName("updatepaidsonglist")
        @Expose
        var updatepaidsonglist = ArrayList<ChoralWorksData>()

        @SerializedName("updatepaidsonglistwithsinger")
        @Expose
        var updatepaidsonglistwithsinger = ArrayList<ChoralWorksData>()

    }
}