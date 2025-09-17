package com.choraline

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RelativeLayout
import com.choraline.models.*
import com.choraline.network.APIListener
import com.choraline.network.Webservices
import com.choraline.utils.AppController
import com.choraline.utils.Constants
import com.choraline.utils.Utility
import com.google.gson.Gson


class EditProfileActivity : BaseActivity(), View.OnClickListener, APIListener{

    private lateinit var context: Context
    private lateinit var data: UserData
    var countryList = ArrayList<CountryData>()
    var selectedCountryId: String = ""
    var  toolbar: Toolbar? = null


    var  edtprofile_edtFirstName: EditText? = null
    var  edtprofile_edtLastName: EditText? = null
    var  edtprofile_edtEmail: EditText? = null
    var  edtprofile_edtAddress1: EditText? = null
    var  edtprofile_edtAddress2: EditText? = null
    var  edtprofile_edtTown: EditText? = null
    var  edtprofile_edtPostCode: EditText? = null
    var  edtprofile_edtCountry: EditText? = null
    var  edtprofile_edtConfirmEmail: EditText? = null
    var  edtprofile_edtTelephone: EditText? = null
    var  edtprofile_edtNameOfYourChoir: EditText? = null

    var  tootlbar_imgbtnShare: ImageButton? = null
    var  edtprofile_OldPassword: EditText? = null
    var  edtprofile_btnSubmit: EditText? = null



    var  edtprofile_edtConfirmPassword: EditText? = null
    var  edtprofile_edtPassword: EditText? = null
    var  layoutParent: RelativeLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        context=this@EditProfileActivity
         toolbar = findViewById(R.id.toolbar)

        edtprofile_edtConfirmPassword = findViewById(R.id.edtprofile_edtConfirmPassword)
        edtprofile_OldPassword = findViewById(R.id.edtprofile_OldPassword)
        edtprofile_edtPassword = findViewById(R.id.edtprofile_edtPassword)
        edtprofile_edtFirstName = findViewById(R.id.edtprofile_edtFirstName)
        edtprofile_edtLastName = findViewById(R.id.edtprofile_edtLastName)
        edtprofile_edtEmail = findViewById(R.id.edtprofile_edtEmail)
        edtprofile_edtAddress1 = findViewById(R.id.edtprofile_edtAddress1)
        edtprofile_edtAddress2 = findViewById(R.id.edtprofile_edtAddress2)
        edtprofile_edtTown = findViewById(R.id.edtprofile_edtTown)
        edtprofile_edtPostCode = findViewById(R.id.edtprofile_edtPostCode)
        edtprofile_edtCountry = findViewById(R.id.edtprofile_edtCountry)
        edtprofile_edtTelephone = findViewById(R.id.edtprofile_edtTelephone)
        edtprofile_edtNameOfYourChoir = findViewById(R.id.edtprofile_edtNameOfYourChoir)
        tootlbar_imgbtnShare = findViewById(R.id.tootlbar_imgbtnShare)
        edtprofile_btnSubmit = findViewById(R.id.edtprofile_btnSubmit)
        layoutParent = findViewById(R.id.layoutParent)
        edtprofile_edtConfirmEmail = findViewById(R.id.edtprofile_edtConfirmEmail)



        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        var strCountry = AppController!!.appPref!!.countryData
        if(!strCountry.equals(""))
        {
            var model=Gson()!!.fromJson(strCountry, CountryModel::class.java)
            if(model!!.response!=null)
            {
                if(model!!.response!!.countryList!=null && model!!.response!!.countryList!!.size>0)
                {
                    countryList!!.addAll(model!!.response!!.countryList)
                }
                else
                    getCountry()
            }
            else
                getCountry()
        }
        else
        {
            getCountry()
        }

        initUI()
    }

    fun initUI()
    {
        setData()

        tootlbar_imgbtnShare!!.setOnClickListener(this)
        edtprofile_edtCountry!!.setOnClickListener(this)
        edtprofile_btnSubmit!!.setOnClickListener(this)
    }


    fun setData()
    {
        var gson= Gson()
        data=gson!!.fromJson(AppController.appPref.userData, UserData::class.java)



        edtprofile_edtFirstName!!.setText(data.firstname)
        edtprofile_edtLastName!!.setText(data.lastname)
        edtprofile_edtEmail!!.setText(data.email)
        edtprofile_edtAddress1!!.setText(data.addess1)
        edtprofile_edtAddress2!!.setText(data.addess2)
        edtprofile_edtTown!!.setText(data.town)
        edtprofile_edtPostCode!!.setText(data.postcode)
        edtprofile_edtCountry!!.setText(data.country)
        edtprofile_edtTelephone!!.setText(data.telephone)
        edtprofile_edtNameOfYourChoir!!.setText(data.choir)

    }

    override fun onClick(v: View?) {
        Utility!!.hideSoftKeyboard(this)
        if(v==tootlbar_imgbtnShare)
        {
            Utility!!.shareApp(context)
        }
        else if(v==edtprofile_edtCountry)
        {
            openCountrySpinner()
        }
        else if(v==edtprofile_btnSubmit)
        {
            if(validate())
            {
                updateProfile()
            }
        }
    }

    fun validate() : Boolean
    {
        if(edtprofile_edtFirstName!!.text.toString().equals(""))
        {
            Utility!!.showSnakeBar(layoutParent!!, "Enter Your First Name.")
            edtprofile_edtFirstName!!.requestFocus()
            return false
        }
        if(edtprofile_edtLastName!!.text.toString().equals(""))
        {
            Utility!!.showSnakeBar(layoutParent!!, "Enter Your Last Name.")
            edtprofile_edtLastName!!.requestFocus()
            return false
        }
        if(edtprofile_edtEmail!!.text.toString().equals(""))
        {
            Utility!!.showSnakeBar(layoutParent!!, "Enter your Email.")
            edtprofile_edtEmail!!.requestFocus()
            return false
        }
        if(!Utility!!.isValidEmail(edtprofile_edtEmail!!.text!!.toString()))
        {
            Utility!!.showSnakeBar(layoutParent!!, "Enter a valid Email.")
            edtprofile_edtEmail!!.requestFocus()
            return false
        }
        if(!edtprofile_edtEmail!!.text.toString().equals(data!!.email))
        {
            if(edtprofile_edtConfirmEmail!!.text.toString().equals(""))
            {
                Utility!!.showSnakeBar(layoutParent!!, "Please check that you have entered your Email address correctly")
                edtprofile_edtEmail!!.requestFocus()
                return false
            }
            else if(!edtprofile_edtConfirmEmail!!.text.toString().equals(edtprofile_edtEmail!!.text!!.toString()))
            {
                Utility!!.showSnakeBar(layoutParent!!, "Please check that you have enter wrong confirm Email.")
                edtprofile_edtEmail!!.requestFocus()
                return false
            }

        }
        if(edtprofile_edtEmail!!.text.toString().equals(data!!.email) && !edtprofile_edtConfirmEmail!!.text.toString().equals("")
                && !edtprofile_edtConfirmEmail!!.text.toString().equals(data!!.email))
        {
            Utility!!.showSnakeBar(layoutParent!!, "Please check that you have enter wrong confirm Email.")
            edtprofile_edtEmail!!.requestFocus()
            return false
        }



        if(!(edtprofile_edtEmail!!.text.toString().equals(data!!.email) && !edtprofile_edtConfirmEmail!!.text.toString().equals("")
                && !edtprofile_edtConfirmEmail!!.text.toString().equals(data!!.email)))
        {
            if (edtprofile_OldPassword!!.text.toString().isEmpty()) {
                Utility!!.showSnakeBar(layoutParent!!, "Please enter current password.")
                return false
            }

        }



        if (edtprofile_edtPassword!!.text!!.toString().length>0) {
            if (edtprofile_OldPassword!!.text.toString().isEmpty()) {
                Utility!!.showSnakeBar(layoutParent!!, "Current Password can not be less than 6 digits.")
                return false
            }
        }




        if(!edtprofile_edtPassword!!.text!!.toString().equals(""))
        {
            if(edtprofile_edtPassword!!.text!!.toString()!!.length<6)
            {
                Utility!!.showSnakeBar(layoutParent!!, "Password can not be less than 6 digits.")
                edtprofile_edtPassword!!.requestFocus()
                return false
            }
            if(edtprofile_edtConfirmPassword!!.text!!.toString().equals(""))
            {
                Utility!!.showSnakeBar(layoutParent!!, "Please re-enter Password to confirm.")
                edtprofile_edtConfirmPassword!!.requestFocus()
                return false
            }
            if(!edtprofile_edtPassword!!.text!!.toString().equals(edtprofile_edtConfirmPassword!!.text!!.toString()))
            {
                Utility!!.showSnakeBar(layoutParent!!, "Confirm Password does not match.")
                edtprofile_edtPassword!!.requestFocus()
                return false
            }
        }
        if(edtprofile_edtPassword!!.text!!.toString().equals("") && !edtprofile_edtConfirmPassword!!.text!!.toString().equals(""))
        {
            Utility!!.showSnakeBar(layoutParent!!, "Please enter Password first")
            edtprofile_edtPassword!!.requestFocus()
            return false
        }


        if(!edtprofile_edtTelephone!!.text.toString().equals(""))
        {
            if(edtprofile_edtTelephone!!.text.toString().length<7)
            {
                Utility!!.showSnakeBar(layoutParent!!, "Telephone should not be less than 7 digits")
                edtprofile_edtTelephone!!.requestFocus()
                return false
            }
        }
        return true
    }

    fun getCountry()
    {
        Webservices(context, this, true, "Please wait...").callGetCountryListAPI(Constants.API_GET_COUNTRY_LIST);
    }

    fun updateProfile()
    {
        Webservices(context, this, true, "Please wait...")
                .callUpdateProfileAPI(AppController!!.appPref!!.userId,
                edtprofile_edtFirstName!!.text!!.toString(),
                        edtprofile_edtLastName!!.text!!.toString(),
                edtprofile_edtEmail!!.text!!.toString(),
                        edtprofile_edtConfirmEmail!!.text!!.toString(),
                        edtprofile_OldPassword!!.text.toString(),
                edtprofile_edtPassword!!.text!!.toString(),
                edtprofile_edtConfirmPassword!!.text!!.toString(), edtprofile_edtAddress1!!.text!!.toString(),
                edtprofile_edtAddress2!!.text!!.toString(), edtprofile_edtTown!!.text!!.toString(),
                edtprofile_edtPostCode!!.text!!.toString(), edtprofile_edtCountry!!.text!!.toString(),
                edtprofile_edtTelephone!!.text!!.toString(), "optin",
                edtprofile_edtNameOfYourChoir!!.text!!.toString(), Constants!!.API_UPDATE_PROFILE)
    }

    fun getProfile()
    {
        Webservices(context, this, true, "Please wait...").callGetProfileAPI(AppController!!.appPref!!.userId,
                "", "Android", Constants!!.API_GET_PROFILE)
    }

    private fun openCountrySpinner() {
        if(countryList!=null) {
            if (countryList.size > 0) {
                val expcount = countryList.size
                val arr = arrayOfNulls<String>(countryList.size)
                for (i in 0..expcount - 1) {
                    arr[i] = countryList.get(i)!!.name
                }

                val expDialogListener = DialogInterface.OnClickListener { dialog, which ->
                    selectedCountryId = countryList!!.get(which)!!.id
                    edtprofile_edtCountry!!.setText(countryList!!.get(which)!!.name)

                }

                val expbuilder = AlertDialog.Builder(context)
                expbuilder.setTitle("Select Country")
                expbuilder.setItems(arr, expDialogListener)
                val expdialog = expbuilder.create()
                expdialog.show()
            }
        }

    }

    override fun onApiSuccess(obj: Any, api: Int) {

        if(api==Constants.API_GET_COUNTRY_LIST)
        {
            var result= obj as CountryModel
            if(result.status)
            {
                if(result!!.response!=null)
                {
                    if(result!!.response!!.countryList!=null && result!!.response!!.countryList!!.size>0)
                    {
                        countryList!!.addAll(result!!.response!!.countryList)
                    }
                }
                val gson = Gson()
                var gsonOutput = gson.toJson(result)
                AppController.appPref.countryData=gsonOutput
            }
            else
            {
                Utility!!.showMessageDialog(context, result!!.message)
            }
        }
        else if(api == Constants!!.API_UPDATE_PROFILE)
        {
            var result = obj as GenericResponseModel
            if(result!!.status)
            {
                if(result!!.message.contains("Your email address has been updated successfully", true))
                {
                    showMessageDialog(context, result.message)
                } else
                {
                    //getProfile()
                    showMessageDialog1(context, result.message)
                }

            }
            else
            {
                Utility!!.showSnakeBar(layoutParent!!, result!!.message)
            }
        }
        if(api==Constants.API_GET_PROFILE)
        {
            var result= obj as LoginModel
            if(result.status)
            {
                val gson = Gson()
                var gsonOutput = gson.toJson(result.response)
                AppController.appPref.userData=gsonOutput
                AppController.appPref.userId=result!!.response!!.userId
                AppController.appPref.userName=result!!.response!!.firstname+" "+result!!.response!!.lastname
                AppController.appPref.token=result!!.response!!.token
                val i = Intent(context, ProfileActivity::class.java)
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                //i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(i)
                finish()
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

    fun showMessageDialog(mContext: Context?, msg: String?) {
        val myAlertDialog = AlertDialog.Builder(mContext)
        myAlertDialog.setIcon(R.mipmap.app_icon)
        myAlertDialog.setTitle("ChoraLine")
        myAlertDialog.setMessage(msg)
        myAlertDialog.setCancelable(false)
        myAlertDialog.setPositiveButton("OK") { dialog, which ->
            AppController.appPref.isLogin=false
            AppController.appPref.clearSharedPreference()
            var intents = Intent(context, WelcomActivity::class.java)
            startActivity(intents)
            finishAffinity()
        }
        val dialog = myAlertDialog.create()
        dialog.show()
    }

    fun showMessageDialog1(mContext: Context?, msg: String?) {
        val myAlertDialog = AlertDialog.Builder(mContext)
        myAlertDialog.setIcon(R.mipmap.app_icon)
        myAlertDialog.setTitle("ChoraLine")
        myAlertDialog.setMessage(msg)
        myAlertDialog.setCancelable(false)
        myAlertDialog.setPositiveButton("OK") { dialog, which ->
           getProfile()
        }
        val dialog = myAlertDialog.create()
        dialog.show()
    }
}
