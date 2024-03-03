package com.choraline.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

import com.choraline.utils.Constants
import com.choraline.utils.Utility

/**
 * Created by deepak Tyagi on 8/18/2017.
 */

class DownlaodCompleteReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action == Constants.DOWNLOAD_COMPLETE_ACTION) {
            Utility.displayToast(context, "Your Choraline music download has been completed. Please check in your storage Choraline Folder.")
        }
    }
}
