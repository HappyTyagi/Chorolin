package com.choraline.services

import android.annotation.TargetApi
import android.app.*
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import android.widget.Toast

import com.choraline.HomeActivity
import com.choraline.R
import com.choraline.utils.AppLog
import com.choraline.utils.Constants
import com.choraline.utils.Utility
import java.util.*
import android.media.AudioManager
import android.content.ContentUris
import android.graphics.BitmapFactory
import com.choraline.models.SongsData
import com.choraline.utils.AppController
import kotlinx.android.synthetic.main.layout_player.*
import android.content.ComponentName
import android.graphics.Color
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.*

import android.view.LayoutInflater
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.choraline.BaseActivity
import com.choraline.SplashActivity


/**
 * Created by deepak Tyagi on 8/7/2017.
 */

//http://www.tutorialsface.com/2015/08/android-custom-notification-tutorial/
class MusicPlayerService : Service(), MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {
    private val LOG_TAG = MusicPlayerService::class.java.simpleName
    private var context: Context? = null

    //media player
    private var player: MediaPlayer? = null
    //song list
    private var songs: ArrayList<SongsData>? = null
    //current position
    private var songPosn: Int = 0
    var albumId: String = ""
    //title of current song
    private var songTitle = ""
    var currentDownLoadPlayUrl = ""
    //binder
    private val musicBind = MusicBinder()
    private var rand: Random? = null
    val TAG = "MusicPlayerService"

    lateinit var  notificationmanager:NotificationManager



    companion object {
        var mInstance: MusicPlayerService? = null
    }


    // indicates the state our service:
    enum class State {
        Stopped,
        Preparing,
        Playing,
        Paused
    }
    lateinit var mState: State


    override fun onCreate() {
        //create the service
        super.onCreate()
        mInstance = this;
        if (AppController!!.service != null)
            AppController!!.service!!.updateInit()
        //initialize position
        songPosn = 0
        //random
        rand = Random()
        //create player
        player = MediaPlayer()

        //initialize
        initMusicPlayer()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        context = this
        if (intent.action == "init") {

            Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show()
        }

        // Toast.makeText(this,"1111111111111111111111111111111111111",Toast.LENGTH_LONG ).show()
        if (intent.action == Constants.ACTION.STARTFOREGROUND_ACTION) {
            songs = intent!!.extras!!.getParcelableArrayList("songList");
            songPosn = intent!!.getIntExtra("pos", 0)
            albumId = intent!!.getStringExtra("albumId")!!
            mState = State.Preparing
            playSong()
            showNotification()
            //Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show()

        } else if (intent.action == Constants.ACTION.PREV_ACTION) {
            //Toast.makeText(this, "Clicked Previous", Toast.LENGTH_SHORT).show()
            Log.i(LOG_TAG, "Clicked Previous")
            playPrev()
            showNotification()
        } else if (intent.action == Constants.ACTION.PLAY_ACTION) {
            //Toast.makeText(this, "Clicked Play", Toast.LENGTH_SHORT).show()
            Log.i(LOG_TAG, "Clicked Play")
            if (isPng())
                pausePlayer()
            else
                go()
            showNotification()
            if (AppController!!.service != null)
                AppController!!.service!!.updatePlay(songPosn)
        } else if (intent.action == Constants.ACTION.NEXT_ACTION) {
            //Toast.makeText(this, "Clicked Next", Toast.LENGTH_SHORT).show()
            Log.i(LOG_TAG, "Clicked Next")
            playNext()
            showNotification()
        } else if (intent.action == Constants.ACTION.STOPFOREGROUND_ACTION) {
            Log.i(LOG_TAG, "Received Stop Foreground Intent")
            //Toast.makeText(this, "Service Stoped", Toast.LENGTH_SHORT).show()
            player!!.reset()
            mState = State.Stopped
            AppController.setUpisDownloading(false)
            stopSelf()
            stopForeground(true)
            mInstance = null

            showNotification()
            if (AppController!!.service != null)
                AppController!!.service!!.updateStop()

        }
        return Service.START_REDELIVER_INTENT
    }

    //binder
    inner class MusicBinder : Binder() {
        internal val service: MusicPlayerService
            get() = this@MusicPlayerService
    }

    //activity will bind to service
    override fun onBind(intent: Intent): IBinder? {
        return musicBind
    }

    //release resources when unbind
    override fun onUnbind(intent: Intent): Boolean {
        player!!.stop()
        player!!.release()
        return false
    }

    fun initMusicPlayer() {
        //set player properties
        player!!.setWakeMode(applicationContext,
                PowerManager.PARTIAL_WAKE_LOCK)
        player!!.setAudioStreamType(AudioManager.STREAM_MUSIC);


        //  audioManager.
        //set listeners
        player!!.setOnPreparedListener(this)
        player!!.setOnCompletionListener(this)
        player!!.setOnErrorListener(this)

    }

    //pass song list
    fun setList(theSongs: ArrayList<SongsData>) {
        songs = theSongs
    }

    fun adjustHeadphone(leftVolume : Float, rightVolume : Float) {
        player?.setVolume(leftVolume, rightVolume)
    }

    //play a song
    @RequiresApi(Build.VERSION_CODES.M)
    fun playSong() {
        //play
        player!!.reset()
        // player!!.stop()
        // player!!.start()
        //get song

        val playSong = songs!!.get(songPosn)
        //get title
        songTitle = playSong!!.title + " " + playSong!!.subtitle

        //set the data source
        try {
            mState = State.Preparing


            Log.d(TAG, playSong.songUrl)

            if (playSong.localFileUri.isNotEmpty())
            {
                player!!.setDataSource(playSong.localFileUri)
                player!!.isLooping = AppController.mAppController!!.getLooping()
                currentDownLoadPlayUrl = playSong.localFileUri

            }else {
                if (playSong.songUrl.contains("http")) {
                    player!!.isLooping = AppController.mAppController!!.getLooping()
                    player!!.setDataSource(playSong.songUrl)
                    player!!.setAudioStreamType(AudioManager.STREAM_MUSIC)

                } else {
                    player!!.setDataSource(playSong.songUrl)
                    player!!.isLooping = AppController.mAppController!!.getLooping()
                    currentDownLoadPlayUrl = playSong.songUrl
                }
            }
            mediaFocusChange()
        } catch (e: Exception) {
            Log.d(TAG, "Error setting data source")
        }
        try {
            player!!.prepareAsync()
        } catch (e: Exception) {
            Log.d(TAG, e.localizedMessage)
        }
        // player!!.getPlaybackParams().setPitch(pitch)

        player!!.playbackParams = player!!.getPlaybackParams().setSpeed(AppController.mAppController!!.getPitch());
    }

    //set the song
    fun setSong(songIndex: Int) {
        songPosn = songIndex
    }

    fun getSelectedSongPosition(): Int {
        return songPosn
    }

    //playback methods
    fun getPosn(): Int {
        return player!!.getCurrentPosition()
    }

    fun setLooping()
    {
        player!!.isLooping = AppController.mAppController!!.getLooping()
    }

    fun getDur(): Int {
        return player!!.getDuration()
    }

    fun isPng(): Boolean {
        return player!!.isPlaying()
    }

    fun pausePlayer() {
        mState = State.Paused
        player!!.pause()

        // showNotification()
    }

    fun seek(posn: Int) {
        player!!.seekTo(posn)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun go() {
        mState = State.Playing
        player!!.start()
        if (player!!.isPlaying)
        player!!.setPlaybackParams(player!!.getPlaybackParams().setSpeed(AppController.mAppController!!.getPitch()));


        showNotification()
    }

    //skip to previous track
    fun playPrev() {
        if (songPosn != 0) {
            songPosn--
            if (songPosn < 0)
                songPosn = songs!!.size - 1
            playSong()
            showNotification()
            if (AppController!!.service != null)
                AppController!!.service!!.updatePrev(songPosn)
        }
    }




    fun setPich() {
             try {
                 if (player!=null&&player!!.isPlaying)
                 player!!.setPlaybackParams(player!!.getPlaybackParams().setSpeed(AppController.mAppController!!.getPitch()));
             }catch (e:IllegalStateException)
             {
                 e.printStackTrace()
             }


    }

    //skip to next
    @RequiresApi(Build.VERSION_CODES.M)
    fun playNext() {
        songPosn++
        if (songPosn >= songs!!.size) songPosn = 0
        if (AppController!!.service != null)
            AppController!!.service!!.updateNext(songPosn)
        showNotification()
        playSong()
    }


    override fun onCompletion(mp: MediaPlayer) {
        //check if playback has reached the end of a track
        if (songPosn < songs!!.size-1) {
            if (player!!.getCurrentPosition() > 0) {
                mp.reset()
                playNext()
            }
        }else
        {
            pausePlayer()
        }
    }

    override fun onError(mp: MediaPlayer, what: Int, extra: Int): Boolean {
        Log.v("MUSIC PLAYER", "Playback Error")
        mp.reset()
        return false
    }

    override fun onPrepared(mp: MediaPlayer) {
        //start playback
        if (AppController!!.service != null)
            AppController!!.service!!.updatePrep()
        mState = State.Playing
        mp.start()
        showNotification()
    }


    internal lateinit var status: Notification
    internal lateinit var notificationIntent: Intent
    private var views: RemoteViews? = null
    private var bigViews: RemoteViews? = null
    private fun showNotification() {
        // Using RemoteViews to bind custom layouts into Notification
        views = RemoteViews(packageName,
                R.layout.status_bar)
        bigViews = RemoteViews(packageName,
                R.layout.status_bar_expanded)

        // showing default album image
        views!!.setViewVisibility(R.id.status_bar_icon, View.GONE)

        views!!.setImageViewBitmap(R.id.status_bar_album_art,
                Utility.getDefaultAlbumArt(context!!))
        bigViews!!.setImageViewBitmap(R.id.status_bar_album_art,
                Utility.getDefaultAlbumArt(context!!))


        notificationIntent = Intent(this, SplashActivity::class.java)
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)


        val pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, PendingIntent.FLAG_IMMUTABLE)

        val previousIntent = Intent(this, MusicPlayerService::class.java)
        previousIntent.action = Constants.ACTION.PREV_ACTION
        val ppreviousIntent = PendingIntent.getService(this, 0,
                previousIntent, PendingIntent.FLAG_IMMUTABLE)

        val playIntent = Intent(this, MusicPlayerService::class.java)
        playIntent.action = Constants.ACTION.PLAY_ACTION
        val pplayIntent = PendingIntent.getService(this, 0,
                playIntent, PendingIntent.FLAG_IMMUTABLE)

        val nextIntent = Intent(this, MusicPlayerService::class.java)
        nextIntent.action = Constants.ACTION.NEXT_ACTION
        val pnextIntent = PendingIntent.getService(this, 0,
                nextIntent, PendingIntent.FLAG_IMMUTABLE)

        val closeIntent = Intent(this, MusicPlayerService::class.java)
        closeIntent.action = Constants.ACTION.STOPFOREGROUND_ACTION
        val pcloseIntent = PendingIntent.getService(this, 0,
                closeIntent, PendingIntent.FLAG_IMMUTABLE)

        views!!.setOnClickPendingIntent(R.id.status_bar_play, pplayIntent)
        bigViews!!.setOnClickPendingIntent(R.id.status_bar_play, pplayIntent)

        views!!.setOnClickPendingIntent(R.id.status_bar_next, pnextIntent)
        bigViews!!.setOnClickPendingIntent(R.id.status_bar_next, pnextIntent)

        views!!.setOnClickPendingIntent(R.id.status_bar_prev, ppreviousIntent)
        bigViews!!.setOnClickPendingIntent(R.id.status_bar_prev, ppreviousIntent)

        views!!.setOnClickPendingIntent(R.id.status_bar_collapse, pcloseIntent)
        bigViews!!.setOnClickPendingIntent(R.id.status_bar_collapse, pcloseIntent)

        views!!.setImageViewResource(R.id.status_bar_play,
                R.mipmap.player)
        bigViews!!.setImageViewResource(R.id.status_bar_play,
                R.mipmap.player)

        if (isPng()) {
            views!!.setImageViewResource(R.id.status_bar_play,
                    R.mipmap.player)
            bigViews!!.setImageViewResource(R.id.status_bar_play,
                    R.mipmap.player)
        } else {
            views!!.setImageViewResource(R.id.status_bar_play,
                    R.mipmap.pause)
            bigViews!!.setImageViewResource(R.id.status_bar_play,
                    R.mipmap.pause)
        }


        views!!.setTextViewText(R.id.status_bar_track_name, songs!!.get(songPosn)!!.songTitle)
        bigViews!!.setTextViewText(R.id.status_bar_track_name, songs!!.get(songPosn)!!.songTitle)
        val channelId ="MyChanelId";

         var  mBuilder:NotificationCompat.Builder

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            createNotificationChannel(channelId,"Choraline")
            mBuilder = NotificationCompat.Builder(context!!,channelId)


        }else
        {
            mBuilder = NotificationCompat.Builder(context!!)
        }

        status = mBuilder
                .setContentIntent(pendingIntent)
                .setContent(views)
                .setCustomBigContentView(bigViews)
                .setSmallIcon(R.mipmap.notification_icon)
                .setLargeIcon(BitmapFactory.decodeResource(context!!.getResources(), R.mipmap.app_icon))
                .build()

        status.flags = Notification.FLAG_ONGOING_EVENT
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
         //  notificationmanager.notify(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE,status)
        }
        startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, status)
    }


@RequiresApi(Build.VERSION_CODES.O)
private fun createNotificationChannel(channelId: String, channelName: String): NotificationChannel{
    val chan = NotificationChannel(channelId,
            channelName, NotificationManager.IMPORTANCE_NONE)
    chan.lightColor = Color.BLUE
    chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
     notificationmanager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationmanager.createNotificationChannel(chan)
    return chan
}
    override fun onDestroy() {

        stopForeground(true)
    }

    interface updatePrepared {
        fun updateInit()
        fun updatePrep()
        fun updatePlay(pos: Int)
        fun updatePrev(pos: Int)
        fun updateNext(pos: Int)
        fun updateStop()
    }

    /**
     * Method checks if the app is in background or not
     */
    fun isAppIsInBackground(context: Context): Boolean {
        var isInBackground = true
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            val runningProcesses = am.runningAppProcesses
            for (processInfo in runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (activeProcess in processInfo.pkgList) {
                        if (activeProcess == context.packageName) {
                            isInBackground = false
                        }
                    }
                }
            }
        } else {
            val taskInfo = am.getRunningTasks(1)
            val componentInfo = taskInfo[0].topActivity
            if (componentInfo!!.packageName == context.packageName) {
                isInBackground = false
            }
        }

        return isInBackground
    }


    fun mediaFocusChange() {
        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager


        audioManager.requestAudioFocus(audioFocusChangeLissten, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN)

    }

    val audioFocusChangeLissten = AudioManager.OnAudioFocusChangeListener()
    {
        when (it) {

            AudioManager.AUDIOFOCUS_LOSS -> {

                Log.d(LOG_TAG, "111111111111111111111111111111111111111111111111111111111111111")


                if (player != null) {
                    pausePlayer()
                    stopSelf()
                }


            }
            AudioManager.AUDIOFOCUS_GAIN -> {
                Log.d(LOG_TAG, "2222222222222222222222222222222222222222222222222222222222222222222222")

                // if (player!=null) go()


            }
        }
    }


    /**
     * This function use to on left and right headset volume
     * @param leftVolume
     * @param rightVolume
     */
    fun setVolume(leftVolume: Float, rightVolume: Float) {
        player?.setVolume(leftVolume, rightVolume)
    }

}
