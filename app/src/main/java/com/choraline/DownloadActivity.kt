package com.choraline

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.recyclerview.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.CompoundButton
import android.widget.SeekBar
import com.choraline.adapters.DownloadedAdapter
import com.choraline.models.SongsData
import com.choraline.services.MusicPlayerService
import com.choraline.utils.AppController
import com.choraline.utils.Constants
import com.choraline.utils.Utility
import kotlinx.android.synthetic.main.activity_download.*
import kotlinx.android.synthetic.main.layout_player.*
import kotlinx.android.synthetic.main.pitch_dialogue.*
import kotlinx.android.synthetic.main.pitch_dialogue.view.*
import kotlinx.android.synthetic.main.pitch_dialogue.view.seekbar_adjust_volume


import java.io.File
import android.widget.Toast


class DownloadActivity : BaseActivity(), MusicPlayerService.updatePrepared {

    var isLeftSelected = false
    var isRightSelected = false

    override fun updateInit() {
        musicPlayerService = MusicPlayerService!!.mInstance
    }

    override fun updatePrep() {
        player_progressLoading!!.visibility = View.GONE
        updateProgressBar()
        player_imgbtnPlayStop.setImageResource(R.mipmap.player)
    }

    override fun updatePlay(flag: Int) {

        if (musicPlayerService!!.mState!!.toString()
                .equals(MusicPlayerService.State.Playing.toString())
        ) {
            player_imgbtnPlayStop.setImageResource(R.mipmap.player)
        } else if (musicPlayerService!!.mState!!.toString()
                .equals(MusicPlayerService.State.Paused.toString())
        ) {
            player_imgbtnPlayStop.setImageResource(R.mipmap.pause)
        }
    }

    fun initPlayer() {

        player_imgbtnPlayStop.setOnClickListener {

            if (musicPlayerService!!.mState.toString()
                    .equals(MusicPlayerService.State.Playing.toString())
            ) {
                player_imgbtnPlayStop.setImageResource(R.mipmap.pause)
                val i = Intent(context, MusicPlayerService::class.java)
                i!!.action = Constants.ACTION.PLAY_ACTION
                startService(i)
            } else {
                player_imgbtnPlayStop.setImageResource(R.mipmap.player)
                val i = Intent(context, MusicPlayerService::class.java)
                i!!.action = Constants.ACTION.PLAY_ACTION
                startService(i)
            }
        }

        player_imgbtnPrevious.setOnClickListener {

            if (selectedPosition - 1 >= 0) {
                selectedPosition--
                player_seekbar!!.progress = 0
                player_seekbar!!.max = 100
                val i = Intent(context, MusicPlayerService::class.java)
                i!!.action = Constants.ACTION.PREV_ACTION
                startService(i)
            }


        }

        player_imgbtnNext.setOnClickListener {
            player_seekbar!!.progress = 0
            player_seekbar!!.max = 100
            val i = Intent(context, MusicPlayerService::class.java)
            i!!.action = Constants.ACTION.NEXT_ACTION
            startService(i)
        }

        player_imgbtnClose.setOnClickListener {
            mHandler.removeCallbacks(updateTimeTask)
            adapter.refreshList(-1)
            purchasedmusicsongs_layoutPlayer.setVisibility(View.GONE)
            val i = Intent(context, MusicPlayerService::class.java)
            i!!.action = Constants.ACTION.STOPFOREGROUND_ACTION
            startService(i)
            musicPlayerService = null

        }

        player_seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                mHandler.removeCallbacks(updateTimeTask)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                mHandler.removeCallbacks(updateTimeTask);
                val totalDuration = musicPlayerService!!.getDur()
                val currentPosition = Utility.progressToTimer(seekBar.getProgress(), totalDuration);
                musicPlayerService!!.seek(currentPosition);
                updateProgressBar();
            }
        })
    }

    override fun updateNext(pos: Int) {
        if (mHandler != null)
            mHandler.removeCallbacks(updateTimeTask)
        selectedPosition = pos
        player_progressLoading.visibility = View.VISIBLE
        //  if(!musicPlayerService!!.albumId.equals("") and musicPlayerService!!.albumId.equals(selectedAlbumId))
        adapter!!.refreshList(selectedPosition)

    }

    override fun updatePrev(pos: Int) {
        if (mHandler != null)
            mHandler.removeCallbacks(updateTimeTask)
        selectedPosition = pos
        player_progressLoading.visibility = View.VISIBLE
        // if(!musicPlayerService!!.albumId.equals("") and musicPlayerService!!.albumId.equals(selectedAlbumId))
        adapter!!.refreshList(selectedPosition)
    }

    var isHide = false
    override fun updateStop() {
        mHandler.removeCallbacks(updateTimeTask)
        adapter.refreshList(-1)

        if (!isHide) {
            purchasedmusicsongs_layoutPlayer.setVisibility(View.GONE)
            musicPlayerService = null
        }
        isHide = false
    }

    fun updateProgressBar() {
        mHandler.postDelayed(updateTimeTask, 100)
    }

    private val updateTimeTask = object : Runnable {
        override fun run() {
            val totalDuration = musicPlayerService!!.getDur()
            val currentDuration = musicPlayerService!!.getPosn()

            val progress =
                Utility.getProgressPercentage(currentDuration.toLong(), totalDuration.toLong())
            player_seekbar.progress = progress

            Utility.showTime(
                currentDuration,
                txt_start_time,
                totalDuration,
                txt_end_time
            )// show time

            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100)
        }
    }
    lateinit var adapter: DownloadedAdapter;
    val list = ArrayList<SongsData>()
    private var musicPlayerService: MusicPlayerService? = null
    private val mHandler = Handler();
    var selectedPosition = -1
    private var selectedAlbumId: String = ""
    private var isPlaying = false
    private lateinit var context: Context
    var path = "";

    companion object {
        var isEdit = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)
        AppController!!.setupInteface(this)
        context = this
        isPlaying = false
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        delete_all_songs.visibility = View.GONE


        purchasedmusicsongs_txtVoiceType.setText(intent.getStringExtra(Constants.AppConstants.TOOLBARTILE))
        isEdit = false

        if (AppController.mAppController!!.getLooping()) {
            loop_image_view.setBackgroundResource(R.mipmap.loop_active)

        } else {
            loop_image_view.setBackgroundResource(R.mipmap.loop_inactive)
        }


        delete_all_songs.setOnClickListener {


            val builder = AlertDialog.Builder(context)
            builder.setMessage("Are you sure you wish to delete all songs?")
                .setPositiveButton("Confirm", DialogInterface.OnClickListener { dialog, id ->
                    // FIRE ZE MISSILES!
//                        val filedir = File(intent.getStringExtra(Constants.AppConstants.PATH))
//                        val files = filedir.listFiles()


                    val appSpecificInternalStorageDirectory: File = this.getFilesDir()
                    val files = File(appSpecificInternalStorageDirectory, "").listFiles()



                    for (i in files.indices) {
                        try {
                            File(files[i].absolutePath).delete()
                        } catch (e: Exception) {

                        }
                    }
                    list.clear()
                    updateToolbarEdit()
                    dialog.dismiss()

                })
                .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->
                    // User cancelled the dialog
                    dialog.dismiss()
                })

            builder.create()
            builder.show()


        }



        loop_image_view.setOnClickListener {
            AppController.mAppController!!.setLooping()
            if (musicPlayerService != null) {
                musicPlayerService!!.setLooping()
            }


            if (AppController.mAppController!!.getLooping()) {
                loop_image_view.setBackgroundResource(R.mipmap.loop_active)

            } else {
                loop_image_view.setBackgroundResource(R.mipmap.loop_inactive)
            }
        }
        //list.addAll((applicationContext as AppController).songlist)

        change_pitch.setOnClickListener {
            pitchDialogue()
        }

        toolbar_edit.setOnClickListener {
            isEdit = !isEdit
            if (isEdit) {
                toolbar_edit.text = "Done"
                delete_all_songs.visibility = View.VISIBLE
                change_pitch.visibility = View.GONE

            } else {
                toolbar_edit.text = "Edit"
                delete_all_songs.visibility = View.GONE
                change_pitch.visibility = View.VISIBLE
            }
            purchasedmusicsongs_layoutPlayer.setVisibility(View.GONE)
            player_progressLoading.visibility = View.GONE

            adapter.notifyDataSetChanged()
            if (musicPlayerService != null)
                if (musicPlayerService!!.mState.toString()
                        .equals(MusicPlayerService.State.Playing.toString())
                ) {
                    mHandler.removeCallbacks(updateTimeTask)
                    adapter.refreshList(-1)
                    purchasedmusicsongs_layoutPlayer.setVisibility(View.GONE)
                    val i = Intent(context, MusicPlayerService::class.java)
                    i!!.action = Constants.ACTION.STOPFOREGROUND_ACTION
                    startService(i)

                } else {
                    /* player_imgbtnPlayStop.setImageResource(R.mipmap.player)
                     val i=Intent(context, MusicPlayerService::class.java)
                     i!!.action=Constants.ACTION.PLAY_ACTION
                     startService(i)*/
                }

        }
        path = intent.getStringExtra(Constants.AppConstants.PATH)!!
//        val filedir = File(intent.getStringExtra(Constants.AppConstants.PATH))
//        val files = filedir.listFiles()


//        var files: Array<File>? = null;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val appSpecificInternalStorageDirectory: File = context.getDir("Choralin", Context.MODE_PRIVATE)
           var  files = File(appSpecificInternalStorageDirectory, path).listFiles()

//        } else {
//            val filedir = File(intent.getStringExtra(Constants.AppConstants.PATH))
//            files = filedir.listFiles()
//        }





        if (files != null) {
            for (i in 0 until files.size) {
                Log.d("Files", "FileName:" + files[i].name)
                var model = SongsData()
                var ar = files[i].name.split(Constants.DELIMITER)
                model.songTitle = files[i].name
                if (ar.size > 1) {
                    model.title = ar[1]
                    model.id = ar[0]
                    model.songTitle = ar[1]
                } else {
                    model.title = ""
                    model.id = ""
                }

                model.songUrl = files[i].absolutePath

                list.add(model)


            }
        }

        setUprecyclerView()
        initPlayer()
        updateToolbarEdit()

        text_left.setOnClickListener {
            leftSelected(isRightSelected)
        }

        text_right.setOnClickListener {
            rightSelected(isLeftSelected)
        }

    }

    private fun rightSelected(isSelected: Boolean) {
        if (isSelected) {
            musicPlayerService?.setVolume(1f, 1f)
            text_left.isSelected = false
        }

        if (text_right.isSelected) {
            musicPlayerService?.setVolume(1f, 1f)
            text_right.isSelected = false
            isRightSelected = false
        } else {
            musicPlayerService?.setVolume(0f, 1f)
            text_right.isSelected = true
            isRightSelected = true
        }
    }

    private fun leftSelected(isSelected: Boolean) {

        if (isSelected) {
            musicPlayerService?.setVolume(1f, 1f)
            text_right.isSelected = false
        }


        if (text_left.isSelected) {
            musicPlayerService?.setVolume(1f, 1f)
            text_left.isSelected = false
            isLeftSelected = false
        } else {
            musicPlayerService?.setVolume(1f, 0f)
            text_left.isSelected = true
            isLeftSelected = true
        }
    }


    fun pitchDialogue() {
        val builder = AlertDialog.Builder(this)

        var view = LayoutInflater.from(this).inflate(R.layout.pitch_dialogue, null)
        builder.setView(view)

        when {
            AppController.mAppController!!.getPitch() == .9f -> view.point_five_pitch_radio.isChecked =
                true
            AppController.mAppController!!.getPitch() == 0.75f -> view.point_seven_five_pitch.isChecked =
                true
            else -> view.one_pitch_radio.isChecked = true
        }


        view.seekbar_adjust_volume.progress = AppController.mAppController!!.getVolumeProgress()

        var progressChangedValue = 0
        var leftVolume = 0f
        var rightVolume = 0f
        var isAdjustVolumeClicked: Boolean = false


        view.seekbar_adjust_volume.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                progressChangedValue = progress
                when {
                    progress < 50 -> {
                        leftVolume = 1f
                        rightVolume = 0f
                    }
                    progress in 45..55 -> {
                        leftVolume = 1f
                        rightVolume = 1f
                    }
                    else -> {
                        leftVolume = 0f
                        rightVolume = 1f
                    }
                }

                isAdjustVolumeClicked = true
                musicPlayerService?.setVolume(leftVolume, rightVolume)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // TODO Auto-generated method stub
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })

        /* when{
             AppController.mAppController?.getLeftVolume() == 1 ->
         }*/


        /*    builder.setPositiveButton("Apply", DialogInterface.OnClickListener { dialog, id ->
            if (view.point_five_pitch_radio.isChecked)
                AppController!!.mAppController!!.setPich(0.5f)
            else if (view.point_seven_five_pitch.isChecked)
                AppController!!.mAppController!!.setPich(0.75f)
            else  AppController!!.mAppController!!.setPich(1.0f)

            if (musicPlayerService!=null)
            {
                musicPlayerService!!.setPich()
            }
        })

        builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->
          dialog.dismiss()
        })
        builder.show()*/

        val dialog = builder.show()


        view.seekbar_adjust_volume.progress = 50
        view.switch_default.setOnClickListener {
            musicPlayerService?.setVolume(1.0f, 1.0f)
            view.seekbar_adjust_volume.progress = 50
        }

        dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        view.apply_tv.setOnClickListener {
            if (view.point_five_pitch_radio.isChecked)
                AppController.mAppController!!.setPich(0.9f)
            else if (view.point_seven_five_pitch.isChecked)
                AppController.mAppController!!.setPich(0.75f)
            else AppController.mAppController!!.setPich(1.0f)

            if (musicPlayerService != null) {
                musicPlayerService!!.setPich()
            }
/*
            if(isAdjustVolumeClicked) {
                AppController.mAppController!!.setVolumeProgress(progressChangedValue)
                musicPlayerService?.setVolume(leftVolume, rightVolume)
            }*/

            dialog.dismiss()
        }


        view.cancel_tv.setOnClickListener { dialog.dismiss() }

    }


    fun updateToolbarEdit() {
        deletefolder()
        if (list.size == 0) {
            toolbar_edit.visibility = View.INVISIBLE
        } else {
            toolbar_edit.visibility = View.VISIBLE
        }
    }


    //start and bind the service when the activity starts
    override fun onStart() {
        super.onStart()
        musicPlayerService = MusicPlayerService!!.mInstance
        if (musicPlayerService != null) {
            if ((musicPlayerService!!.mState.toString()
                    .equals(MusicPlayerService.State.Playing.toString())) or
                musicPlayerService!!.mState.toString()
                    .equals(MusicPlayerService.State.Paused.toString()) or
                (musicPlayerService!!.mState.toString()
                    .equals(MusicPlayerService.State.Preparing.toString()))
            ) {
                purchasedmusicsongs_layoutPlayer!!.visibility = View.VISIBLE
                if ((musicPlayerService!!.mState.toString()
                        .equals(MusicPlayerService.State.Preparing.toString()))
                ) {
                    player_progressLoading!!.visibility = View.VISIBLE
                    player_seekbar!!.progress = 0
                    player_seekbar!!.max = 100
                    mHandler.removeCallbacks(updateTimeTask)
                } else {
                    player_progressLoading!!.visibility = View.GONE
                    updateProgressBar()
                }

                var songData = SongsData()
                songData.songUrl = musicPlayerService!!.currentDownLoadPlayUrl
                var pos = list.indexOf(songData)

                if (pos > -1) {


                    adapter!!.refreshList(pos)
                }

                if (musicPlayerService!!.mState.toString()
                        .equals(MusicPlayerService.State.Paused.toString())
                )
                    player_imgbtnPlayStop!!.setImageResource(R.mipmap.pause)
            }
        }
    }

    fun setUprecyclerView() {

        adapter = DownloadedAdapter(list, this)
        downloaded_song_recycler_view.adapter = adapter
        var manger = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        downloaded_song_recycler_view.layoutManager = manger
        downloaded_song_recycler_view.setHasFixedSize(true)
        // AppController.setUpisDownloading(true)
        adapter.notifyDataSetChanged()


    }

    inner class MyAsyncTask : AsyncTask<Void, Void, Void>() {
        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg params: Void?): Void? {
            Thread.sleep(100)
            return null
        }

        @Deprecated("Deprecated in Java")
        override fun onPostExecute(result: Void?) {
            playsong(selectedPosition)

        }

    }

    fun stopServices() {
        try {
            mHandler.removeCallbacks(updateTimeTask)
            adapter.refreshList(-1)

            val i = Intent(context, MusicPlayerService::class.java)
            i!!.action = Constants.ACTION.STOPFOREGROUND_ACTION
            startService(i)


        } catch (e: Exception) {

        }
    }

    open fun playsong(pos: Int) {
        selectedPosition = pos

        if (musicPlayerService == null) musicPlayerService = MusicPlayerService.mInstance

        if (AppController.isDownloadSongPlaying && !isPlaying) {
            isHide = true

            AppController.setUpisDownloading(false)
            stopServices()
            val myAsync = MyAsyncTask()
            myAsync.execute()
            return
        }

        AppController.setUpisDownloading(true)
        isPlaying = true
        isHide = false

        if (mHandler != null)
            mHandler.removeCallbacks(updateTimeTask)

        if (list.get(pos).isPlaying) {
            return
        }

        for (i in 0..list.size - 1) {
            list.get(i).isPlaying = false
        }
        list.get(pos).isPlaying = true

        purchasedmusicsongs_layoutPlayer.setVisibility(View.VISIBLE)

        player_progressLoading.visibility = View.VISIBLE


        //on complete
        player_imgbtnPlayStop.setImageResource(R.mipmap.pause)
        list.get(selectedPosition).isPlaying = false;
        adapter.refreshList(-1);

        //player.start()
        adapter.refreshList(pos)
        player_seekbar.setProgress(0);
        player_seekbar.setMax(100);
        //updateProgressBar();
        purchasedmusicsongs_layoutPlayer.setVisibility(View.VISIBLE)
        player_progressLoading.visibility = View.VISIBLE
        val i = Intent(this, MusicPlayerService::class.java)
        i!!.action = Constants.ACTION.STARTFOREGROUND_ACTION
        var b = Bundle();
        b!!.putParcelableArrayList("songList", list)
        i!!.putExtras(b)
        i!!.putExtra("albumId", selectedAlbumId);
        i!!.putExtra("pos", pos)
        startService(i)
    }

    open fun deletefolder() {
        try {
            if (list.size == 0) {

                val appSpecificInternalStorageDirectory: File = this.getFilesDir()
//                val files = File(appSpecificInternalStorageDirectory, "").listFiles()


//                val filedir = File(intent.getStringExtra(Constants.AppConstants.PATH))
                appSpecificInternalStorageDirectory.delete()
                onBackPressed()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
