package com.choraline

import android.content.Context
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

class FAQActivity : BaseActivity(), View.OnClickListener {


    var  toolbar: Toolbar? = null
    var  faq_webview: WebView? = null
    var  faq_progressbar: ProgressBar? = null
    var  tootlbar_imgbtnShare: ImageButton? = null
    private lateinit var context: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faq)
        context=this@FAQActivity
        faq_progressbar = findViewById(R.id.faq_progressbar)
        tootlbar_imgbtnShare = findViewById(R.id.tootlbar_imgbtnShare)
        toolbar = findViewById(R.id.toolbar)
        faq_webview = findViewById(R.id.faq_webview)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        initUI()
    }

    fun initUI()
    {
        //faq_webview!!.setWebViewClient(WebViewClient())
        faq_webview!!.visibility=View.GONE
        faq_progressbar!!.visibility=View.VISIBLE
        this.faq_webview!!.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                faq_progressbar!!.visibility=View.GONE
                faq_webview!!.visibility=View.VISIBLE
            }
        })
        faq_webview!!.getSettings().setJavaScriptEnabled(true)
        faq_webview!!.loadUrl(Constants.FAQ_URL)

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
