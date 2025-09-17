package com.choraline

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.choraline.models.LoginModel
import com.choraline.models.UserData
import com.choraline.network.APIListener
import com.choraline.network.Webservices
import com.choraline.utils.AppController
import com.choraline.utils.Constants
import com.choraline.utils.Utility
import com.google.gson.Gson


class ProfileActivity : BaseActivity(), View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, APIListener{

    private lateinit var context: Context
    private var intents: Intent? = null

    var toolbar : Toolbar? = null
    var tootlbar_imgbtnShare : ImageButton? = null
    var profile_btnEditProfile : Button? = null
    var profile_swipRefreshLayout : SwipeRefreshLayout? = null

    var profile_txtFirstName : EditText? = null
    var profile_txtLastName : EditText? = null
    var profile_txtEmail : EditText? = null
    var profile_txtAddress1 : EditText? = null
    var profile_txtAddress2 : EditText? = null
    var profile_txtTown : EditText? = null
    var profile_txtPostcode : EditText? = null
    var profile_txtCountry : EditText? = null
    var profile_txtTelephone : EditText? = null
    var profile_txtChoir : EditText? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        context=this@ProfileActivity
        toolbar = findViewById(R.id.toolbar)
        profile_txtEmail = findViewById(R.id.profile_txtEmail)
        profile_txtTelephone = findViewById(R.id.profile_txtTelephone)
        profile_txtCountry = findViewById(R.id.profile_txtCountry)
        profile_txtPostcode = findViewById(R.id.profile_txtPostcode)
        profile_txtTown = findViewById(R.id.profile_txtTown)
        profile_txtAddress2 = findViewById(R.id.profile_txtAddress2)
        profile_txtAddress1 = findViewById(R.id.profile_txtAddress1)
        profile_txtLastName = findViewById(R.id.profile_txtLastName)
        profile_txtFirstName = findViewById(R.id.profile_txtFirstName)
        tootlbar_imgbtnShare = findViewById(R.id.tootlbar_imgbtnShare)
        profile_txtChoir = findViewById(R.id.profile_txtChoir)
        profile_btnEditProfile = findViewById(R.id.profile_btnEditProfile)
        profile_swipRefreshLayout = findViewById(R.id.profile_swipRefreshLayout)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        initUI()
    }

    fun initUI()
    {
        tootlbar_imgbtnShare!!.setOnClickListener(this)
        profile_swipRefreshLayout!!.setOnRefreshListener(this)
        profile_btnEditProfile!!.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        setData()
    }

    fun setData()
    {
        var gson=Gson()
        val data=gson!!.fromJson(AppController.appPref.userData, UserData::class.java)

        profile_txtFirstName!!.setText(data.firstname)
        profile_txtLastName!!.setText(data.lastname)
        profile_txtEmail!!.setText(data.email)
        profile_txtAddress1!!.setText(data.addess1)
        profile_txtAddress2!!.setText(data.addess2)
        profile_txtTown!!.setText(data.town)
        profile_txtPostcode!!.setText(data.postcode)
        profile_txtCountry!!.setText(data.country)
        profile_txtTelephone!!.setText(data.telephone)
        profile_txtChoir!!.setText(data.choir)

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
