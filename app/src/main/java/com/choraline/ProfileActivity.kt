package com.choraline

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import android.view.MenuItem
import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.choraline.models.LoginModel
import com.choraline.models.UserData
import com.choraline.network.APIListener
import com.choraline.network.Webservices
import com.choraline.utils.AppController
import com.choraline.utils.Constants
import com.choraline.utils.Utility
import com.google.gson.Gson

import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : BaseActivity(), View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, APIListener{

    private lateinit var context: Context
    private var intents: Intent? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        context=this@ProfileActivity
        val toolbar = findViewById<Toolbar>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        initUI()
    }

    fun initUI()
    {
        tootlbar_imgbtnShare.setOnClickListener(this)
        profile_swipRefreshLayout!!.setOnRefreshListener(this)
        profile_btnEditProfile.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        setData()
    }

    fun setData()
    {
        var gson=Gson()
        val data=gson!!.fromJson(AppController.appPref.userData, UserData::class.java)

        profile_txtFirstName.setText(data.firstname)
        profile_txtLastName.setText(data.lastname)
        profile_txtEmail.setText(data.email)
        profile_txtAddress1.setText(data.addess1)
        profile_txtAddress2.setText(data.addess2)
        profile_txtTown.setText(data.town)
        profile_txtPostcode.setText(data.postcode)
        profile_txtCountry.setText(data.country)
        profile_txtTelephone.setText(data.telephone)
        profile_txtChoir.setText(data.choir)

    }

    override fun onClick(v: View?) {


        if(v==tootlbar_imgbtnShare)
        {
            Utility!!.shareApp(context)
        }
        else if(v==profile_btnEditProfile)
        {
            intents = Intent(context, EditProfileActivity::class.java)
            startActivity(intents)
        }
    }

    override fun onRefresh() {

        getProgile()
    }

    fun getProgile()
    {
        Webservices(context, this, false, "").callGetProfileAPI(AppController.appPref.userId, "", "Android",
                Constants.API_GET_PROFILE);
    }

    override fun onApiSuccess(obj: Any, api: Int) {

        if(api==Constants.API_GET_PROFILE)
        {
            if(profile_swipRefreshLayout!!.isRefreshing)
                profile_swipRefreshLayout!!.isRefreshing=false
            var result= obj as LoginModel
            if(result.status)
            {
                if(result!!.response!=null)
                {
                    val gson = Gson()
                    var gsonOutput = gson.toJson(result.response)
                    AppController.appPref.userData=gsonOutput
                    AppController.appPref.userId=result!!.response!!.userId
                    AppController.appPref.userName=result!!.response!!.firstname+" "+result!!.response!!.lastname
                    AppController.appPref.token=result!!.response!!.token
                    setData()
                }

            }
        }
    }

    override fun onApiFailure(throwable: Throwable,api: Int) {

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
