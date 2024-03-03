package com.choraline.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat

/**
 * Created by kumarpalsinh on 24/2/17.
 */

class Permissions {



    //Check for read storage permission
    fun isReadStoragePermissionGranted(mActivity: Activity): Boolean? {

        if (ContextCompat.checkSelfPermission(mActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            Utility.displayToast(mActivity, "Please allow storage permission.")

            ActivityCompat.requestPermissions(mActivity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_READ_STORAGE_PERMISSION)
            return false

        } else {
            return true
        }
    }

    //Check for write storage permission
    fun isWriteStoragePermissionGranted(mActivity: Activity): Boolean? {

        if (ContextCompat.checkSelfPermission(mActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            Utility.displayToast(mActivity, "Please allow storage permission.")

            ActivityCompat.requestPermissions(mActivity,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_WRITE_STORAGE_PERMISSION)


            return false

        } else {
            return true
        }
    }

    //Check for read contacts permission
    fun isReadContactsPermissionGranted(mActivity: Activity): Boolean? {

        if (ContextCompat.checkSelfPermission(mActivity,
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

            Utility.displayToast(mActivity, "Please allow contact permission.")

            ActivityCompat.requestPermissions(mActivity,
                    arrayOf(Manifest.permission.READ_CONTACTS),
                    REQUEST_READ_CONTACTS_PERMISSION)
            return false

        } else {
            return true
        }
    }

    //Check for write contacts permission
    fun isWriteContactsPermissionGranted(mActivity: Activity): Boolean? {

        if (ContextCompat.checkSelfPermission(mActivity,
                Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

            Utility.displayToast(mActivity, "Please allow contact permission.")

            ActivityCompat.requestPermissions(mActivity,
                    arrayOf(Manifest.permission.WRITE_CONTACTS),
                    REQUEST_WRITE_CONTACTS_PERMISSION)
            return false

        } else {
            return true
        }
    }

    //Check for camera permission
    fun isCameraPermissionGranted(mActivity: Activity): Boolean? {

        if (ContextCompat.checkSelfPermission(mActivity,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            Utility.displayToast(mActivity, "Please allow camera permission.")

            ActivityCompat.requestPermissions(mActivity,
                    arrayOf(Manifest.permission.CAMERA),
                    REQUEST_CAMERA_PERMISSION)
            return false

        } else {
            return true
        }
    }










    //Check for write settings permission
    fun isWriteSettingsPermissionGranted(mActivity: Activity): Boolean? {

        if (ContextCompat.checkSelfPermission(mActivity,
                Manifest.permission.WRITE_SETTINGS) != PackageManager.PERMISSION_GRANTED) {

            Utility.displayToast(mActivity, "Please allow write settings permission.")

            ActivityCompat.requestPermissions(mActivity,
                    arrayOf(Manifest.permission.WRITE_SETTINGS),
                    REQUEST_WRITE_SETTINGS)
            return false

        } else {
            return true
        }
    }

    //Check for record audio permission
    fun isRecordAudioPermissionGranted(mActivity: Activity): Boolean? {

        if (ContextCompat.checkSelfPermission(mActivity,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            Utility.displayToast(mActivity, "Please allow record audio permission.")

            ActivityCompat.requestPermissions(mActivity,
                    arrayOf(Manifest.permission.RECORD_AUDIO),
                    REQUEST_RECORD_AUDIO)
            return false

        } else {
            return true
        }
    }

    companion object {

        private var mPermissions: Permissions? = null

        val REQUEST_READ_STORAGE_PERMISSION = 1
        val REQUEST_WRITE_STORAGE_PERMISSION = 2
        val REQUEST_READ_CONTACTS_PERMISSION = 3
        val REQUEST_WRITE_CONTACTS_PERMISSION = 4
        val REQUEST_CAMERA_PERMISSION = 5
        val REQUEST_WRITE_SETTINGS = 6
        val REQUEST_RECORD_AUDIO = 7

        //Get Single Instance of CommonUtils Class
        val instance: Permissions
            get() {
                if (mPermissions == null) {
                    mPermissions = Permissions()
                }
                return mPermissions as Permissions
            }
    }

}


