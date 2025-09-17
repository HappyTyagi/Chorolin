package com.choraline

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import com.choraline.models.GenericResponseModel
import com.choraline.network.APIListener
import com.choraline.network.Webservices
import com.choraline.utils.Constants
import com.choraline.utils.Utility


class ForgetPasswordActivity : BaseActivity(), View.OnClickListener, APIListener {

    lateinit var context: Context
    var forgotpassword_btnSubmit : Button? = null
    var forgotpassword_txtBackToLogin : TextView? = null
    var forgotpassword_edtEmail : EditText? = null
    var layoutParent : RelativeLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)

        layoutParent = findViewById(R.id.layoutParent)
        forgotpassword_btnSubmit = findViewById(R.id.forgotpassword_btnSubmit)
        forgotpassword_txtBackToLogin = findViewById(R.id.forgotpassword_txtBackToLogin)
        forgotpassword_edtEmail = findViewById(R.id.forgotpassword_edtEmail)

        context= this
        initUI()
    }

    fun initUI()
    {
        forgotpassword_btnSubmit!!.setOnClickListener(this)
        forgotpassword_txtBackToLogin!!.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        Utility!!.hideSoftKeyboard(this)
        if(v==forgotpassword_btnSubmit)
        {
            if(validate())
            {
                submitEmail()
                //Utility.showSnakeBar(layoutParent, "Under Development.")
            }
        }
        else if(v==forgotpassword_txtBackToLogin)
        {
            finish()
        }
    }

    fun validate() : Boolean
    {
        if(forgotpassword_edtEmail!!.text.toString().equals(""))
        {
            Utility.showSnakeBar(layoutParent!!, "Enter Your Email Id.")
            return false
        }
        if(!Utility.isValidEmail(forgotpassword_edtEmail!!.text.toString()))
        {
            Utility.showSnakeBar(layoutParent!!, "Enter Valid Email Id.")
            return false
        }

        return true
    }

    fun submitEmail()
    {
        Webservices(context, this, true, "Please wait...").callForgotPasswordAPI(forgotpassword_edtEmail!!.text.toString(),
                Constants.API_FORGOT_PASSWORD)
    }

    override fun onApiSuccess(obj: Any, api: Int) {

        if(api==Constants.API_FORGOT_PASSWORD)
        {
            val result=obj as GenericResponseModel
            if(result.status)
            {
                forgotpassword_edtEmail!!.setText("")
                Utility.showMessageDialog(context, result.message)

            }
            else
                Utility.showMessageDialog(context, result.message)
        }
    }

    override fun onApiFailure(throwable: Throwable,api: Int) {

    }

}
