package com.choraline.services

import android.content.Context
import com.braintreepayments.api.ClientTokenCallback
import com.braintreepayments.api.ClientTokenProvider
import com.choraline.models.GetTokeModel
import com.choraline.network.APIClient
import com.choraline.network.APIInterface
import com.choraline.utils.AppController
import com.choraline.utils.Utility
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DemoClientTokenProvider(context: Context) : ClientTokenProvider {
    private val applicationContext: Context
    private var mApiInterface: APIInterface? = null

    init {
        applicationContext = context.applicationContext
        mApiInterface = APIClient.getClient()!!.create(APIInterface::class.java)
    }

    override fun getClientToken(callback: ClientTokenCallback) {

        var call = mApiInterface!!.getToken(AppController.appPref.accessToken)
        call.enqueue(object : Callback<GetTokeModel> {
            override fun onResponse(
                call: Call<GetTokeModel>,
                response: Response<GetTokeModel>
            ) {
                var mObject = response.body()
                if (mObject != null) {
                    callback.onSuccess(mObject.response!!.braintreeToken)
//                    Utility.displayToast(this@BasketActivity, mObject.response!!.braintreeToken)

                }
            }

            override fun onFailure(call: Call<GetTokeModel>, t: Throwable) {
                val errorMessage = "Unable to get a client token."
                callback.onFailure(Exception(errorMessage))

            }
        })


    }
}