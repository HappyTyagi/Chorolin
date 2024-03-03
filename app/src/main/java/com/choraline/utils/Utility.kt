package com.choraline.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.choraline.R
import com.google.android.material.snackbar.Snackbar


/**
 * Created by deepak Tyagi on 5/13/2017.
 */

object Utility {

    var isUserInLoggedInSection = false

    /**
     * This function is used for checking internet connection
     */
    fun isNetworkAvailable(context: Context): Boolean {

        try {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        } catch (e: Exception) {
            return false
        }

    }

    /**
     * This function is used to show Toast Message
     */
    fun displayToast(context: Context?, respMsg: String) {
        try {
            if (context != null) {
                Toast.makeText(context, respMsg, Toast.LENGTH_SHORT)
                    .show()
            }
        } catch (e: Exception) {
            AppLog.loadStackTrace(e)
        }

    }

    fun showSnakeBar(view: View, msg: String) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show()
    }

    fun showMessageDialog(mContext: Context?, msg: String?) {
        if (mContext == null) return
        val myAlertDialog = AlertDialog.Builder(mContext)
        myAlertDialog.setIcon(R.mipmap.app_icon)
        myAlertDialog.setTitle("ChoraLine")
        myAlertDialog.setMessage(msg)
        myAlertDialog.setPositiveButton("OK") { dialog, which -> }
        val dialog = myAlertDialog.create()
        try {
            dialog.show()
        } catch (e: Exception) {
        }

    }

    fun overrideFontAwsom(context: Context, v: View) {
        try {
            if (v is ViewGroup) {
                val vg = v
                for (i in 0..vg.childCount - 1) {
                    val child = vg.getChildAt(i)
                    overrideFontAwsom(context, child)
                }
            } else if (v is TextView) {
                v.typeface = Typeface.createFromAsset(context.assets, "fontawesome-webfont.ttf")

            } else if (v is EditText) {
                v.typeface = Typeface.createFromAsset(context.assets, "fontawesome-webfont.ttf")

            } else if (v is Button) {
                v.typeface = Typeface.createFromAsset(context.assets, "fontawesome-webfont.ttf")

            } else if (v is CheckBox) {
                v.typeface = Typeface.createFromAsset(context.assets, "fontawesome-webfont.ttf")

            } else if (v is RadioButton) {
                v.typeface = Typeface.createFromAsset(context.assets, "fontawesome-webfont.ttf")

            }

        } catch (e: Exception) {
        }

    }

    fun setValueToPref(context: Context, key: String, value: String) {
        val pref = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE)
        val e = pref.edit()
        e.putString(key, value)
        e.commit()
    }

    fun getValueFromPref(context: Context, key: String): String {
        val pref = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE)
        return pref.getString(key, "")!!
    }

    fun isValidEmail(target: CharSequence?): Boolean {
        if (target == null) {
            return false
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }
    }

    fun clearPref(context: Context) {
        val pref = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE)
        val e = pref.edit()
        e.clear()
        e.commit()
    }

    /**
     * This function is used for hiding soft keyboard
     */
    fun hideSoftKeyboard(mActivity: Activity) {
        // Check if no view has focus:
        val view = mActivity.currentFocus
        if (view != null) {
            val imm = mActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun shareApp(context: Context) {
        try {
            val i = Intent(Intent.ACTION_SEND)
            i.type = "text/plain"
            i.putExtra(Intent.EXTRA_SUBJECT, "Sing Your Notes Perfectly - ChoraLine App")
            //var sAux = "I'm using the ChoraLine app to learn my choral singing parts! You might like to try it too:\n\n"
            var sAux =
                "I’m using the ChoraLine app to learn my choral voice parts! Why not give it a try? \n\n"
            //sAux = sAux + "https://play.google.com/store/apps/details?id="+context!!.packageName+" \n\n"
            sAux = sAux + "https://www.choralineapp.com/"
            i.putExtra(Intent.EXTRA_TEXT, sAux)
            context.startActivity(Intent.createChooser(i, "choose one"))
        } catch (e: Exception) {
            //e.toString();
        }

    }

    /**
     * Function to convert milliseconds time to
     * Timer Format
     * Hours:Minutes:Seconds
     */
    fun milliSecondsToTimer(milliseconds: Long): String {
        var finalTimerString = ""
        var secondsString = ""

        // Convert total duration into time
        val hours = (milliseconds / (1000 * 60 * 60)).toInt()
        val minutes = (milliseconds % (1000 * 60 * 60)).toInt() / (1000 * 60)
        val seconds = (milliseconds % (1000 * 60 * 60) % (1000 * 60) / 1000).toInt()
        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours.toString() + ":"
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds
        } else {
            secondsString = "" + seconds
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString

        // return timer string
        return finalTimerString
    }

    /**
     * Function to get Progress percentage
     * @param currentDuration
     * *
     * @param totalDuration
     * *
     */
    fun getProgressPercentage(currentDuration: Long, totalDuration: Long): Int {
        var percentage: Double? = 0.toDouble()

        val currentSeconds = (currentDuration / 1000).toInt().toLong()
        val totalSeconds = (totalDuration / 1000).toInt().toLong()

        // calculating percentage
        percentage = currentSeconds.toDouble() / totalSeconds * 100

        // return percentage
        return percentage.toInt()
    }

    /**
     * Function to change progress to timer
     * @param progress -
     * *
     * @param totalDuration
     * * returns current duration in milliseconds
     * *
     */
    fun progressToTimer(progress: Int, totalDuration: Int): Int {
        var totalDuration = totalDuration
        var currentDuration = 0
        totalDuration = (totalDuration / 1000)
        currentDuration = (progress.toDouble() / 100 * totalDuration).toInt()

        // return current duration in milliseconds
        return (currentDuration * 1000)
    }

    fun getDefaultAlbumArt(context: Context): Bitmap? {
        var bm: Bitmap? = null
        val options = BitmapFactory.Options()
        try {
            bm = BitmapFactory.decodeResource(
                context.getResources(),
                R.mipmap.app_icon, options
            )
        } catch (ee: Error) {
        } catch (e: Exception) {
        }

        return bm
    }


    fun isExternalStorageWritingAllowed(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            var permission =
                context.checkSelfPermission(android.Manifest.permission.READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED
            return permission;

        } else if (Build.VERSION.SDK_INT >= 23) {
            var permission =
                context.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
            return permission;
        } else {
            return true;
        }
    }


    fun isExternalStorageReadingAllowed(context: Context): Boolean {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            var permission =
                context.checkSelfPermission(android.Manifest.permission.READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED
            return permission;

        } else if (Build.VERSION.SDK_INT >= 23) {
            var permission =
                context.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
            return permission;
        } else {
            return true;
        }


    }


    fun requestReadAndWriteExternalStoragePersmission(activity: Activity) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(activity, storge_permissions_33, 12)
        } else {
            ActivityCompat.requestPermissions(activity, storge_permissions, 12)
        }

    }


    var storge_permissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    var storge_permissions_33 = arrayOf(
        Manifest.permission.READ_MEDIA_AUDIO
    )

    fun openBrowawer(context: Context, url: String) {

        val i = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(i)
    }

    fun customToast(context: Context?, message: String?) {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.custom_tost, null as ViewGroup?)
        (view.findViewById<View>(R.id.txt_message) as TextView).text = message
        val toast = Toast(context)
        toast.duration = Toast.LENGTH_LONG
        toast.setGravity(80, 0, 0)
        toast.setView(view)
        toast.show()
    }

    const val POPUP = "logintoken"
    const val PAYPOPUP = "token"


    fun getPopUp(context: Context): String? {
        return context.getSharedPreferences(POPUP, 0)
            .getString(POPUP, "")
    }

    fun setPopup(context: Context, terminal_id: String?) {
        context.getSharedPreferences(POPUP, 0).edit()
            .putString(POPUP, terminal_id).commit()
    }
    


    fun getPayPopUp(context: Context): String? {
        return context.getSharedPreferences(POPUP, 0)
            .getString(PAYPOPUP, "")
    }

    fun setPayPopup(context: Context, terminal_id: String?) {
        context.getSharedPreferences(POPUP, 0).edit()
            .putString(PAYPOPUP, terminal_id).commit()
    }



    @SuppressLint("SetTextI18n")
    fun showTime(
        progress: Int,
        txtProgress: TextView,
        totalDuration: Int,
        txtStartProgress: TextView
    ) {
        txtProgress.text = secondsToString(progress / 1000)
        txtStartProgress.text = secondsToString(totalDuration / 1000)
    }

    private fun secondsToString(pTime: Int): String {
        return String.format("%02d:%02d", pTime / 60, pTime % 60)
    }


    fun updatePlayerView(
        txtStartTime: TextView,
        txtEndTime: TextView,
        seekBar: SeekBar,
        visible: Boolean
    ) {
        when (visible) {
            true -> {
                txtStartTime.visibility = VISIBLE
                txtEndTime.visibility = VISIBLE
                seekBar.visibility = VISIBLE
            }

            false -> {
                txtStartTime.visibility = GONE
                txtEndTime.visibility = GONE
                seekBar.visibility = GONE
            }
        }
    }

}
