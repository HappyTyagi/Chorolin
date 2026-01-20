package com.choraline.utils

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import com.choraline.R


/**
 * Created by deepak Tyagi on 5/16/2017.
 */

object Constants {

    //Application TAGs
    const val LOG_TAG = "Choraline-App-Log"

    //const val BASE_URL = "http://192.168.1.63/choraline/api/"
    //const val BASE_URL = "http://96.57.152.179/choraline/api/" //US Server
    //  const val BASE_URL = "http://148.76.75.203/choraline/api/" //US Server
     const val BASE_URL = "http://app.choraline.com/api/" //Client Server
//     const val BASE_URL = "https://appstage.choraline.com/api/" //Client Server

    const val GET_FREE_TRAIL_URL = "api.php?action=freeVoiceType"
    const val GET_FREE_TRAIL_MUSIC_LIST_URL = "api.php?action=freeMusicList"
    const val LOGIN_URL = "api.php?action=login"
    const val SIGNUP_URL = "api.php?action=userSignup"
    const val FORGOT_PASSWORD_URL = "api.php?action=forgetPassword"
    const val GET_PROFILE_URL = "api.php?action=userProfile"
    const val UPDATE_PROFILE_URL = "api.php?action=updateUserProfile"
    const val COMPOSER_LIST_URL = "api.php?action=composerList"
    const val CHORAL_WORKS_URL = "api.php?action=composerAlbumList";
    const val SONG_DETAIL_UEL = "api.php?action=albumDetail";
    const val ADD_TO_BASKET_URL = "api.php?action=addToBasket";
    const val FREE_GIFT = "api.php?action=iosSubmitOrder"
    const val CHECK_FREE_GIFT_STATUS = "api.php?action=addToBasketIOS"

    const val DELETE_BASKET_ITEM_URL = "api.php?action=removeFromBasket"
    const val APPLY_DISCOUNT_CODE_URL = "api.php?action=applyDiscountcode"

    const val REMOVE_DISCOUNT_CODE_URL = "api.php?action=removeDiscountcode"


    const val GET_BASKET_URL = "api.php?action=getBasket";
    const val PURCHASE_URL = "api.php?action=purchase"
    const val GET_TOKEN_URL = "api.php?action=getBraintreeToken"
    const val SUBMIT_ORDER_URL = "api.php?action=submitOrder"
    const val SUBMIT_GOOGLE_ORDER_URL = "api.php?action=submitOrderAndroid"
    const val ORDER_HISTORY_URL = "api.php?action=orderHistory"
    const val PURCHASED_MUSIC_URL = "api.php?action=purchasedMusic"

    const val CONTACT_US_URL = "api.php?action=contact_us"
    const val TERMS_AND_CONDITIONS_URL = BASE_URL+"api.php?action=pages&title=terms-conditions"
    const val PRIVACY_POLICY_URL = BASE_URL+"api.php?action=pages&title=privacy-policy"




    const val FAQ_URL = BASE_URL+"api.php?action=faq"
    const val ABOUT_US_URL = BASE_URL+"api.php?action=pages&title=about-us"

    const val GET_COUNTRY_LIST_URL = "api.php?action=countries"
    const val GET_STATE_URL = "submit/getplaces/states/{country_id}?response=api"
    const val GET_CITY_URL = "submit/getplaces/cities/{STATE_ID}?response=api"

    const val PREF_NAME = "prefName"
    const val LOGIN_STATUS = "loginStatus"
    const val SIGNUP_STATUS = "signupStatus"

    const val API_LOGIN = 1
    const val API_SIGNUP = 2
    const val API_FORGOT_PASSWORD = 3
    const val API_GET_PROFILE = 4
    const val API_UPDATE_PROFILE = 5
    const val API_GET_FREE_TRAIL = 6
    const val API_GET_FREE_TRAIL_MUSIC_LIST = 7
    const val API_CONTACT_US = 8
    const val API_COMPOSER_LIST = 9
    const val API_CHORAL_WORKS = 10
    const val API_SONG_DETAIL = 11
    const val API_ADD_TO_BASKET = 12
    const val API_DELETE_BASKET_ITEM = 13
    const val API_APPLY_DISCOUNT_CODE = 14
    const val API_GET_BASKET = 15
    const val API_GET_ORDER_HISTORY = 16
    const val API_GET_PURCHASED_MUSIC = 17
    const val API_PURCHASE = 18
    const val API_GET_TOKEN = 180
    const val API_SUBMIT_ORDER = 19
    const val API_GET_COUNTRY_LIST = 20
    const val FREE_GIFT_API = 21
    const val FREE_GIFT_CHECK_API = 22
    const val API_GOOGLE_SUBMIT_ORDER = 23


    interface ACTION {
        companion object {
            const val MAIN_ACTION = "com.marothiatechs.customnotification.action.main"
            const val INIT_ACTION = "com.marothiatechs.customnotification.action.init"
            const val PREV_ACTION = "com.marothiatechs.customnotification.action.prev"
            const val PLAY_ACTION = "com.marothiatechs.customnotification.action.play"
            const val NEXT_ACTION = "com.marothiatechs.customnotification.action.next"
            const val STARTFOREGROUND_ACTION = "com.marothiatechs.customnotification.action.startforeground"
            const val STOPFOREGROUND_ACTION = "com.marothiatechs.customnotification.action.stopforeground"
        }

    }

    interface AppConstants{
        companion object {
            val NAME = "name"
            val SUBTITLE = "subtitle"
            val VOICE_TYPE = "voicetype"
            val TYPE = "type"
            val PATH = "path"
            val TOOLBARTILE = "toolbartitle"
        }


    }
    interface NOTIFICATION_ID {
        companion object {
            val FOREGROUND_SERVICE = 101
        }
    }

    const val DOWNLOAD_COMPLETE_ACTION = "com.choraline.downloadcomplete"

     var IS_APP_RUNNING: Boolean = false
    val DELIMITER="___"

}
