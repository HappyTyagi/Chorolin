package com.choraline.models

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by deepak Tyagi on 7/28/2017.
 */

class UserData() : Parcelable {

    @SerializedName("email")
    @Expose
    var email: String = ""

   //access_token

    @SerializedName("access_token")
    @Expose
    var accessToken: String = ""


    @SerializedName("userId")
    @Expose
    var userId: String = ""
    @SerializedName("firstname")
    @Expose
    var firstname: String = ""
    @SerializedName("lastname")
    @Expose
    var lastname: String = ""
    @SerializedName("username")
    @Expose
    var username: String = ""
    @SerializedName("token")
    @Expose
    var token: String = ""
    @SerializedName("addess1")
    @Expose
    var addess1: String = ""
    @SerializedName("addess2")
    @Expose
    var addess2: String = ""
    @SerializedName("town")
    @Expose
    var town: String = ""
    @SerializedName("postcode")
    @Expose
    var postcode: String = ""
    @SerializedName("country")
    @Expose
    var country: String = ""
    @SerializedName("telephone")
    @Expose
    var telephone: String = ""
    @SerializedName("optin")
    @Expose
    var optin: String = ""
    @SerializedName("choir")
    @Expose
    var choir: String = ""
    @SerializedName("loginStatus")
    @Expose
    var loginStatus: String = ""

    constructor(parcel: Parcel) : this() {
        email = parcel.readString()!!
        accessToken = parcel.readString()!!
        userId = parcel.readString()!!
        firstname = parcel.readString()!!
        lastname = parcel.readString()!!
        username = parcel.readString()!!
        token = parcel.readString()!!
        addess1 = parcel.readString()!!
        addess2 = parcel.readString()!!
        town = parcel.readString()!!
        postcode = parcel.readString()!!
        country = parcel.readString()!!
        telephone = parcel.readString()!!
        optin = parcel.readString()!!
        choir = parcel.readString()!!
        loginStatus = parcel.readString()!!
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(email)
        parcel.writeString(accessToken)
        parcel.writeString(userId)
        parcel.writeString(firstname)
        parcel.writeString(lastname)
        parcel.writeString(username)
        parcel.writeString(token)
        parcel.writeString(addess1)
        parcel.writeString(addess2)
        parcel.writeString(town)
        parcel.writeString(postcode)
        parcel.writeString(country)
        parcel.writeString(telephone)
        parcel.writeString(optin)
        parcel.writeString(choir)
        parcel.writeString(loginStatus)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserData> {
        override fun createFromParcel(parcel: Parcel): UserData {
            return UserData(parcel)
        }

        override fun newArray(size: Int): Array<UserData?> {
            return arrayOfNulls(size)
        }
    }


}


