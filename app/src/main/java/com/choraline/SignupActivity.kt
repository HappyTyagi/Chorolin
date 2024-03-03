package com.choraline

import android.content.Context

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import android.text.InputFilter
import android.view.MenuItem
import android.view.View
import com.choraline.models.LoginModel
import com.choraline.network.APIListener
import com.choraline.network.Webservices
import com.choraline.utils.Constants
import com.choraline.utils.Utility

import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : BaseActivity(), View.OnClickListener, APIListener {

    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        context = this@SignupActivity
        val toolbar = findViewById<Toolbar>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        initUI()
    }

    fun initUI()
    {
        signup_edtFirstName!!.filters= arrayOf(filter)
        signup_edtLastName!!.filters= arrayOf(filter)
        //signup_edtNameOfYourChoir!!.filters= arrayOf(filter)
        tootlbar_imgbtnShare.setOnClickListener(this)
        signup_btnSubmit.setOnClickListener(this)
    }

    internal var filter: InputFilter = InputFilter { source, start, end, dest, dstart, dend ->

        for (i in start until end) {
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
        else if(v==signup_btnSubmit)
        {
            if(validate())
            {
                doSignup()

            }
        }
    }

    fun validate() : Boolean
    {
        if(signup_edtFirstName.text.toString().equals(""))
        {
            Utility!!.showSnakeBar(layoutParent, "Enter Your First Name.")
            signup_edtFirstName.requestFocus()
            return false
        }
        if(signup_edtLastName.text.toString().equals(""))
        {
            Utility!!.showSnakeBar(layoutParent, "Enter Your Last Name.")
            signup_edtLastName.requestFocus()
            return false
        }
        if(signup_edtEmail.text.toString().equals(""))
        {
            Utility!!.showSnakeBar(layoutParent, "Enter Your Email.")
            signup_edtEmail.requestFocus()
            return false
        }
        if(!Utility!!.isValidEmail(signup_edtEmail.text.toString()))
        {
            Utility!!.showSnakeBar(layoutParent, "Enter a Valid Email.")
            signup_edtEmail.requestFocus()
            return false
        }
        if(signup_edtConfirmEmail.text.toString().equals(""))
        {
            Utility!!.showSnakeBar(layoutParent, "Re-Enter Email To Confirm.")
            signup_edtConfirmEmail.requestFocus()
            return false
        }
        if(!Utility!!.isValidEmail(signup_edtConfirmEmail.text.toString()))
        {
            Utility!!.showSnakeBar(layoutParent, "Enter a Valid Confirm Email.")
            signup_edtConfirmEmail.requestFocus()
            return false
        }
        if(signup_edtPassword.text.toString().equals(""))
        {
            Utility!!.showSnakeBar(layoutParent, "Enter Password.")
            signup_edtPassword.requestFocus()
            return false
        }
        if(signup_edtPassword.text.toString().length<6)
        {
            Utility!!.showSnakeBar(layoutParent, "Your password should contain at least 6 characters.")
            signup_edtPassword.requestFocus()
            return false
        }
        if(signup_edtConfirmPassword.text.toString().equals(""))
        {
            Utility!!.showSnakeBar(layoutParent, "Re-Enter Password To Confirm .")
            signup_edtConfirmPassword.requestFocus()
            return false
        }
        if(!signup_edtConfirmPassword.text.toString().equals(signup_edtPassword.text.toString()))
        {
            Utility!!.showSnakeBar(layoutParent, "Confirm Password doesn't match.")
            signup_edtConfirmPassword.requestFocus()
            return false
        }

        return true
    }

    fun doSignup()
    {
        Webservices(context, this, true, "Signing up...").callSignupAPI(signup_edtFirstName.text.toString(),
                signup_edtLastName.text.toString(), signup_edtEmail.text.toString(), signup_edtEmail.text.toString(),
                signup_edtPassword.text.toString(), signup_edtPassword.text.toString(),
                signup_edtNameOfYourChoir.text.toString(), "", "Android", Constants.API_SIGNUP)
    }

    override fun onApiSuccess(obj: Any, api: Int) {

        when(api)
        {
            Constants.API_SIGNUP ->
            {
                var result= obj as LoginModel
                if(result.status)
                {
                    /*val gson = Gson()
                    var gsonOutput = gson.toJson(result.response)
                    AppController.appPref.userData=gsonOutput
                    AppController.appPref.isLogin=true
                    AppController.appPref.userId=result!!.response!!.userId
                    AppController.appPref.userName=result!!.response!!.firstname+" "+result!!.response!!.lastname
                    AppController.appPref.token=result!!.response!!.token
                    var i= Intent(context, HomeActivity::class.java)
                    startActivity(i)
                    finishAffinity()*/
                    signup_edtFirstName.setText("")
                    signup_edtLastName.setText("")
                    signup_edtEmail.setText("")
                    signup_edtConfirmEmail.setText("")
                    signup_edtPassword.setText("")
                    signup_edtConfirmPassword.setText("")
                    signup_edtNameOfYourChoir.setText("")
                    Utility.showMessageDialog(context, result!!.message)

                }
                else
                {
                    Utility.showMessageDialog(context, result!!.message)
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
