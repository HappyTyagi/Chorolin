package com.choraline

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageButton
import android.widget.ProgressBar
import com.choraline.utils.Constants
import com.choraline.utils.Utility

class PrivacyPlicyActivity : AppCompatActivity(),View.OnClickListener {

    private lateinit var context: Context
    var termsandconditions_webview : WebView? = null
    var tootlbar_imgbtnShare : ImageButton? = null
    var termsandconditions_progressbar : ProgressBar? = null
    var toolbar : Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_plicy)

        context=this@PrivacyPlicyActivity
        tootlbar_imgbtnShare = findViewById(R.id.tootlbar_imgbtnShare)
        toolbar = findViewById(R.id.toolbar)
        termsandconditions_webview = findViewById(R.id.termsandconditions_webview)
        termsandconditions_progressbar = findViewById(R.id.termsandconditions_progressbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        initUI()
    }

    fun initUI()
    {
        //termsandconditions_webview!!.setWebViewClient(WebViewClient())
        termsandconditions_webview!!.visibility= View.GONE
        termsandconditions_progressbar!!.visibility= View.VISIBLE
        this.termsandconditions_webview!!.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                termsandconditions_progressbar!!.visibility= View.GONE
                termsandconditions_webview!!.visibility= View.VISIBLE
            }
        })
        termsandconditions_webview!!.getSettings().setJavaScriptEnabled(true)

        termsandconditions_webview!!.loadUrl(Constants.PRIVACY_POLICY_URL)


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
