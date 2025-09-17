package com.choraline

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import com.choraline.utils.Constants
import com.choraline.utils.PreferenceHelper
import com.choraline.utils.Utility


class AboutUsActivity : BaseActivity(), View.OnClickListener {

    private lateinit var context: Context
    var toolbar: Toolbar? = null
    var aboutus_webview: WebView? = null
    var aboutus_progressbar: ProgressBar? = null
    var tootlbar_imgbtnShare: ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us)
        context=this@AboutUsActivity
         toolbar = findViewById(R.id.toolbar)
        aboutus_webview = findViewById(R.id.aboutus_webview)
        aboutus_progressbar = findViewById(R.id.aboutus_progressbar)
        tootlbar_imgbtnShare = findViewById(R.id.tootlbar_imgbtnShare)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        initUI()
    }

    fun initUI()
    {

        //aboutus_webview!!.setWebViewClient(WebViewClient())
        aboutus_webview!!.visibility=View.GONE
        aboutus_progressbar!!.visibility=View.VISIBLE
        this.aboutus_webview!!.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                aboutus_progressbar!!.visibility=View.GONE
                aboutus_webview!!.visibility=View.VISIBLE
            }
        })
        aboutus_webview!!.getSettings().setJavaScriptEnabled(true)
        aboutus_webview!!.loadUrl(Constants.ABOUT_US_URL)
        aboutus_webview!!.setBackgroundColor(Color.parseColor("#919191"));
            aboutus_webview!!.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);

        tootlbar_imgbtnShare!!.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        if(v==tootlbar_imgbtnShare)
        {
            Utility!!.shareApp(context)
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                this.finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

}
