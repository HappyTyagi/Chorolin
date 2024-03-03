package com.choraline.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by deepak Tyagi on 8/21/2017.
 */
class ChoralWorksData {

    @SerializedName("albumId")
    @Expose
    var albumId: String = "";

    @SerializedName("song_name")
    @Expose
    var songName: String = "";
}