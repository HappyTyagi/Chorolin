package com.choraline

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.choraline.models.GenericResponseModel
import com.choraline.network.APIListener
import com.choraline.network.Webservices
import com.choraline.utils.Constants
import com.choraline.utils.Utility

import kotlinx.android.synthetic.main.activity_forget_password.*

class ForgetPasswordActivity : BaseActivity(), View.OnClickListener, APIListener {

    lateinit var context: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)
        context= this
        initUI()
    }

    fun initUI()
    {
        forgotpassword_btnSubmit.setOnClickListener(this)
        forgotpassword_txtBackToLogin.setOnClickListener(this)
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
        if(forgotpassword_edtEmail.text.toString().equals(""))
        {
            Utility.showSnakeBar(layoutParent, "Enter Your Email Id.")
            return false
        }
        if(!Utility.isValidEmail(forgotpassword_edtEmail.text.toString()))
        {
            Utility.showSnakeBar(layoutParent, "Enter Valid Email Id.")
            return false
        }

        return true
    }

    fun submitEmail()
    {
        Webservices(context, this, true, "Please wait...").callForgotPasswordAPI(forgotpassword_edtEmail.text.toString(),
                Constants.API_FORGOT_PASSWORD)
    }

    override fun onApiSuccess(obj: Any, api: Int) {

        if(api==Constants.API_FORGOT_PASSWORD)
        {
            val result=obj as GenericResponseModel
            if(result.status)
            {
                forgotpassword_edtEmail.setText("")
                Utility.showMessageDialog(context, result.message)

            }
            else
                Utility.showMessageDialog(context, result.message)
        }
    }

    override fun onApiFailure(throwable: Throwable,api: Int) {

    }

}
