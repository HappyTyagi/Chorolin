package com.choraline

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import android.view.MenuItem
import android.view.View
import com.choraline.utils.AppController
import com.choraline.utils.Utility
import kotlinx.android.synthetic.main.activity_thank_you.*

class ThankYouActivity : BaseActivity(), View.OnClickListener {

    private lateinit var context: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thank_you)
        context=this@ThankYouActivity
        val toolbar = findViewById<Toolbar>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        initUI()
    }

    fun initUI()
    {
        tootlbar_imgbtnShare!!.setOnClickListener(this)
        thankyou_btnGoToPurchase!!.setOnClickListener(this)
        thankyou_btnContinueShopping!!.setOnClickListener(this)
        thankyou_btnLogout!!.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        if(v==tootlbar_imgbtnShare)
        {
            Utility!!.shareApp(context)
        }
        else if(v==thankyou_btnGoToPurchase)
        {
            var i = Intent(context, PurchasedMusicActivity::class.java)
            startActivity(i)
            finish()
        }
        else if(v==thankyou_btnContinueShopping)
        {
            finish()
        }
        else if(v==thankyou_btnLogout)
        {
            finishAffinity()
            AppController.appPref.isLogin=false
            var i = Intent(context, WelcomActivity::class.java)
            startActivity(i)
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
