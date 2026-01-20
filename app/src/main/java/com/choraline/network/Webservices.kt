package com.choraline.network

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.hardware.display.VirtualDisplay
import android.util.Log
import android.view.Window
import com.android.billingclient.api.Purchase
import com.choraline.R
import com.choraline.models.*
import com.choraline.utils.AppController
import com.choraline.utils.Utility
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Field

/**
 * Created by deepak Tyagi on 7/10/2017.
 */
class Webservices(
    private var mContext: Context,
    private val mApiListener: APIListener,
    isShowProgressDialog: Boolean?,
    msg: String
) {

    private val dialog: ProgressDialog
    private val mApiInterface: APIInterface
    private val device_type = "1"
    private val device_token = ""
    private val latitude = ""
    private val longitude = ""
    private var context: Context? = null


    init {
        dialog = ProgressDialog(mContext)
        this.context = mContext
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        if (msg !== "")
            dialog!!.setMessage(msg)
        dialog.setCancelable(false)

        if (isShowProgressDialog!!) {
            try {
                dialog.show()

            } catch (E: Exception) {

            }
        }
        mApiInterface = APIClient.getClient()!!.create(APIInterface::class.java)
    }

    fun onResponseSuccess(obj: Any, api: Int) {
        try {
            dialog!!.dismiss()
            mApiListener.onApiSuccess(obj, api)
        } catch (e: Exception) {
            e!!.printStackTrace()
        }
    }

    fun onFailureResponse(t: Throwable, api: Int) {
        dialog!!.dismiss()
        // Utility!!.showMessageDialog(mContext, mContext.resources.getString(R.string.alert_server_not_responding))
        mApiListener.onApiFailure(t, api)
    }

    fun onNoInternetConnection(api: Int) {
        var t = Throwable()
        mApiListener.onApiFailure(t, api)
        if (dialog == null) return
        dialog!!.dismiss()


        Utility.showMessageDialog(
            mContext,
            mContext.resources.getString(R.string.alert_no_internet_connection)
        )
    }

    fun callLoginAPI(
        email: String,
        password: String,
        gcmToken: String,
        deviceType: String,
        api: Int
    ) {
        if (Utility!!.isNetworkAvailable(mContext)) {
            var call = mApiInterface.doLogin(email, password, gcmToken, deviceType)
            call.enqueue(object : Callback<LoginModel> {
                override fun onResponse(call: Call<LoginModel>, response: Response<LoginModel>) {
                    val mObject = response.body()
                    if (mObject != null) {
                        onResponseSuccess(mObject, api)
                    }
                }

                override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                    onFailureResponse(t, api)
                }
            })

        } else {
            onNoInternetConnection(api)
        }
    }

    fun callForgotPasswordAPI(email: String, api: Int) {
        if (Utility!!.isNetworkAvailable(mContext)) {
            var call = mApiInterface.doForgotPassword(email)
            call.enqueue(object : Callback<GenericResponseModel> {
                override fun onResponse(
                    call: Call<GenericResponseModel>,
                    response: Response<GenericResponseModel>
                ) {
                    val mObject = response.body()
                    if (mObject != null) {
                        onResponseSuccess(mObject, api)
                    }
                }

                override fun onFailure(call: Call<GenericResponseModel>, t: Throwable) {
                    onFailureResponse(t, api)
                }
            })

        } else {
            onNoInternetConnection(api)
        }
    }

    fun callSignupAPI(
        first_name: String, last_name: String, email: String, conf_email: String, password: String,
        conf_password: String, choir_name: String, gcmToken: String, deviceType: String, api: Int
    ) {
        if (Utility!!.isNetworkAvailable(mContext)) {
            var call = mApiInterface.doSignup(
                first_name,
                last_name,
                email,
                conf_email,
                password,
                conf_password,
                choir_name,
                gcmToken,
                deviceType
            );
            call.enqueue(object : Callback<LoginModel> {
                override fun onResponse(call: Call<LoginModel>, response: Response<LoginModel>) {
                    val mObject = response.body()
                    if (mObject != null) {
                        onResponseSuccess(mObject, api)
                    }
                }

                override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                    onFailureResponse(t, api)
                }
            })

        } else {
            onNoInternetConnection(api)
        }
    }

    fun callGetProfileAPI(userId: String, gcmToken: String, deviceType: String, api: Int) {
        if (Utility!!.isNetworkAvailable(mContext)) {
            var call = mApiInterface.doGetProfile(
                userId,
                gcmToken,
                deviceType,
                AppController.appPref.accessToken
            )
            call.enqueue(object : Callback<LoginModel> {
                override fun onResponse(call: Call<LoginModel>, response: Response<LoginModel>) {
                    val mObject = response.body()
                    if (mObject != null) {
                        onResponseSuccess(mObject, api)
                    }
                }

                override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                    onFailureResponse(t, api)
                }
            })

        } else {
            //  onNoInternetConnection(api)
        }
    }

    fun callUpdateProfileAPI(
        userId: String,
        first_name: String,
        last_name: String,
        email: String,
        conf_email: String,
        oldpassword: String,
        password1: String,
        password2: String,
        address1: String,
        address2: String,
        town: String,
        postcode: String,
        country: String,
        telephone: String,
        optin: String,
        choir_name: String,
        api: Int
    ) {
        if (Utility!!.isNetworkAvailable(mContext)) {
            var call = mApiInterface.doUpdateProfile(
                userId,
                first_name,
                last_name,
                email,
                conf_email,
                oldpassword,
                password1,
                password2,
                address1,
                address2,
                town,
                postcode,
                country,
                telephone,
                optin,
                choir_name,
                AppController.appPref.accessToken
            );
            call.enqueue(object : Callback<GenericResponseModel> {
                override fun onResponse(
                    call: Call<GenericResponseModel>,
                    response: Response<GenericResponseModel>
                ) {
                    val mObject = response.body()
                    if (mObject != null) {
                        onResponseSuccess(mObject, api)
                    }
                }

                override fun onFailure(call: Call<GenericResponseModel>, t: Throwable) {
                    onFailureResponse(t, api)
                }
            })

        } else {
            onNoInternetConnection(api)
        }
    }

    fun callGetFreeTrailAPI(api: Int) {
        if (Utility!!.isNetworkAvailable(mContext)) {
            var call = mApiInterface.doGetFreeTrail()
            call.enqueue(object : Callback<FreeTrailModel> {
                override fun onResponse(
                    call: Call<FreeTrailModel>,
                    response: Response<FreeTrailModel>
                ) {
                    val mObject = response.body()
                    if (mObject != null) {
                        onResponseSuccess(mObject, api)
                    }
                }

                override fun onFailure(call: Call<FreeTrailModel>, t: Throwable) {
                    onFailureResponse(t, api)
                }
            })

        } else {
            onNoInternetConnection(api)
        }
    }

    fun callGetSongsListAPI(voiceType: String, api: Int) {
        if (Utility!!.isNetworkAvailable(mContext)) {
            var call = mApiInterface.doGetMusicList(voiceType)
            call.enqueue(object : Callback<SongsModel> {
                override fun onResponse(call: Call<SongsModel>, response: Response<SongsModel>) {
                    val mObject = response.body()
                    if (mObject != null) {
                        onResponseSuccess(mObject, api)
                    }
                }

                override fun onFailure(call: Call<SongsModel>, t: Throwable) {
                    onFailureResponse(t, api)
                }
            })

        } else {
            onNoInternetConnection(api)
        }
    }

    fun callGetComposerListAPI(api: Int) {
        if (Utility!!.isNetworkAvailable(mContext)) {
            var call = mApiInterface.getComposerList(AppController.appPref.composerListTimeStamp)
            call.enqueue(object : Callback<ComposerModel> {
                override fun onResponse(
                    call: Call<ComposerModel>,
                    response: Response<ComposerModel>
                ) {
                    val mObject = response.body()
                    if (mObject != null) {
                        onResponseSuccess(mObject, api)
                    }
                }

                override fun onFailure(call: Call<ComposerModel>, t: Throwable) {
                    onFailureResponse(t, api)
                }
            })

        } else {
            var t = Throwable()

            onFailureResponse(t, api)
            //onNoInternetConnection(api)
        }
    }

    fun callGetChoralWorksAPI(composerName: String, timeStamp: String, api: Int) {
        if (Utility!!.isNetworkAvailable(mContext)) {
            var call = mApiInterface.getChoralWorks(composerName, timeStamp)
            call.enqueue(object : Callback<ChoralWorksModel> {
                override fun onResponse(
                    call: Call<ChoralWorksModel>,
                    response: Response<ChoralWorksModel>
                ) {
                    val mObject = response.body()
                    if (mObject != null) {
                        onResponseSuccess(mObject, api)
                    }
                }

                override fun onFailure(call: Call<ChoralWorksModel>, t: Throwable) {
                    onFailureResponse(t, api)
                }
            })

        } else {
            onNoInternetConnection(api)
        }
    }

    fun callGetSongDetailAPI(songId: String, api: Int) {
        if (Utility!!.isNetworkAvailable(mContext)) {
            var call = mApiInterface.getSongDetail(songId)
            call.enqueue(object : Callback<SongDetailModel> {
                override fun onResponse(
                    call: Call<SongDetailModel>,
                    response: Response<SongDetailModel>
                ) {
                    val mObject = response.body()
                    if (mObject != null) {
                        onResponseSuccess(mObject, api)
                    }
                }

                override fun onFailure(call: Call<SongDetailModel>, t: Throwable) {
                    onFailureResponse(t, api)
                }
            })

        } else {
            onNoInternetConnection(api)
        }
    }

    fun callAddToBasketAPI(songId: String, currencyId: String, userId: String, api: Int) {
        if (Utility!!.isNetworkAvailable(mContext)) {
            var call = mApiInterface.addToBasket(
                songId,
                currencyId,
                userId,
                AppController.appPref.accessToken
            )
            call.enqueue(object : Callback<BasketModel> {
                override fun onResponse(call: Call<BasketModel>, response: Response<BasketModel>) {
                    val mObject = response.body()
                    if (mObject != null) {
                        onResponseSuccess(mObject, api)
                    }
                }

                override fun onFailure(call: Call<BasketModel>, t: Throwable) {
                    onFailureResponse(t, api)
                }
            })

        } else {
            onNoInternetConnection(api)
        }
    }


    fun callFreeGift(songId: String, currencyId: String, userId: String, api: Int) {
        if (Utility!!.isNetworkAvailable(mContext)) {
            var call = mApiInterface.freeGift(
                songId,
                currencyId,
                userId,
                "",
                "0",
                AppController.appPref.accessToken
            )
            call.enqueue(object : Callback<BaseModel> {
                override fun onResponse(call: Call<BaseModel>, response: Response<BaseModel>) {
                    val mObject = response.body()
                    if (mObject != null) {
                        onResponseSuccess(mObject, api)
                    }
                }

                override fun onFailure(call: Call<BaseModel>, t: Throwable) {
                    onFailureResponse(t, api)
                }
            })

        } else {
            onNoInternetConnection(api)
        }
    }


    fun callCheckFreeGift(songId: String, currencyId: String, userId: String, api: Int) {
        if (Utility!!.isNetworkAvailable(mContext)) {
            var call = mApiInterface.checkfreeGift(
                userId,
                songId,
                "abc",
                AppController.appPref.accessToken
            )
            call.enqueue(object : Callback<BaseModel> {
                override fun onResponse(call: Call<BaseModel>, response: Response<BaseModel>) {
                    val mObject = response.body()
                    if (mObject != null) {
                        onResponseSuccess(mObject, api)
                    }
                }

                override fun onFailure(call: Call<BaseModel>, t: Throwable) {
                    onFailureResponse(t, api)
                }
            })

        } else {
            onNoInternetConnection(api)
        }
    }


    fun callDeleteBasketItemAPI(id: String, currencyId: String, userId: String, api: Int) {
        if (Utility!!.isNetworkAvailable(mContext)) {
            var call = mApiInterface.deleteFromBasket(
                id,
                currencyId,
                userId,
                AppController.appPref.accessToken
            )
            call.enqueue(object : Callback<BasketModel> {
                override fun onResponse(call: Call<BasketModel>, response: Response<BasketModel>) {
                    val mObject = response.body()
                    if (mObject != null) {
                        onResponseSuccess(mObject, api)
                    }
                }

                override fun onFailure(call: Call<BasketModel>, t: Throwable) {
                    onFailureResponse(t, api)
                }
            })

        } else {
            onNoInternetConnection(api)
        }
    }

    fun callApplyDiscountcodeAPI(
        discountCode: String,
        currencyId: String,
        userId: String,
        api: Int
    ) {
        if (Utility!!.isNetworkAvailable(mContext)) {
            var call = mApiInterface.applyDiscountCode(
                discountCode,
                currencyId,
                userId,
                AppController.appPref.accessToken
            )
            call.enqueue(object : Callback<BasketModel> {
                override fun onResponse(call: Call<BasketModel>, response: Response<BasketModel>) {
                    val mObject = response.body()
                    if (mObject != null) {
                        onResponseSuccess(mObject, api)
                    }
                }

                override fun onFailure(call: Call<BasketModel>, t: Throwable) {
                    onFailureResponse(t, api)
                }
            })

        } else {
            onNoInternetConnection(api)
        }
    }


    fun callRemoveCoupon(currencyId: String, userId: String, api: Int) {
        if (Utility!!.isNetworkAvailable(mContext)) {
            var call = mApiInterface.removeDiscountCode(
                currencyId,
                userId,
                AppController.appPref.accessToken
            )
            call.enqueue(object : Callback<BasketModel> {
                override fun onResponse(call: Call<BasketModel>, response: Response<BasketModel>) {
                    val mObject = response.body()
                    if (mObject != null) {
                        onResponseSuccess(mObject, api)
                    }
                }

                override fun onFailure(call: Call<BasketModel>, t: Throwable) {
                    onFailureResponse(t, api)
                }
            })

        } else {
            onNoInternetConnection(api)
        }
    }


    fun callGetBasketAPI(userId: String, api: Int) {
        if (Utility!!.isNetworkAvailable(mContext)) {
            var call = mApiInterface.getBasket(userId, AppController.appPref.accessToken)
            call.enqueue(object : Callback<BasketModel> {
                override fun onResponse(call: Call<BasketModel>, response: Response<BasketModel>) {
                    val mObject = response.body()
                    if (mObject != null) {
                        onResponseSuccess(mObject, api)
                    }
                }

                override fun onFailure(call: Call<BasketModel>, t: Throwable) {
                    onFailureResponse(t, api)
                }
            })

        } else {
            onNoInternetConnection(api)
        }
    }


    fun callPurchaseAPI(
        userId: String,
        discountCode: String,
        subTotal: String,
        currencyId: String,
        api: Int
    ) {
        if (Utility!!.isNetworkAvailable(mContext)) {
            var call = mApiInterface.purchase(
                userId,
                discountCode,
                subTotal,
                currencyId,
                AppController.appPref.accessToken
            )
            call.enqueue(object : Callback<PurchaseDataModel> {
                override fun onResponse(
                    call: Call<PurchaseDataModel>,
                    response: Response<PurchaseDataModel>
                ) {
                    val mObject = response.body()
                    if (mObject != null) {
                        onResponseSuccess(mObject, api)
                    }
                }

                override fun onFailure(call: Call<PurchaseDataModel>, t: Throwable) {
                    onFailureResponse(t, api)
                }
            })

        } else {
            onNoInternetConnection(api)
        }
    }


    fun callSubmitOrderAPI(
        userId: String,
        orderId: String,
        transId: String,
        api: Int,
        nonce: String
    ) {
        if (Utility!!.isNetworkAvailable(mContext)) {
            var call = mApiInterface.submitOrder(
                userId,
                orderId,
                transId,
                AppController.appPref.accessToken,
                nonce
            )
            call.enqueue(object : Callback<SubmitOrderModel> {
                override fun onResponse(
                    call: Call<SubmitOrderModel>,
                    response: Response<SubmitOrderModel>
                ) {
                    val mObject = response.body()
                    if (mObject != null) {
                        onResponseSuccess(mObject, api)
                    }
                }

                override fun onFailure(call: Call<SubmitOrderModel>, t: Throwable) {
                    onFailureResponse(t, api)
                }
            })

        } else {
            onNoInternetConnection(api)
        }
    }


    fun callGoogleSubmitOrderAPI(
        userId: String,
        productId: String,
        orderID: String,
        api: Int,
        purcase: Purchase
    ) {
        val gson = Gson()

        if (Utility!!.isNetworkAvailable(mContext)) {

            var call = mApiInterface.submitGoogleOrder(
                userId,
                orderID!!,
                purcase.orderId.toString(),
                AppController.appPref.accessToken,
                productId,
                purcase.purchaseToken
            )
            call.enqueue(object : Callback<SubmitOrderModel> {
                override fun onResponse(
                    call: Call<SubmitOrderModel>,
                    response: Response<SubmitOrderModel>
                ) {
                    val mObject = response.body()
                    Log.e("callGoogleSubmitOrderAPI", Gson()!!.toJson(mObject))
                    if (mObject != null) {
                        onResponseSuccess(mObject, api)

                    }
                }

                override fun onFailure(call: Call<SubmitOrderModel>, t: Throwable) {
                    onFailureResponse(t, api)
                    Log.e("callGoogleSubmitOrderAPI", t.toString())
                }
            })

        } else {
            onNoInternetConnection(api)
            Log.e("callGoogleSubmitOrderAPI", "111")
        }
    }

    fun callGetOrderHistoryAPI(userId: String, api: Int) {
        if (Utility!!.isNetworkAvailable(mContext)) {
            var call = mApiInterface.getOrderHistory(
                userId,
                AppController.appPref.historyListTimeStamp,
                AppController.appPref.accessToken
            )
            call.enqueue(object : Callback<OrderHistoryModel> {
                override fun onResponse(
                    call: Call<OrderHistoryModel>,
                    response: Response<OrderHistoryModel>
                ) {
                    val mObject = response.body()
                    if (mObject != null) {
                        onResponseSuccess(mObject, api)
                    }
                }

                override fun onFailure(call: Call<OrderHistoryModel>, t: Throwable) {
                    onFailureResponse(t, api)
                }
            })

        } else {
            try {
                dialog!!.dismiss()

            } catch (e: Exception) {
                e!!.printStackTrace()
            }
            var t = Throwable()
            mApiListener.onApiFailure(t, api)
            //onFailureResponse(t,api)
            //  onNoInternetConnection(api)
        }
    }

    fun callPurchasedMusicAPI(userId: String, api: Int) {
        if (Utility!!.isNetworkAvailable(mContext)) {
            var call =
                mApiInterface.getPurchasedMusic(userId, "0", AppController.appPref.accessToken)
            call.enqueue(object : Callback<PurchasedMusicModel> {
                override fun onResponse(
                    call: Call<PurchasedMusicModel>,
                    response: Response<PurchasedMusicModel>
                ) {
                    val mObject = response.body()
                    if (mObject != null) {
                        onResponseSuccess(mObject, api)
                    }
                }

                override fun onFailure(call: Call<PurchasedMusicModel>, t: Throwable) {
                    onFailureResponse(t, api)
                }
            })

        } else {
            try {
                dialog!!.dismiss()

            } catch (e: Exception) {
                e!!.printStackTrace()
            }
            var t = Throwable()
            mApiListener.onApiFailure(t, api)
            //  onNoInternetConnection(api)
        }
    }


    fun callContactUsAPI(
        name: String,
        email: String,
        contact_number: String,
        message: String,
        api: Int
    ) {
        if (Utility!!.isNetworkAvailable(mContext)) {
            var call = mApiInterface.doContactUs(name, email, contact_number, message)
            call.enqueue(object : Callback<GenericResponseModel> {
                override fun onResponse(
                    call: Call<GenericResponseModel>,
                    response: Response<GenericResponseModel>
                ) {
                    val mObject = response.body()
                    if (mObject != null) {
                        onResponseSuccess(mObject, api)
                    }
                }

                override fun onFailure(call: Call<GenericResponseModel>, t: Throwable) {
                    onFailureResponse(t, api)
                }
            })

        } else {
            onNoInternetConnection(api)
        }
    }


    fun callGetCountryListAPI(api: Int) {
        if (Utility!!.isNetworkAvailable(mContext)) {
            var call = mApiInterface.getCountryList()
            call.enqueue(object : Callback<CountryModel> {
                override fun onResponse(
                    call: Call<CountryModel>,
                    response: Response<CountryModel>
                ) {
                    val mObject = response.body()
                    if (mObject != null) {
                        onResponseSuccess(mObject, api)
                    }
                }

                override fun onFailure(call: Call<CountryModel>, t: Throwable) {
                    onFailureResponse(t, api)
                }
            })

        } else {
            onNoInternetConnection(api)
        }
    }


}