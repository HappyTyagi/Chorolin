package com.choraline

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import android.text.InputFilter
import android.text.Spanned
import android.view.MenuItem
import android.view.View
import com.choraline.models.GenericResponseModel
import com.choraline.models.UserData
import com.choraline.network.APIListener
import com.choraline.network.Webservices
import com.choraline.utils.AppController
import com.choraline.utils.Constants
import com.choraline.utils.Utility
import com.google.gson.Gson

import kotlinx.android.synthetic.main.activity_contact_us.*

class ContactUsActivity : BaseActivity(), View.OnClickListener, APIListener{

    private lateinit var context: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_us)
        context = this
       // val toolbar = findViewById<Toolbar>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        initUI()
    }

    fun initUI()
    {
        contactus_edtName!!.filters= arrayOf(filter)
        if(AppController!!.appPref!!.isLogin)
        {
            var gson= Gson()
            val data=gson!!.fromJson(AppController.appPref.userData, UserData::class.java)
            contactus_edtName!!.setText(data!!.firstname+" "+data!!.lastname)
            contactus_edtEmail!!.setText(data!!.email)
            contactus_edtContactNumber!!.setText(data!!.telephone)

        }
        tootlbar_imgbtnShare.setOnClickListener(this)
        contactus_btnSubmit.setOnClickListener(this)
    }

    internal var filter: InputFilter = InputFilter { source, start, end, dest, dstart, dend ->

        for (i in start..end - 1) {
            if (source.toString().toCharArray().get(i) === ' ') {

            } else if (!Character.isLetter(source.toString().toCharArray().get(i))) {
                return@InputFilter ""
            }
        }
        null
    }

    override fun onClick(v: View?) {
        Utility!!.hideSoftKeyboard(this)
        if(v==tootlbar_imgbtnShare)
        {
            Utility!!.shareApp(context)
        }
        else if(v==contactus_btnSubmit)
        {
            if(validate())
            {
                doContactUs()
            }
        }
    }

    fun validate() : Boolean
    {
        if(contactus_edtName.text.toString().equals(""))
        {
            Utility.showSnakeBar(layoutParent, "Enter your Name")
            return false
        }
        if(contactus_edtEmail.text.toString().equals(""))
        {
            Utility.showSnakeBar(layoutParent, "Enter your Email.")
            return false
        }
        if(!Utility.isValidEmail(contactus_edtEmail.text.toString()))
        {
            Utility.showSnakeBar(layoutParent, "Enter the valid Email Id.")
            return false
        }
        if(!contactus_edtContactNumber.text.toString().equals(""))
        {
            if(contactus_edtContactNumber.text.toString().length<7)
            {
                Utility.showSnakeBar(layoutParent, "Contact Number should have atleast 7 digits.")
                return false
            }
        }
        if(contactus_edtLeaveYourMessage.text.toString().equals(""))
        {
            Utility.showSnakeBar(layoutParent, "Enter your Message.")
            return false
        }
        if(contactus_edtLeaveYourMessage.text.toString().length>400)
        {
            Utility.showSnakeBar(layoutParent, "Your message should not be greater than 400 characters.")
            return false
        }
        return  true
    }

    fun doContactUs()
    {
        Webservices(context, this, true, "Please wait...").callContactUsAPI(contactus_edtName.text.toString(),
                contactus_edtEmail.text.toString(), contactus_edtContactNumber.text.toString(), contactus_edtLeaveYourMessage.text.toString(),
                Constants.API_CONTACT_US)
    }

    override fun onApiSuccess(obj: Any, api: Int) {

        if(api==Constants.API_CONTACT_US)
        {
            val result=obj as GenericResponseModel
            if(result.status)
            {
                if(AppController!!.appPref!!.isLogin)
                {
                    contactus_edtLeaveYourMessage.setText("")

                }
                else {
                    contactus_edtName.setText("")
                    contactus_edtEmail.setText("")
                    contactus_edtContactNumber.setText("")
                    contactus_edtLeaveYourMessage.setText("")
                }
                Utility.showMessageDialog(context, result.message);
            }
            else
                Utility.showMessageDialog(context, result.message);
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
