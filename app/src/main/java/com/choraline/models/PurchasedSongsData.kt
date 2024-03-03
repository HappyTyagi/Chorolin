package com.choraline.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by deepak Tyagi on 8/4/2017.
 */
class PurchasedSongsData : Parcelable {

    @SerializedName("id")
    @Expose
    var id: String = ""
    @SerializedName("title")
    @Expose
    var title: String = ""
    @SerializedName("subtitle")
    @Expose
    var subtitle: String = ""
    @SerializedName("song_url")
    @Expose
    var songUrl: String = ""
    @SerializedName("song_name")
    @Expose
    var songName: String = ""
    @SerializedName("songtitle")
    @Expose
    var songtitle: String = ""
    @SerializedName("isPlaying")
    @Expose
    var isPlaying: Boolean = false


    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(id)
        dest.writeValue(title)
        dest.writeValue(subtitle)
        dest.writeValue(songUrl)
        dest.writeValue(songName)
        dest.writeValue(songtitle)
        dest.writeValue(isPlaying)

    }

    private fun readFromParcel(parcel: Parcel){
        id = parcel.readString()!!
        title = parcel.readString()!!
        subtitle = parcel.readString()!!
        songUrl = parcel.readString()!!
        songName = parcel.readString()!!
        songtitle = parcel.readString()!!
        isPlaying = if (`parcel`.readInt() === 0) false else true;
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<PurchasedSongsData> = object : Parcelable.Creator<PurchasedSongsData> {


            override fun createFromParcel(`in`: Parcel): PurchasedSongsData {
                val instance = PurchasedSongsData()
                instance.id = `in`.readValue(String::class.java.classLoader) as String
                instance.title = `in`.readValue(String::class.java.classLoader) as String
                instance.subtitle = `in`.readValue(String::class.java.classLoader) as String
                instance.songUrl = `in`.readValue(String::class.java.classLoader) as String
                instance.songName = `in`.readValue(String::class.java.classLoader) as String
                instance.songtitle = `in`.readValue(String::class.java.classLoader) as String
                instance.isPlaying = `in`.readValue(Boolean::class.java.classLoader) as Boolean
                return instance
            }

            override fun newArray(size: Int): Array<PurchasedSongsData?> {
                return arrayOfNulls(size)
            }

        }
    }

}