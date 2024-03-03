package com.choraline.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by deepak Tyagi on 9/29/2017.
 */
class CountryData {

    @SerializedName("id")
    @Expose
    var id: String = ""

    @SerializedName("name")
    @Expose
    var name: String = ""
}