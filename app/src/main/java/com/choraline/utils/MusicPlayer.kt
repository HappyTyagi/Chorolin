package com.choraline.utils

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import com.choraline.R

import java.util.zip.Inflater

/**
 * Created by deepak Tyagi on 8/1/2017.
 */

class MusicPlayer(private val context: Context) {
    private var view: View? = null
    private lateinit var mediaPlayer: MediaPlayer
    fun initUI() {

        mediaPlayer= MediaPlayer()
        mediaPlayer.setOnCompletionListener(object: MediaPlayer.OnCompletionListener{

            override fun onCompletion(mp: MediaPlayer?) {

            }
        })
    }

    fun setDataSource(datasource: String)
    {
        mediaPlayer.setDataSource(datasource)
    }
    /*open fun setDataSource(afd: AssetFileDescriptor)
    {
        mediaPlayer.setDataSource(afd)
    }*/

    fun prepare()
    {
        mediaPlayer.prepare()
    }

    fun start()
    {
        mediaPlayer.start()
    }

    fun pause()
    {
        mediaPlayer.pause()
    }

    fun reset()
    {
        mediaPlayer.reset()
    }

    fun getDuration() : Int
    {
        return mediaPlayer.duration
    }

    fun getCurrentDuration() : Int
    {
        return mediaPlayer.currentPosition
    }

    fun seekTo(msec: Int)
    {
        mediaPlayer.seekTo(msec)
    }

    fun isPlaying() : Boolean
    {
        return mediaPlayer.isPlaying
    }

    fun stop()
    {
        mediaPlayer.stop()
    }

    fun release()
    {
        mediaPlayer.release()
    }





}
