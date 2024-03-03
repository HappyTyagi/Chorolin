package com.choraline.services

import android.app.*
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Environment
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.FileProvider
import com.choraline.BuildConfig
import com.choraline.R
import com.choraline.models.Download
import com.choraline.models.SongsData
import com.choraline.network.APIClient
import com.choraline.network.APIInterface
import com.choraline.utils.AppLog
import com.choraline.utils.Constants
import com.choraline.utils.Utility
import okhttp3.ResponseBody
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


/**
 * Created by deepak Tyagi on 8/7/2017.
 */

class DownloadService : IntentService("Download Service") {

    private var notificationBuilder: NotificationCompat.Builder? = null
    private var notificationManager: NotificationManager? = null
    private var totalFileSize: Int = 0
    private var fileName = ""
    private lateinit var context: Context
    private val TAG = "DownloadService"
    private var songsData = SongsData()

    private var singerName = ""
    private var subtitle = ""
    private var voiceType = ""


    override fun onHandleIntent(intent: Intent?) {
        context = this
        val url = intent!!.getStringExtra("url")
        fileName =
            intent!!.getStringExtra("id") + Constants.DELIMITER + intent!!.getStringExtra("fileName")
        songsData.songTitle = intent!!.getStringExtra("fileName")!!
        songsData.title = intent!!.getStringExtra("fileName")!!
        songsData.id = intent!!.getStringExtra("id")!!
        songsData.dbUrl = url!!


        singerName =
            intent.getStringExtra(Constants.AppConstants.NAME)!!.replace("/", " ").replace("*", " ")
        subtitle = intent.getStringExtra(Constants.AppConstants.SUBTITLE)!!.replace("/", " ")
            .replace("*", " ")
        voiceType = intent.getStringExtra(Constants.AppConstants.VOICE_TYPE)!!.replace("/", " ")
            .replace("*", " ")


        val intents = Intent(Intent.ACTION_VIEW)
        val filedir = File(
            Environment.getExternalStorageDirectory(),
            "/ChoraLine/" + singerName + "/" + subtitle + "/" + voiceType
        )

        val file = File(filedir, "/" + fileName)

        AppLog.debugD(Constants.LOG_TAG, "path :: " + file.path)
        val fileURI =
            FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", file)
        intents.setDataAndType(fileURI, "audio/*")
        intents.flags =
            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION

        val pendingIntent =
            PendingIntent.getActivity(applicationContext, 21, intents, PendingIntent.FLAG_IMMUTABLE)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        val channelId = "MyChanelId";



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(channelId, "Choraline")
            notificationBuilder = NotificationCompat.Builder(context!!, channelId)


        } else {
            notificationBuilder = NotificationCompat.Builder(context)
        }

        // notificationBuilder.setSmallIcon()

        notificationBuilder?.setSmallIcon(R.mipmap.download)
            ?.setContentTitle("ChoraLine - Music")
            ?.setContentText("Downloading Music")
            ?.setAutoCancel(true)
            ?.setContentIntent(pendingIntent)

        //  startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, status)
        // notificationManager!!.notify(0, notificationBuilder!!.build())
        notificationManager?.notify(
            System.currentTimeMillis().toInt(),
            notificationBuilder?.build()
        )

        initDownload(url)

    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(
        channelId: String,
        channelName: String
    ): NotificationChannel {
        val chan = NotificationChannel(
            channelId,
            channelName, NotificationManager.IMPORTANCE_NONE
        )
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        //   notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager?.createNotificationChannel(chan)
        return chan
    }


    private fun initDownload(url: String) {

        var retrofit = APIClient.getClient()
        var retrofitInterface = retrofit!!.create(APIInterface::class.java)
        val request = retrofitInterface.downloadSong(url)
        try {

            downloadFile(request.execute().body())

        } catch (e: IOException) {

            e.printStackTrace()
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()

        }

    }

    @Throws(IOException::class)
    private fun downloadFile(body: ResponseBody?) {

        var count: Int
        val data = ByteArray(1024 * 4)
        val fileSize = body!!.contentLength()
        val bis = BufferedInputStream(body!!.byteStream(), 1024 * 8)


//        val filedir = File(
//            Environment.getExternalStorageDirectory().absolutePath,
//            "/ChoraLine" + "/" + singerName + "-" + subtitle + "-" + voiceType
//        )

        var appSpecificInternalStorageDirectory = context.getDir("Choralin", Context.MODE_PRIVATE)
        var file1111 = File(appSpecificInternalStorageDirectory, subtitle);

        if (!(file1111 as File).exists()) {
            (file1111 as File).mkdirs()
            file1111.mkdir()
        }


        AppLog.debugD(Constants.LOG_TAG, "path :: " + file1111.path)


        var output: FileOutputStream? = null

        var file = File(file1111, fileName)
        file.createNewFile()
        output = FileOutputStream(file, false)


//            val outputFile = File(filedir, fileName)
//            songsData.songUrl = outputFile.absolutePath
//            output = FileOutputStream(outputFile)


        //val file = File(filedir.absolutePath, "/"+fileName)*

        var total: Long = 0
        val startTime = System.currentTimeMillis()
        var timeCount = 1
        count = bis.read(data)
        while (count != -1) {

            total += count.toLong()
            totalFileSize = (fileSize / Math.pow(1024.0, 2.0)).toInt()
            val current = Math.round(total / Math.pow(1024.0, 2.0)).toDouble()

            val progress = (total * 100 / fileSize).toInt()

            val currentTime = System.currentTimeMillis() - startTime

            val download = Download()
            download.totalFileSize = totalFileSize

            if (currentTime > 1000 * timeCount) {

                download.currentFileSize = current.toInt()
                download.progress = progress
                sendNotification(download)
                timeCount++
            }

            output.write(data, 0, count)
            count = bis.read(data)
        }
        onDownloadComplete()
        output.flush()
        output.close()
        bis.close()

    }

    private fun sendNotification(download: Download) {

        sendIntent(download)
        notificationBuilder!!.setProgress(100, download.progress, false)
        notificationBuilder!!.setContentText("Downloading file " + download.currentFileSize + "/" + totalFileSize + " MB")
        notificationManager!!.notify(0, notificationBuilder!!.build())
    }


    private fun sendIntent(download: Download) {

        //Intent intent = new Intent(MainActivity.MESSAGE_PROGRESS);
        //intent.putExtra("download",download);
        //LocalBroadcastManager.getInstance(DownloadService.this).sendBroadcast(intent);
    }

    private fun onDownloadComplete() {

        val download = Download()
        download.progress = 100
        sendIntent(download)



        notificationManager!!.cancel(0)
        notificationBuilder!!.setProgress(0, 0, false)
        notificationBuilder!!.setContentText("File Downloaded")
        notificationManager!!.notify(0, notificationBuilder!!.build())
        Utility.displayToast(context, "Your download has been completed.")
        val intent = Intent()
        intent.action = Constants.DOWNLOAD_COMPLETE_ACTION
        sendBroadcast(intent)
    }

    override fun onTaskRemoved(rootIntent: Intent) {
        notificationManager!!.cancel(0)
    }

}
