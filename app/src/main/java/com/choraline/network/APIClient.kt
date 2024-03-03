package com.choraline.network

import com.choraline.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by deepak Tyagi on 7/10/2017.
 */
object APIClient {

    var retrofit: Retrofit? = null

    fun getClient(): Retrofit?
    {
        if (retrofit === null) {
            retrofit = Retrofit.Builder()
                    .client(getOkHTTPClient())
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }
        return retrofit
    }

    fun getOkHTTPClient() : OkHttpClient
    {
        var logging= HttpLoggingInterceptor()
        logging!!.setLevel(HttpLoggingInterceptor.Level.BODY);
        var httpClient = OkHttpClient.Builder().connectTimeout(5000, TimeUnit.MINUTES).
        readTimeout(5000, TimeUnit.MINUTES).
                writeTimeout(5000, TimeUnit.MINUTES)
        return httpClient!!.addInterceptor(logging).build()
    }

}