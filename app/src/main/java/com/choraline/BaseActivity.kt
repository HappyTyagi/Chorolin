package com.choraline

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import com.choraline.models.SongsData
import com.choraline.network.APIListener
import com.choraline.utils.AppController
import com.choraline.utils.Constants
import java.io.File

open class BaseActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

    }

    override fun onStart() {
        super.onStart()
        Constants!!.IS_APP_RUNNING=true
    }

    override fun onDestroy() {
        super.onDestroy()
        Constants!!.IS_APP_RUNNING=false
    }







}
