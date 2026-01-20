package com.choraline

import android.content.Context
import android.os.Bundle
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.widget.Toolbar
import android.os.Handler
import android.view.View
import android.widget.SeekBar
import com.choraline.adapters.SongsListAdapter
import com.choraline.models.SongsData
import com.choraline.utils.Utility
import android.content.pm.PackageManager
import android.content.Intent
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.recyclerview.widget.RecyclerView
import com.choraline.services.DownloadService
import com.choraline.services.MusicPlayerService
import com.choraline.utils.AppController
import com.choraline.utils.Constants
import com.google.android.material.snackbar.Snackbar
import java.util.Locale


class FreeTrailSongsActivity : BaseActivity(), View.OnClickListener, MusicPlayerService.updatePrepared {

    var isLeftSelected = false
    var isRightSelected = false

    override fun updateInit() {
        musicPlayerService=MusicPlayerService!!.mInstance
    }
    override fun updatePrep() {
        Utility.updatePlayerView(txt_start_time!!, txt_end_time!!, player_seekbar!!, true)
        player_progressLoading!!.visibility=View.GONE
        updateProgressBar()
        player_imgbtnPlayStop!!.setImageResource(R.mipmap.player)
    }

    override fun updatePlay(flag: Int) {

            if(musicPlayerService!!.mState!!.toString().equals(MusicPlayerService.State.Playing.toString())) {
                player_imgbtnPlayStop!!.setImageResource(R.mipmap.player)
            }
            else if(musicPlayerService!!.mState!!.toString().equals(MusicPlayerService.State.Paused.toString())) {
                player_imgbtnPlayStop!!.setImageResource(R.mipmap.pause)
            }

    }

    override fun updateNext(pos: Int) {
        if(mHandler!=null)
            mHandler.removeCallbacks(updateTimeTask)
        selectedPosition=pos
        player_progressLoading!!.visibility=View.VISIBLE
        //adapter!!.refreshList(selectedPosition)
        if(!musicPlayerService!!.albumId.equals("") and musicPlayerService!!.albumId.equals(selectedVoice))
            adapter!!.refreshList(selectedPosition)
    }

    override fun updatePrev(pos: Int) {
        if(mHandler!=null)
            mHandler.removeCallbacks(updateTimeTask)
        selectedPosition=pos
        player_progressLoading!!.visibility=View.VISIBLE
        //adapter!!.refreshList(selectedPosition)
        if(!musicPlayerService!!.albumId.equals("") and musicPlayerService!!.albumId.equals(selectedVoice))
            adapter!!.refreshList(selectedPosition)
    }

    override fun updateStop() {
        mHandler.removeCallbacks(updateTimeTask)
        adapter.refreshList(-1)
        freetrailsongs_layoutPlayer!!.setVisibility(View.GONE)
    }

    lateinit var context: Context
    var songList = ArrayList<SongsData>()
    private lateinit var adapter: SongsListAdapter
    private val mHandler = Handler();
    private var selectedPosition: Int =-1
    private var selectedDownloadPosition=-1;
    private val PERMISSION_REQUEST_CODE = 11
    private var selectedVoice: String = ""

    private var musicPlayerService: MusicPlayerService? = null


    var loop_image_view : ImageView? = null
    var player_imgbtnPlayStop : ImageButton? = null
    var player_imgbtnPrevious : ImageButton? = null
    var player_imgbtnNext : ImageButton? = null
    var player_progressLoading : ProgressBar? = null
    var tootlbar_imgbtnShare : ImageButton? = null
    var text_left : TextView? = null
    var text_right : TextView? = null
    var txt_start_time : TextView? = null
    var txt_end_time : TextView? = null
    var player_seekbar : AppCompatSeekBar? = null
    var layoutParent : RelativeLayout? = null
    var player_imgbtnClose : ImageButton? = null

    var freetrailsongs_btnJoin : Button? = null
    var freetrailsongs_txtVoiceType : TextView? = null
    var freetrailsongs_recyclerSongs : RecyclerView? = null
    var freetrailsongs_layoutPlayer : RelativeLayout? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_free_trail_songs)
        context=this@FreeTrailSongsActivity
        AppController!!.setupInteface(this)
        //player.initUI()
        freetrailsongs_btnJoin = findViewById(R.id.freetrailsongs_btnJoin)
        freetrailsongs_txtVoiceType = findViewById(R.id.freetrailsongs_txtVoiceType)
        player_imgbtnClose = findViewById(R.id.player_imgbtnClose)
        layoutParent = findViewById(R.id.layoutParent)
        player_progressLoading = findViewById(R.id.player_progressLoading)
        txt_start_time = findViewById(R.id.txt_start_time)
        txt_end_time = findViewById(R.id.txt_end_time)
        player_imgbtnNext = findViewById(R.id.player_imgbtnNext)
        player_imgbtnPrevious = findViewById(R.id.player_imgbtnPrevious)
        player_imgbtnPlayStop = findViewById(R.id.player_imgbtnPlayStop)
        loop_image_view = findViewById(R.id.loop_image_view)
        tootlbar_imgbtnShare = findViewById(R.id.tootlbar_imgbtnShare)
        text_left = findViewById(R.id.text_left)
        text_right = findViewById(R.id.text_right)
        tootlbar_imgbtnShare = findViewById(R.id.tootlbar_imgbtnShare)
        player_seekbar = findViewById(R.id.player_seekbar)
        freetrailsongs_layoutPlayer = findViewById(R.id.freetrailsongs_layoutPlayer)
        freetrailsongs_recyclerSongs = findViewById(R.id.freetrailsongs_recyclerSongs)


        if(intent!=null)
        {
            if(intent.extras!=null) {
                songList = intent.extras!!.getParcelableArrayList<SongsData>("songsList")!!
                selectedVoice = intent.extras!!.getString("voiceType")!!
            }
        }
        if (AppController.mAppController!!.getLooping())
        {
            loop_image_view!!.setBackgroundResource(R.mipmap.loop_active)

        }else
        {
            loop_image_view!!.setBackgroundResource(R.mipmap.loop_inactive)
        }

        loop_image_view!!.setOnClickListener{
            AppController.mAppController!!.setLooping()
            if (musicPlayerService!=null)
            {
                musicPlayerService!!.setLooping()
            }


            if (AppController.mAppController!!.getLooping())
            {
                loop_image_view!!.setBackgroundResource(R.mipmap.loop_active)

            }else
            {
                loop_image_view!!.setBackgroundResource(R.mipmap.loop_inactive)
            }
        }

        freetrailsongs_txtVoiceType!!.setText(selectedVoice.uppercase(Locale.getDefault()))
        adapter= SongsListAdapter(context, this@FreeTrailSongsActivity, songList,0)
        val toolbar = findViewById<Toolbar>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        player_seekbar!!.setPadding(0,0,0,0)
        initUI()
        initPlayer()
    }
    fun initUI()
    {
        val mLayoutManager = LinearLayoutManager(context)
        freetrailsongs_recyclerSongs!!.setLayoutManager(mLayoutManager)
        freetrailsongs_recyclerSongs!!.setItemAnimator(DefaultItemAnimator())
        freetrailsongs_recyclerSongs!!.setAdapter(adapter)

        tootlbar_imgbtnShare!!.setOnClickListener(this)
        freetrailsongs_btnJoin!!.setOnClickListener(this)

        text_left!!.setOnClickListener {
            leftSelected(isRightSelected)
        }

        text_right!!.setOnClickListener {
            rightSelected(isLeftSelected)
        }

    }

    private fun rightSelected(isSelected: Boolean) {
        if (isSelected){
            musicPlayerService?.setVolume(1f, 1f)
            text_left!!.isSelected = false
        }

        if (text_right!!.isSelected) {
            musicPlayerService?.setVolume(1f, 1f)
            text_right!!.isSelected = false
            isRightSelected = false
        } else {
            musicPlayerService?.setVolume(0f, 1f)
            text_right!!.isSelected = true
            isRightSelected = true
        }
    }

    private fun leftSelected(isSelected: Boolean) {

        if (isSelected){
            musicPlayerService?.setVolume(1f, 1f)
            text_right!!.isSelected = false
        }


        if (text_left!!.isSelected) {
            musicPlayerService?.setVolume(1f, 1f)
            text_left!!.isSelected = false
            isLeftSelected = false
        } else {
            musicPlayerService?.setVolume(1f, 0f)
            text_left!!.isSelected = true
            isLeftSelected = true
        }
    }

    //start and bind the service when the activity starts
    override fun onStart() {
        super.onStart()
        musicPlayerService=MusicPlayerService!!.mInstance
        if(musicPlayerService!=null)
        {
            if((musicPlayerService!!.mState.toString().equals(MusicPlayerService.State.Playing.toString())) or
                    musicPlayerService!!.mState.toString().equals(MusicPlayerService.State.Paused.toString()) or
                    (musicPlayerService!!.mState.toString().equals(MusicPlayerService.State.Preparing.toString())))
            {
                freetrailsongs_layoutPlayer!!.visibility=View.VISIBLE
                if((musicPlayerService!!.mState.toString().equals(MusicPlayerService.State.Preparing.toString())))
                {
                    player_progressLoading!!.visibility=View.VISIBLE
                    player_seekbar!!.progress=0
                    player_seekbar!!.max=100
                    mHandler.removeCallbacks(updateTimeTask)
                }
                else
                {
                    player_progressLoading!!.visibility=View.GONE
                    updateProgressBar()
                }

                if(!musicPlayerService!!.albumId.equals("") and musicPlayerService!!.albumId.equals(selectedVoice))
                    adapter!!.refreshList(musicPlayerService!!.getSelectedSongPosition())

                if(musicPlayerService!!.mState.toString().equals(MusicPlayerService.State.Paused.toString()))
                    player_imgbtnPlayStop!!.setImageResource(R.mipmap.pause)
            }
        }
    }

    override fun onClick(v: View?) {

        if(v==tootlbar_imgbtnShare)
        {
            Utility!!.shareApp(context)
        }
        else if(v==freetrailsongs_btnJoin)
        {
            val intents=Intent(context, SignupActivity::class.java)
            startActivity(intents)
        }
    }

    fun initPlayer()
    {


        player_imgbtnPlayStop!!.setOnClickListener{

            if(musicPlayerService!!.mState.toString().equals(MusicPlayerService.State.Playing.toString())) {
                player_imgbtnPlayStop!!.setImageResource(R.mipmap.pause)
                val i=Intent(context, MusicPlayerService::class.java)
                i!!.action=Constants.ACTION.PLAY_ACTION
                startService(i)
            }
            else {
                player_imgbtnPlayStop!!.setImageResource(R.mipmap.player)
                val i=Intent(context, MusicPlayerService::class.java)
                i!!.action=Constants.ACTION.PLAY_ACTION
                startService(i)
            }
        }

        player_imgbtnPrevious!!.setOnClickListener {
            if(selectedPosition-1>=0)
            {
                Utility.updatePlayerView(txt_start_time!!, txt_end_time!!, player_seekbar!!, false)
                selectedPosition--
                player_seekbar!!.progress=0
                player_seekbar!!.max=100
                val i=Intent(context, MusicPlayerService::class.java)
                i!!.action=Constants.ACTION.PREV_ACTION
                startService(i)
            }

        }

        player_imgbtnNext!!.setOnClickListener {
            Utility.updatePlayerView(txt_start_time!!, txt_end_time!!, player_seekbar!!, false)
            player_seekbar!!.progress=0
            player_seekbar!!.max=100
            val i=Intent(context, MusicPlayerService::class.java)
            i!!.action=Constants.ACTION.NEXT_ACTION
            startService(i)
        }

        player_imgbtnClose!!.setOnClickListener {
            mHandler.removeCallbacks(updateTimeTask)
            adapter.refreshList(-1)
            freetrailsongs_layoutPlayer!!.setVisibility(View.GONE)
            val i=Intent(context, MusicPlayerService::class.java)
            i!!.action=Constants.ACTION.STOPFOREGROUND_ACTION
            startService(i)
        }

        player_seekbar!!.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // remove message Handler from updating progress bar
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

    open fun playsong(pos: Int)
    {
        if(mHandler!=null)
            mHandler.removeCallbacks(updateTimeTask)

        if(songList.get(pos).isPlaying)
        {
            return
        }

        for (i in 0..songList.size-1)
        {
            songList.get(i).isPlaying=false
        }
        songList.get(pos).isPlaying=true

        freetrailsongs_layoutPlayer!!.setVisibility(View.VISIBLE)
        player_progressLoading!!.visibility=View.VISIBLE
        selectedPosition=pos

        //on complete
        player_imgbtnPlayStop!!.setImageResource(R.mipmap.pause)
        songList.get(selectedPosition).isPlaying = false;
        adapter.refreshList(-1);

        //player.start()
        adapter.refreshList(pos)
        player_seekbar!!.setProgress(0);
        player_seekbar!!.setMax(100);
        //updateProgressBar();
        freetrailsongs_layoutPlayer!!.setVisibility(View.VISIBLE)
        player_progressLoading!!.visibility=View.VISIBLE
        val i=Intent(context, MusicPlayerService::class.java)
        i!!.action=Constants.ACTION.STARTFOREGROUND_ACTION
        var b=Bundle();
        b!!.putParcelableArrayList("songList", songList)
        i!!.putExtras(b)
        i!!.putExtra("albumId", selectedVoice);
        i!!.putExtra("pos", pos)
        startService(i)
    }

    /**
     * Update timer on seekbar
     */
    fun updateProgressBar() {
        mHandler.postDelayed(updateTimeTask, 100)
    }

    private val updateTimeTask = object : Runnable {
        override fun run() {
            val totalDuration = musicPlayerService!!.getDur()
            val currentDuration = musicPlayerService!!.getPosn()

            val progress = Utility.getProgressPercentage(currentDuration.toLong(), totalDuration.toLong())
            player_seekbar!!.setProgress(progress)

            Utility.showTime(currentDuration, txt_start_time!!, totalDuration, txt_end_time!!)// show time

            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100)
        }
    }

    open fun startDownload(pos: Int) {
        selectedDownloadPosition=pos
        if(checkPermission()) {
            Utility.updatePlayerView(txt_start_time!!, txt_end_time!!, player_seekbar!!, false)
            Utility.displayToast(context, "Your download has been started in the notification bar.")
            val intent = Intent(this, DownloadService::class.java)
            intent.putExtra("id", "-1")
            intent.putExtra("url", songList.get(pos).songUrl)
            intent.putExtra("fileName", songList.get(pos).songTitle)
            intent.putExtra(Constants.AppConstants.NAME,songList.get(pos).title)
            intent.putExtra(Constants.AppConstants.SUBTITLE,songList.get(pos).subtitle)
            intent.putExtra(Constants.AppConstants.VOICE_TYPE,songList.get(pos).voiceType)
            startService(intent)
        }
        else
            requestPermission()

    }

    private fun checkPermission(): Boolean {
//        val result = ContextCompat.checkSelfPermission(this,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE)
//         return result == PackageManager.PERMISSION_GRANTED
        return Utility.isExternalStorageWritingAllowed(this)

    }

    private fun requestPermission() {

//        ActivityCompat.requestPermissions(this, arrayOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
        Utility.requestReadAndWriteExternalStoragePersmission(this)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                startDownload(selectedDownloadPosition)
            } else {

                Snackbar.make(layoutParent!!, "Permission Denied, Please allow to proceed !", Snackbar.LENGTH_LONG).show()

            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                onBackPressed()
                //this.finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        mHandler.removeCallbacks(updateTimeTask);
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBackPressed() {
        if(musicPlayerService!=null) {
            if (musicPlayerService!!.mState!!.toString().equals(MusicPlayerService.State.Playing.toString())) {
                player_imgbtnPlayStop!!.setImageResource(R.mipmap.player)
                musicPlayerService!!.pausePlayer()
            }
        }
        super.onBackPressed()
    }

}
