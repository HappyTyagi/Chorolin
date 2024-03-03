package com.choraline

import android.content.Context
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.choraline.utils.Constants
import com.choraline.utils.Utility

import kotlinx.android.synthetic.main.activity_terms_and_conditions.*

class TermsAndConditionsActivity : BaseActivity(), View.OnClickListener {

    private lateinit var context: Context


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_and_conditions)
        context=this@TermsAndConditionsActivity
        val toolbar = findViewById<Toolbar>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        initUI()
    }

    fun initUI()
    {
        //termsandconditions_webview!!.setWebViewClient(WebViewClient())
        termsandconditions_webview!!.visibility=View.GONE
        termsandconditions_progressbar!!.visibility=View.VISIBLE
        this.termsandconditions_webview.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                termsandconditions_progressbar!!.visibility=View.GONE
                termsandconditions_webview!!.visibility=View.VISIBLE
            }
        })
        termsandconditions_webview!!.getSettings().setJavaScriptEnabled(true)

            termsandconditions_webview!!.loadUrl(Constants.TERMS_AND_CONDITIONS_URL)


        tootlbar_imgbtnShare.setOnClickListener(this)
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
