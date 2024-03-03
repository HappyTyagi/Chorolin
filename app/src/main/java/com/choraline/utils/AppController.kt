package com.choraline.utils

import android.app.Application
import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import android.os.Environment
import android.util.Log
import androidx.multidex.MultiDex
import com.choraline.database.DatabaseHandler
import com.choraline.models.SongsData
import com.choraline.services.MusicPlayerService
import java.io.File


/**
 * Created by root on 6/3/17.
 */
class AppController : Application() {


    private var pitch: Float = 1.0f
    private var isLooping = false
    private var leftVolume: Float = 0f
    private var rightVolume: Float = 0f
    private var volumeProgress: Int = 0

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        //  Realm.init(this)
        // val config = RealmConfiguration.Builder().name("choraline.realm").build()
        // Realm.setDefaultConfiguration(config)
        mAppController = this@AppController
        mPreferenceHelper = PreferenceHelper(mAppController as AppController)


    }


    fun setPich(pitch: Float) {
        this.pitch = pitch
    }

    fun getPitch(): Float {
        return pitch
    }

    fun setLooping() {
        isLooping = !isLooping
    }

    fun getLooping(): Boolean {
        return isLooping
    }


    fun setLeftVolume(leftVolume: Float) {
        this.leftVolume = leftVolume
    }

    fun getLeftVolume(): Float {
        return leftVolume
    }

    fun setRightVolume(rightVolume: Float) {
        this.rightVolume = rightVolume
    }

    fun getRightVolume(): Float {
        return rightVolume
    }

    fun setVolumeProgress(volumeProgress: Int) {
        this.volumeProgress = volumeProgress
    }

    fun getVolumeProgress(): Int {
        return volumeProgress
    }


    companion object {

        var mAppController: AppController? = null
        private var mPreferenceHelper: PreferenceHelper? = null
        private var instance: DatabaseHandler? = null
        var isDownloadSongPlaying = false

        fun setUpisDownloading(isDownloadSongPlaying: Boolean) {
            this.isDownloadSongPlaying = isDownloadSongPlaying

        }

        val appPref: PreferenceHelper
            @Synchronized get() {
                println("mpreferencehelper :: " + mPreferenceHelper)
                println("mAppController :: " + mAppController)
                if (mPreferenceHelper == null) {
                    mPreferenceHelper = PreferenceHelper(appContext)

                }

                return mPreferenceHelper as PreferenceHelper
            }

        val appContext: Context
            get() = mAppController!!.applicationContext;
        var service: MusicPlayerService.updatePrepared? = null


        fun setupInteface(service: MusicPlayerService.updatePrepared) {
            this.service = service
        }


        val dbInstance: DatabaseHandler
            @Synchronized get() {
                if (instance == null)
                    instance = DatabaseHandler(appContext)
                return instance as DatabaseHandler
            }


    }
}