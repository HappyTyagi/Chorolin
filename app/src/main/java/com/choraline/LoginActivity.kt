package com.choraline

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.view.View.OnClickListener
import com.choraline.models.LoginModel
import com.choraline.network.APIInterface
import com.choraline.network.APIListener
import com.choraline.network.Webservices
import com.choraline.utils.AppController
import com.choraline.utils.Constants
import com.choraline.utils.Utility
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*
import java.io.File
import java.security.AccessController.getContext


class LoginActivity : BaseActivity(), OnClickListener, APIListener{

    lateinit var context: Context
    private var intents: Intent? = null
    private var apiInterface: APIInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        context=this
        initUI()
        deleteAllSong()
    }

    fun initUI()
    {
        login_txtforgotPassword.setOnClickListener(this)
        login_btnLogin.setOnClickListener(this)
        login_btnJoin.setOnClickListener(this)
        if (!Utility.isExternalStorageReadingAllowed(this)||!Utility.isExternalStorageWritingAllowed(this))
        {
            Utility.requestReadAndWriteExternalStoragePersmission(this)
        }
    }

    override fun onClick(v: View?) {
        Utility!!.hideSoftKeyboard(this)
        if(v===login_btnLogin)
        {
            if(validate())
            {
                doLogin()
            }

        }
        else if(v==login_txtforgotPassword)
        {
            intents = Intent(context, ForgetPasswordActivity::class.java)
            startActivity(intents)
        }
        else if(v== login_btnJoin)
        {
            intents = Intent(context, SignupActivity::class.java)
            startActivity(intents)
        }
    }

    fun validate() : Boolean
    {
        if(login_edtEmail.text.toString().equals(""))
        {
            Utility!!.showSnakeBar(layoutParent, "Enter Email Id")
            return false
        }
        if(login_edtPassword.text.toString().equals(""))
        {
            Utility!!.showSnakeBar(layoutParent, "Enter Your Password")
            return false
        }
        return true
    }

    fun doLogin()
    {

        Webservices(context, this, true, "Logging In...").callLoginAPI(login_edtEmail.text.toString(), login_edtPassword.text.toString(), "tewqtqtgqrterytewyth", "Android", Constants.API_LOGIN);
    }


    fun deleteAllSong()
    {

        val dir = File(Environment.getExternalStorageDirectory() ,"/ChoraLine")
        if (dir!= null&&dir.isDirectory()) {
            val parent = dir.listFiles()
            if (parent!=null)
            {
            for (data in parent) {
                var children = data.listFiles()
                for (i in children.indices) {
                    try {
                        File(children[i].absolutePath).delete()
                    } catch (e: Exception) {

                    }

                }
            }
            }


            for (i in parent.indices)
            {
                try {
                    File( parent[i].absolutePath).delete()
                }catch (e:Exception)
                {

                }
            }
        }
    }




    override fun onApiSuccess(obj: Any, api: Int) {

        when(api){

            Constants.API_LOGIN->{

                var result= obj as LoginModel
                if(result.status)
                {
                    val gson = Gson()
                    var gsonOutput = gson.toJson(result.response)
                    AppController.appPref.userData=gsonOutput
                    AppController.appPref.isLogin=true
                    AppController.appPref.userId=result!!.response!!.userId
                    AppController.appPref.userName=result!!.response!!.firstname+" "+result!!.response!!.lastname
                    AppController.appPref.token=result!!.response!!.token
                    AppController.appPref.accessToken = result!!.response!!.accessToken



                    var i=Intent(context, HomeActivity::class.java)
                    startActivity(i)
                    finishAffinity()
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
}
