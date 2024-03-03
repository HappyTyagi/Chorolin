package com.choraline.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BaseModel {
    @SerializedName("status")
    @Expose
    var status: Boolean = false

    @SerializedName("message")
    @Expose
    var message: String = ""
}