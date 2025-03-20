package com.example.example

import com.google.gson.annotations.SerializedName


data class Response (

  @SerializedName("androidMobileVersion" ) var androidMobileVersion : String? = null,
  @SerializedName("androidForceUpdate"   ) var androidForceUpdate   : String? = null,
  @SerializedName("andServerMaintains"   ) var andServerMaintains   : String? = null,
  @SerializedName("android_msg"          ) var androidMsg           : String? = null,
  @SerializedName("iosMobileVersion"     ) var iosMobileVersion     : String? = null,
  @SerializedName("ioServerMaintains"    ) var ioServerMaintains    : String? = null,
  @SerializedName("iosForceUpdate"       ) var iosForceUpdate       : String? = null,
  @SerializedName("ios_msg"              ) var iosMsg               : String? = null

)