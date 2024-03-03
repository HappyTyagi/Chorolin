package com.choraline.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by deepak Tyagi on 7/28/2017.
 */

class LoginModel {

    @SerializedName("status")
    @Expose
    var status: Boolean = false
    @SerializedName("message")
    @Expose
    var message: String = ""
    @SerializedName("response")
    @Expose
    var response= UserData()
}
