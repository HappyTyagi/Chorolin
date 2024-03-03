package com.choraline.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by Kuldeep on 2/1/17.
 */

class PreferenceHelper(private val mContext: Context) {
    private val mSharedPreferences: SharedPreferences


    init {
        mSharedPreferences = mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    }

    //=======================( Clear Preferences )================================
    fun clearSharedPreference() {
        val editor = mSharedPreferences.edit()
        editor.clear()
        editor.commit()
    }

    //=======================( IS LOGIN )================================
    var isLogin: Boolean
        get() = mSharedPreferences.getBoolean(IS_LOGIN, false)
        set(isLoggedIn) {
            val editor = mSharedPreferences.edit()
            editor.putBoolean(IS_LOGIN, isLoggedIn)
            editor.commit()
        }

    //=======================( IMAGE_URL_SDCARD )================================
    var imageUrlSdcard: String?
        get() = mSharedPreferences.getString(IMAGE_URL_SDCARD, "")
        set(ImageUrlSdcard) {
            val editor = mSharedPreferences.edit()
            editor.putString(IMAGE_URL_SDCARD, ImageUrlSdcard)
            editor.commit()
        }

    //=======================( COUNTRY_DATA )================================

    var countryData: String
        get() = mSharedPreferences.getString(COUNTRY_DATA, "")!!
        set(countryData) {
            val editor = mSharedPreferences.edit()
            editor.putString(COUNTRY_DATA, countryData)
            editor.commit()
        }

    //=======================( USER_DATA )================================

    var userData: String
        get() = mSharedPreferences.getString(USER_DATA, "")!!
        set(userData) {
            val editor = mSharedPreferences.edit()
            editor.putString(USER_DATA, userData)
            editor.commit()
        }

    //=======================( USER_ID )================================

    var userId: String
        get() = mSharedPreferences.getString(USER_ID, "")!!
        set(UserId) {
            val editor = mSharedPreferences.edit()
            editor.putString(USER_ID, UserId)
            editor.commit()
        }

    //=======================( USER_ID )================================

    var userName: String
        get() = mSharedPreferences.getString(USER_NAME, "")!!
        set(UserId) {
            val editor = mSharedPreferences.edit()
            editor.putString(USER_NAME, UserId)
            editor.commit()
        }

    //=======================( BASKET_DATA )================================

    var basketData: String
        get() = mSharedPreferences.getString(BASKET_DATA, "")!!
        set(basketData) {
            val editor = mSharedPreferences.edit()
            editor.putString(BASKET_DATA, basketData)
            editor.commit()
        }


    //=======================( TOKEN )================================

    var token: String
        get() = mSharedPreferences.getString(TOKEN, "")!!
        set(Token) {
            val editor = mSharedPreferences.edit()
            editor.putString(TOKEN, Token)
            editor.commit()
        }

    //=======================( composerlisttimestamp )================================

    //accessToken

//=====================================accessToken=================================
    var accessToken: String
        get() = mSharedPreferences.getString(ACCESS_TOKEN, "")!!
        set(Token) {
            val editor = mSharedPreferences.edit()
            editor.putString(ACCESS_TOKEN, Token)
            editor.commit()
        }


    //==============================================================================



    var composerListTimeStamp: String
        get() = "0"
        set(timeStamp) {
            val editor = mSharedPreferences.edit()
            editor.putString(COMSOSER_LIST_TIME_STAMP, timeStamp)
            editor.commit()
        }

    var historyListTimeStamp: String
        get() = mSharedPreferences.getString(HISTORY_TIME_STAMP, "0")!!
        set(timeStamp) {
            val editor = mSharedPreferences.edit()
            editor.putString(HISTORY_TIME_STAMP, timeStamp)
            editor.commit()
        }


    var purchaseSongTimeStamp: String
        get() = mSharedPreferences.getString(PURCHASE_SONG_TIME_STAMP, "0")!!
        set(timeStamp) {
            val editor = mSharedPreferences.edit()
            editor.putString(PURCHASE_SONG_TIME_STAMP, timeStamp)
            editor.commit()
        }




    companion object {

        private val PREFS_NAME = "PreferenceHelper"

        private val IS_LOGIN = "isLogin"
        private val IMAGE_URL_SDCARD = "image_url_sdcard"
        private val USER_ID = "user_id"
        private val USER_NAME = "UserName"
        private val TOKEN = "token"
        private val ACCESS_TOKEN = "access_token"
        private val USER_DATA = "UserData"
        private val COUNTRY_DATA = "CountryData"
        private val BASKET_DATA = "BasketData"
        private val COMSOSER_LIST_TIME_STAMP = "composerlisttimestamp"

        private val HISTORY_TIME_STAMP="historytimestamp"
        private val ALBUM_TIME_STAMP = "albumtimestamp"

        private val PURCHASE_SONG_TIME_STAMP  ="purchasedsongtimestamp"
    }
}