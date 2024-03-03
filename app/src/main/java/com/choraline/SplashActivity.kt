package com.choraline

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.choraline.models.BasketModel
import com.choraline.models.CountryModel
import com.choraline.models.LoginModel
import com.choraline.network.APIListener
import com.choraline.network.Webservices
import com.choraline.utils.AppController
import com.choraline.utils.Constants
import com.choraline.utils.Utility
import com.google.gson.Gson

class SplashActivity : BaseActivity(), APIListener {


    lateinit var context : Context
    private var handler: Handler? = null
    private var runnable: Runnable? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        context = this



        var appSpecificInternalStorageDirectory = context.filesDir
        appSpecificInternalStorageDirectory.delete()



        var strCountry = AppController!!.appPref!!.countryData
        if(strCountry.toString().equals(""))
        {
            getCountry()
        }
        init()
    }

    /**
     * Initialize data
     */
    private fun init() {
        handler = Handler()
        runnable = Runnable {
                if (AppController.appPref.isLogin) {
                    getProgile()
                    getBasket()
                    var i=Intent(applicationContext, HomeActivity::class.java)
                    startActivity(i)
                    finish()
                } else {
                    var i=Intent(applicationContext, WelcomActivity::class.java)
                    startActivity(i)
                    finish()
                }

        }
        handler!!.postDelayed(runnable!!, 2500)
    }

    fun getCountry()
    {
        Webservices(context, this, false, "").callGetCountryListAPI(Constants.API_GET_COUNTRY_LIST);
    }

    fun getProgile()
    {
        Webservices(context, this, false, "").callGetProfileAPI(AppController.appPref.userId, "", "Android",
                Constants.API_GET_PROFILE);
    }

    fun getBasket()
    {
        Webservices(context, this, false, "").callGetBasketAPI(AppController.appPref.userId, Constants.API_GET_BASKET);
    }


    override fun onApiSuccess(obj: Any, api: Int) {

        if(api==Constants.API_GET_COUNTRY_LIST)
        {
            var result= obj as CountryModel
            if(result.status)
            {
                val gson = Gson()
                var gsonOutput = gson.toJson(result)
                AppController.appPref.countryData=gsonOutput
            }
        }
        else if(api==Constants.API_GET_PROFILE)
        {
            val any = Array<CountryModel?>(100){null}
            any[1]= CountryModel()
            var result= obj as LoginModel
            if(result.status)
            {
                val gson = Gson()
                var gsonOutput = gson.toJson(result.response)
                AppController.appPref.userData=gsonOutput
                AppController.appPref.userId=result!!.response!!.userId
                AppController.appPref.userName=result!!.response!!.firstname+" "+result!!.response!!.lastname
                AppController.appPref.token=result!!.response!!.token
            }
        }
        else if(api==Constants.API_GET_BASKET)
        {
            val result=obj as BasketModel
            if(result!=null)
            {
                if(result!!.status)
                {
                    AppController!!.appPref!!.basketData= Gson()!!.toJson(result)

                }
                else
                {
                    AppController!!.appPref!!.basketData= ""
                }

            }
        }
    }

    override fun onApiFailure(throwable: Throwable,api: Int) {

    }



    override fun onBackPressed() {

        super.onBackPressed()
        handler!!.removeCallbacks(runnable!!)
    }
}
