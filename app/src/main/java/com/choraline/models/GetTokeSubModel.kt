package com.choraline.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by deepak Tyagi on 9/13/2017.
 */
class GetTokeSubModel {

  
    @SerializedName("braintreeToken")
    @Expose
    var braintreeToken: String = ""



}