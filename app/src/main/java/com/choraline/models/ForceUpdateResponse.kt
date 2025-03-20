package com.example.example

import com.google.gson.annotations.SerializedName


data class ForceUpdateResponse (

  @SerializedName("status"     ) var status     : Int?      = null,
  @SerializedName("message"    ) var message    : String?   = null,
  @SerializedName("production" ) var production : String?   = null,
  @SerializedName("response"   ) var response   : Response? = Response()

)