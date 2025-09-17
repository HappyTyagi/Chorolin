package com.choraline

import android.content.Context

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import android.text.InputFilter
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RelativeLayout
import com.choraline.models.LoginModel
import com.choraline.network.APIListener
import com.choraline.network.Webservices
import com.choraline.utils.Constants
import com.choraline.utils.Utility


class SignupActivity : BaseActivity(), View.OnClickListener, APIListener {

    private lateinit var context: Context
    var toolbar: Toolbar? = null
    var signup_edtFirstName: EditText? = null
    var signup_edtLastName: EditText? = null
    var tootlbar_imgbtnShare: ImageButton? = null
    var signup_btnSubmit: Button? = null
    var signup_edtEmail: EditText? = null
    var signup_edtPassword: EditText? = null
    var signup_edtConfirmEmail: EditText? = null
    var signup_edtConfirmPassword: EditText? = null
    var signup_edtNameOfYourChoir: EditText? = null
    var layoutParent: RelativeLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        context = this@SignupActivity
        toolbar = findViewById(R.id.toolbar)
        signup_edtFirstName = findViewById(R.id.signup_edtFirstName)
        signup_edtLastName = findViewById(R.id.signup_edtLastName)
        tootlbar_imgbtnShare = findViewById(R.id.tootlbar_imgbtnShare)
        signup_btnSubmit = findViewById(R.id.signup_btnSubmit)
        signup_edtEmail = findViewById(R.id.signup_edtEmail)
        signup_edtPassword = findViewById(R.id.signup_edtPassword)
        signup_edtConfirmEmail = findViewById(R.id.signup_edtConfirmEmail)
        signup_edtConfirmPassword = findViewById(R.id.signup_edtConfirmPassword)
        signup_edtNameOfYourChoir = findViewById(R.id.signup_edtNameOfYourChoir)
        layoutParent = findViewById(R.id.layoutParent)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        initUI()
    }

    fun initUI() {
        signup_edtFirstName!!.filters = arrayOf(filter)
        signup_edtLastName!!.filters = arrayOf(filter)
        //signup_edtNameOfYourChoir!!.filters= arrayOf(filter)
        tootlbar_imgbtnShare!!.setOnClickListener(this)
        signup_btnSubmit!!.setOnClickListener(this)
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
        if (v == tootlbar_imgbtnShare) {
            Utility!!.shareApp(context)
        } else if (v == signup_btnSubmit) {
            if (validate()) {
                doSignup()

            }
        }
    }

    fun validate(): Boolean {
        if (signup_edtFirstName!!.text.toString().equals("")) {
            Utility!!.showSnakeBar(layoutParent!!, "Enter Your First Name.")
            signup_edtFirstName!!.requestFocus()
            return false
        }
        if (signup_edtLastName!!.text.toString().equals("")) {
            Utility!!.showSnakeBar(layoutParent!!, "Enter Your Last Name.")
            signup_edtLastName!!.requestFocus()
            return false
        }
        if (signup_edtEmail!!.text.toString().equals("")) {
            Utility!!.showSnakeBar(layoutParent!!, "Enter Your Email.")
            signup_edtEmail!!.requestFocus()
            return false
        }
        if (!Utility!!.isValidEmail(signup_edtEmail!!.text.toString())) {
            Utility!!.showSnakeBar(layoutParent!!, "Enter a Valid Email.")
            signup_edtEmail!!.requestFocus()
            return false
        }
        if (signup_edtConfirmEmail!!.text.toString().equals("")) {
            Utility!!.showSnakeBar(layoutParent!!, "Re-Enter Email To Confirm.")
            signup_edtConfirmEmail!!.requestFocus()
            return false
        }
        if (!Utility!!.isValidEmail(signup_edtConfirmEmail!!.text.toString())) {
            Utility!!.showSnakeBar(layoutParent!!, "Enter a Valid Confirm Email.")
            signup_edtConfirmEmail!!.requestFocus()
            return false
        }
        if (signup_edtPassword!!.text.toString().equals("")) {
            Utility!!.showSnakeBar(layoutParent!!, "Enter Password.")
            signup_edtPassword!!.requestFocus()
            return false
        }
        if (signup_edtPassword!!.text.toString().length < 6) {
            Utility!!.showSnakeBar(
                layoutParent!!,
                "Your password should contain at least 6 characters."
            )
            signup_edtPassword!!.requestFocus()
            return false
        }
        if (signup_edtConfirmPassword!!.text.toString().equals("")) {
            Utility!!.showSnakeBar(layoutParent!!, "Re-Enter Password To Confirm .")
            signup_edtConfirmPassword!!.requestFocus()
            return false
        }
        if (!signup_edtConfirmPassword!!.text.toString()
                .equals(signup_edtPassword!!.text.toString())
        ) {
            Utility!!.showSnakeBar(layoutParent!!, "Confirm Password doesn't match.")
            signup_edtConfirmPassword!!.requestFocus()
            return false
        }

        return true
    }

    fun doSignup() {
        Webservices(context, this, true, "Signing up...").callSignupAPI(
            signup_edtFirstName!!.text.toString(),
            signup_edtLastName!!.text.toString(),
            signup_edtEmail!!.text.toString(),
            signup_edtEmail!!.text.toString(),
            signup_edtPassword!!.text.toString(),
            signup_edtPassword!!.text.toString(),
            signup_edtNameOfYourChoir!!.text.toString(),
            "",
            "Android",
            Constants.API_SIGNUP
        )
    }

    override fun onApiSuccess(obj: Any, api: Int) {

        when (api) {
            Constants.API_SIGNUP -> {
                var result = obj as LoginModel
                if (result.status) {

                    signup_edtFirstName!!.setText("")
                    signup_edtLastName!!.setText("")
                    signup_edtEmail!!.setText("")
                    signup_edtConfirmEmail!!.setText("")
                    signup_edtPassword!!.setText("")
                    signup_edtConfirmPassword!!.setText("")
                    signup_edtNameOfYourChoir!!.setText("")
                    Utility.showMessageDialog(context, result!!.message)

                } else {
                    Utility.showMessageDialog(context, result!!.message)
                }
            }
        }

    }

    override fun onApiFailure(throwable: Throwable, api: Int) {

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
