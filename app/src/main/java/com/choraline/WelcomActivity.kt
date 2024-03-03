package com.choraline

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.choraline.models.FreeTrailModel
import com.choraline.network.APIListener
import com.choraline.network.InformationStorage
import com.choraline.network.Webservices
import com.choraline.utils.Constants
import com.choraline.utils.Utility
import java.io.File

class WelcomActivity : BaseActivity(), View.OnClickListener, APIListener{

    private val TAG: String? = WelcomActivity::class.simpleName
    private lateinit var context: Context
    private var intents: Intent? = null
    private var btnFreeTrail: Button? = null
    private var btnLogin: Button? = null
    private var btnJoin: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcom)
        context=this
        initUI()

        if (!Utility.isExternalStorageReadingAllowed(this)||!Utility.isExternalStorageWritingAllowed(this))
        {
            Utility.requestReadAndWriteExternalStoragePersmission(this)
        }
    }

    fun initUI()
    {
        //getFreeTrail()
        btnFreeTrail = findViewById<Button>(R.id.welcom_btnFreeTrail) as Button
        btnLogin = findViewById<Button>(R.id.welcom_btnLogin) as Button
        btnJoin = findViewById<Button>(R.id.welcom_btnJoin) as Button

        btnFreeTrail!!.setOnClickListener(this)
        btnLogin!!.setOnClickListener(this)
        btnJoin!!.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        if(v===btnFreeTrail)
        {
            if (!Utility.isExternalStorageReadingAllowed(this) || !Utility.isExternalStorageWritingAllowed(this))
            {
                var builder = AlertDialog.Builder(context)
                builder.setMessage("Please allow Choraline to Read write permission.")
                        .setPositiveButton("ok", DialogInterface.OnClickListener { dialog, id ->
                            // FIRE ZE MISSILES!
                            Utility.requestReadAndWriteExternalStoragePersmission(this)

                            dialog.dismiss()

                        })
                        .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->
                            // User cancelled the dialog
                            dialog.dismiss()
                        })

                builder.create()
                builder.show()
            }else
            getFreeTrail()

        }
        else if(v===btnLogin)
        {
            if (!Utility.isExternalStorageReadingAllowed(this)||!Utility.isExternalStorageWritingAllowed(this))
            {
                val builder = AlertDialog.Builder(context)
                builder.setMessage("Please allow Choraline to Read write permission.")
                        .setPositiveButton("ok", DialogInterface.OnClickListener { dialog, id ->
                            // FIRE ZE MISSILES!
                            Utility.requestReadAndWriteExternalStoragePersmission(this)

                            dialog.dismiss()

                        })
                        .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->
                            // User cancelled the dialog
                            dialog.dismiss()
                        })

                builder.create()
                builder.show()
            }else {
                intents = Intent(context, LoginActivity::class.java)
                startActivity(intents)
            }
        }
        else if(v===btnJoin)
        {
            if (!Utility.isExternalStorageReadingAllowed(this)||!Utility.isExternalStorageWritingAllowed(this))
            {
                val builder = AlertDialog.Builder(context)
                builder.setMessage("Please allow Choraline to Read write permission.")
                        .setPositiveButton("ok", DialogInterface.OnClickListener { dialog, id ->
                            // FIRE ZE MISSILES!
                            Utility.requestReadAndWriteExternalStoragePersmission(this)

                            dialog.dismiss()

                        })
                        .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->
                            // User cancelled the dialog
                            dialog.dismiss()
                        })

                builder.create()
                builder.show()
            }else {
                intents = Intent(context, SignupActivity::class.java)
                startActivity(intents)
            }
        }
    }

    fun getFreeTrail()
    {
        Webservices(context, this, true, "Please wait...").callGetFreeTrailAPI(Constants.API_GET_FREE_TRAIL)
    }


    override fun onApiSuccess(obj: Any, api: Int) {

        if(api==Constants.API_GET_FREE_TRAIL)
        {
            val result=obj as FreeTrailModel
            if(result.status)
            {
                InformationStorage.instance.freeTrailModel=result.response
                intents = Intent(context, FreeTrailActivity::class.java)
                startActivity(intents)
            }
            else
            {
                Utility!!.showMessageDialog(context, result!!.message)
            }
        }
    }

    override fun onApiFailure(throwable: Throwable,api: Int) {

    }
}
