package com.choraline

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.choraline.adapters.PurchasedSongsListAdapter
import com.choraline.services.DownloadService
import com.choraline.services.MusicPlayerService
import com.choraline.utils.Constants
import com.choraline.utils.Utility
import com.google.gson.Gson
import com.choraline.models.SongsData
import com.choraline.utils.AppController
import com.google.android.material.snackbar.Snackbar
import com.google.gson.reflect.TypeToken
import java.io.File
import java.util.Locale


open class PurchasedMusicSongListActivity : BaseActivity(), View.OnClickListener,
    MusicPlayerService.updatePrepared {
    var isLeftSelected = false
    var isRightSelected = false

    override fun updateInit() {
        musicPlayerService = MusicPlayerService!!.mInstance
    }

    override fun updatePrep() {
        Utility.updatePlayerView(txt_start_time!!, txt_end_time!!, player_seekbar!!, true)
        player_progressLoading!!.visibility = View.GONE
        updateProgressBar()
        player_imgbtnPlayStop!!.setImageResource(R.mipmap.player)

    }

    override fun updatePlay(flag: Int) {

        if (musicPlayerService!!.mState!!.toString()
                .equals(MusicPlayerService.State.Playing.toString())
        ) {
            player_imgbtnPlayStop!!.setImageResource(R.mipmap.player)
        } else if (musicPlayerService!!.mState!!.toString()
                .equals(MusicPlayerService.State.Paused.toString())
        ) {
            player_imgbtnPlayStop!!.setImageResource(R.mipmap.pause)
        }
    }

    override fun updateNext(pos: Int) {

        if (!(songList.get(pos).localFileUri != null && (!songList.get(pos).localFileUri.isEmpty()))) {
            if (!Utility.isNetworkAvailable(this)) {
                Utility.showMessageDialog(
                    this,
                    resources.getString(R.string.alert_no_internet_connection)
                )
                return
            }
        }

        if (mHandler != null)
            mHandler.removeCallbacks(updateTimeTask)
        selectedPosition = pos
        player_progressLoading!!.visibility = View.VISIBLE
        if (!musicPlayerService!!.albumId.equals("") and musicPlayerService!!.albumId.equals(
                selectedAlbumId
            )
        )
            adapter!!.refreshList(selectedPosition)

    }

    override fun updatePrev(pos: Int) {
        if (mHandler != null)
            mHandler.removeCallbacks(updateTimeTask)
        selectedPosition = pos
        player_progressLoading!!.visibility = View.VISIBLE
        if (!musicPlayerService!!.albumId.equals("") and musicPlayerService!!.albumId.equals(
                selectedAlbumId
            )
        )
            adapter!!.refreshList(selectedPosition)
    }

    override fun updateStop() {
        mHandler.removeCallbacks(updateTimeTask)
        adapter.refreshList(-1)
        if (!isHide) {
            purchasedmusicsongs_layoutPlayer!!.setVisibility(View.GONE)
            musicPlayerService = null
        }
        isHide = false
    }

    lateinit var context: Context
    private lateinit var player: MediaPlayer
    var songList = ArrayList<SongsData>()
    open var urllist = ArrayList<String>()
    open var localFilePathList = ArrayList<String>()


    private lateinit var adapter: PurchasedSongsListAdapter
    private val mHandler = Handler();
    private var selectedPosition: Int = -1
    private var selectedDownloadPosition = -1;
    private val PERMISSION_REQUEST_CODE = 11
    private var selectedVoiceType: String = ""
    private var selectedTitle: String = ""
    private var selectedAlbumId: String = ""
    private var sl: String = ""
    private var singerNmae = ""
    private var subTitle = ""

    private var musicPlayerService: MusicPlayerService? = null
    var albumTitle = ""

    companion object {
        val TAG = "PurchasedMusicSongList"
    }

    var purchasedmusicsongs_layoutPlayer : View? = null
    var purchasedmusicsongs_txtVoiceType : TextView? = null
    var purchasedmusicsongs_recyclerSongs : RecyclerView? = null
    var loop_image_view : ImageView? = null
    var player_imgbtnPlayStop : ImageButton? = null
    var player_imgbtnPrevious : ImageButton? = null
    var player_imgbtnNext : ImageButton? = null
    var change_pitch : ImageButton? = null
    var player_progressLoading : ProgressBar? = null
    var tootlbar_imgbtnShare : ImageButton? = null
    var text_left : TextView? = null
    var text_right : TextView? = null
    var txt_start_time : TextView? = null
    var txt_end_time : TextView? = null
    var player_seekbar : AppCompatSeekBar? = null
    var layoutParent : RelativeLayout? = null
    var player_imgbtnClose : ImageButton? = null
    var toolbar : Toolbar? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchased_music_song_list)

        context = this@PurchasedMusicSongListActivity



        player_imgbtnClose = findViewById(R.id.player_imgbtnClose)
        layoutParent = findViewById(R.id.layoutParent)
        player_progressLoading = findViewById(R.id.player_progressLoading)
        txt_start_time = findViewById(R.id.txt_start_time)
        txt_end_time = findViewById(R.id.txt_end_time)
        player_imgbtnNext = findViewById(R.id.player_imgbtnNext)
        player_imgbtnPrevious = findViewById(R.id.player_imgbtnPrevious)
        player_imgbtnPlayStop = findViewById(R.id.player_imgbtnPlayStop)
        loop_image_view = findViewById(R.id.loop_image_view)
        change_pitch = findViewById(R.id.change_pitch)
        tootlbar_imgbtnShare = findViewById(R.id.tootlbar_imgbtnShare)
        purchasedmusicsongs_recyclerSongs = findViewById(R.id.purchasedmusicsongs_recyclerSongs)
        purchasedmusicsongs_layoutPlayer = findViewById(R.id.purchasedmusicsongs_layoutPlayer)
        purchasedmusicsongs_txtVoiceType = findViewById(R.id.purchasedmusicsongs_txtVoiceType)
        text_left = findViewById(R.id.text_left)
        text_right = findViewById(R.id.text_right)
        tootlbar_imgbtnShare = findViewById(R.id.tootlbar_imgbtnShare)
        player_seekbar = findViewById(R.id.player_seekbar)

        //player.initUI()
        if (intent != null) {
            if (intent.extras != null) {
                sl = intent!!.getStringExtra("songList") as String
                Log.d(TAG, sl)
                if (!sl.equals("")) {
                    val listType = object : TypeToken<ArrayList<SongsData>>() {}.getType()
                    songList = Gson()!!.fromJson(sl, listType)
                }
                Log.d(TAG, "ppppppppppppppp->" + songList.size)
                selectedVoiceType = intent!!.getStringExtra("voiceType")!!
                selectedTitle = intent!!.getStringExtra("title")!!
                selectedAlbumId = intent!!.getStringExtra("albumId")!!
                singerNmae =
                    intent!!.getStringExtra(Constants.AppConstants.NAME)!!.replace("/", " ")
                        .replace("*", "")
                subTitle =
                    intent!!.getStringExtra(Constants.AppConstants.SUBTITLE)!!.replace("/", " ")
                        .replace("*", "")
            }



            if (AppController.mAppController!!.getLooping()) {
                loop_image_view!!.setBackgroundResource(R.mipmap.loop_active)

            } else {
                loop_image_view!!.setBackgroundResource(R.mipmap.loop_inactive)
            }

            loop_image_view!!.setOnClickListener {
                AppController.mAppController!!.setLooping()
                if (musicPlayerService != null) {
                    musicPlayerService!!.setLooping()
                }


                if (AppController.mAppController!!.getLooping()) {
                    loop_image_view!!.setBackgroundResource(R.mipmap.loop_active)

                } else {
                    loop_image_view!!.setBackgroundResource(R.mipmap.loop_inactive)
                }
            }

            change_pitch!!.setOnClickListener {
                pitchDialogue()
            }

        }

        AppController!!.setupInteface(this)

        var appSpecificInternalStorageDirectory = context.getDir("Choralin", Context.MODE_PRIVATE)
        var file1111 = File(appSpecificInternalStorageDirectory, subTitle);
        var  files = File(file1111, "").listFiles()

        if (files != null) {
            for (i in 0 until files.size) {
                Log.d("Files", "FileName:" + files[i].name)
                var model = SongsData()
                var ar = files[i].name.split(Constants.DELIMITER)
                if (ar.size > 1) {
                    model.title = ar[1]
                    model.id = ar[0]
                } else {
                    model.title = ""
                    model.id = ""
                }
                model.songTitle = files[i].name
                model.songUrl = files[i].absolutePath

                val urlModel = AppController.dbInstance.getShortUrlId(model.id)
                // model.dbUrl=urlModel.songUrl
                if (urlModel.songUrl != null && (!urlModel.songUrl.isEmpty())) {
                    localFilePathList.add(files[i].absolutePath)
                    urllist.add(urlModel.songUrl)
                }


            }
        }
        player = MediaPlayer()

        purchasedmusicsongs_txtVoiceType!!.setText(selectedTitle.uppercase(Locale.getDefault()) + "\n" + selectedVoiceType)
        adapter = PurchasedSongsListAdapter(context, this@PurchasedMusicSongListActivity, songList)
         toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        // player_seekbar.setPadding(0,0,0,0)
        initUI()
        initPlayer()

    }

    fun pitchDialogue() {
        val builder = AlertDialog.Builder(this)

        var view = LayoutInflater.from(this).inflate(R.layout.pitch_dialogue, null)
        var point_five_pitch_radio: RadioButton = view.findViewById(R.id.point_five_pitch_radio)
        var point_seven_five_pitch: RadioButton = view.findViewById(R.id.point_seven_five_pitch)
        var one_pitch_radio: RadioButton = view.findViewById(R.id.one_pitch_radio)
        var seekbar_adjust_volume: SeekBar = view.findViewById(R.id.seekbar_adjust_volume)
        var switch_default: TextView = view.findViewById(R.id.switch_default)
        var apply_tv: TextView = view.findViewById(R.id.apply_tv)
        var cancel_tv: TextView = view.findViewById(R.id.cancel_tv)



        builder.setView(view)

        when {
            AppController.mAppController!!.getPitch() == .9f -> point_five_pitch_radio.isChecked =
                true
            AppController.mAppController!!.getPitch() == 0.75f -> point_seven_five_pitch.isChecked =
                true
            else -> one_pitch_radio.isChecked = true
        }


        seekbar_adjust_volume.progress = AppController.mAppController!!.getVolumeProgress()

        var progressChangedValue = 0
        var leftVolume = 0f
        var rightVolume = 0f
        var isAdjustVolumeClicked: Boolean = false


        seekbar_adjust_volume.setOnSeekBarChangeListener(object :
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

        val dialog = builder.show()

        dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        seekbar_adjust_volume.progress = 50
        switch_default.setOnClickListener {
            musicPlayerService?.setVolume(1.0f, 1.0f)
            seekbar_adjust_volume.progress = 50
        }

        apply_tv.setOnClickListener {
            if (point_five_pitch_radio.isChecked)
                AppController.mAppController!!.setPich(0.9f)
            else if (point_seven_five_pitch.isChecked)
                AppController.mAppController!!.setPich(0.75f)
            else AppController.mAppController!!.setPich(1.0f)

            if (musicPlayerService != null) {
                musicPlayerService!!.setPich()
            }



            dialog.dismiss()
        }


        cancel_tv.setOnClickListener { dialog.dismiss() }

    }

    fun initUI() {
        val mLayoutManager = LinearLayoutManager(context)
        purchasedmusicsongs_recyclerSongs!!.setLayoutManager(mLayoutManager)
        purchasedmusicsongs_recyclerSongs!!.setItemAnimator(DefaultItemAnimator())
        purchasedmusicsongs_recyclerSongs!!.setAdapter(adapter)



        tootlbar_imgbtnShare!!.setOnClickListener(this)

        text_left!!.setOnClickListener {
            leftSelected(isRightSelected)
        }

        text_right!!.setOnClickListener {
            rightSelected(isLeftSelected)
        }

    }

    private fun rightSelected(isSelected: Boolean) {
        if (isSelected) {
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

        if (isSelected) {
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


    override fun onClick(v: View?) {

        if (v == tootlbar_imgbtnShare) {
            Utility!!.shareApp(context)
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

                if (!musicPlayerService!!.albumId.equals("") and musicPlayerService!!.albumId.equals(
                        selectedAlbumId
                    )
                )
                    adapter!!.refreshList(musicPlayerService!!.getSelectedSongPosition())

                if (musicPlayerService!!.mState.toString()
                        .equals(MusicPlayerService.State.Paused.toString())
                )
                    player_imgbtnPlayStop!!.setImageResource(R.mipmap.pause)
            }
        }
    }

    fun initPlayer() {


        player_imgbtnPlayStop!!.setOnClickListener {

            if (musicPlayerService != null && musicPlayerService!!.mState.toString()
                    .equals(MusicPlayerService.State.Playing.toString())
            ) {
                player_imgbtnPlayStop!!.setImageResource(R.mipmap.pause)
                val i = Intent(context, MusicPlayerService::class.java)
                i!!.action = Constants.ACTION.PLAY_ACTION
                startService(i)
            } else {
                player_imgbtnPlayStop!!.setImageResource(R.mipmap.player)
                val i = Intent(context, MusicPlayerService::class.java)
                i!!.action = Constants.ACTION.PLAY_ACTION
                startService(i)
            }
        }

        player_imgbtnPrevious!!.setOnClickListener {
            if (selectedPosition - 1 >= 0) {
                Utility.updatePlayerView(txt_start_time!!, txt_end_time!!, player_seekbar!!, false)
                selectedPosition--
                player_seekbar!!.progress = 0
                player_seekbar!!.max = 100
                val i = Intent(context, MusicPlayerService::class.java)
                i!!.action = Constants.ACTION.PREV_ACTION
                startService(i)
            }


        }

        player_imgbtnNext!!.setOnClickListener {
            Utility.updatePlayerView(txt_start_time!!, txt_end_time!!, player_seekbar!!, false)
            player_seekbar!!.progress = 0
            player_seekbar!!.max = 100
            val i = Intent(context, MusicPlayerService::class.java)
            i!!.action = Constants.ACTION.NEXT_ACTION
            startService(i)
        }

        player_imgbtnClose!!.setOnClickListener {
            stopServices()
        }






        player_seekbar!!.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
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

    fun stopServices() {
        try {
            mHandler.removeCallbacks(updateTimeTask)
            adapter.refreshList(-1)
            purchasedmusicsongs_layoutPlayer!!.setVisibility(View.GONE)
            val i = Intent(context, MusicPlayerService::class.java)
            i!!.action = Constants.ACTION.STOPFOREGROUND_ACTION
            startService(i)
            musicPlayerService = null

        } catch (e: Exception) {

        }
    }


    var isHide = false

    open fun playsong(pos: Int) {
        if (musicPlayerService == null) musicPlayerService = MusicPlayerService.mInstance

        if (!urllist.contains(songList[pos].songUrl)) {
            startDownload(pos)
        }

        selectedPosition = pos

        if (AppController.isDownloadSongPlaying) {
            isHide = true
            AppController.setUpisDownloading(false)
            stopServices()
            val myAsync = MyAsyncTask()
            myAsync.execute()
            return
        }

        isHide = false

        if (mHandler != null)
            mHandler.removeCallbacks(updateTimeTask)

        if (songList.get(pos).isPlaying) {
            return
        }

        for (i in 0..songList.size - 1) {
            songList.get(i).isPlaying = false
        }
        songList.get(pos).isPlaying = true

        purchasedmusicsongs_layoutPlayer!!.setVisibility(View.VISIBLE)
        player_progressLoading!!.visibility = View.VISIBLE


        //on complete
        player_imgbtnPlayStop!!.setImageResource(R.mipmap.pause)
        songList.get(selectedPosition).isPlaying = false;
        adapter.refreshList(-1);

        //player.start()
        adapter.refreshList(pos)
        player_seekbar!!.progress = 0
        player_seekbar!!.max = 100
        //updateProgressBar();
        purchasedmusicsongs_layoutPlayer!!.visibility = View.VISIBLE
        player_progressLoading!!.visibility = View.VISIBLE
        val i = Intent(this, MusicPlayerService::class.java)
        i.action = Constants.ACTION.STARTFOREGROUND_ACTION
        val b: Bundle = Bundle();
        b.putParcelableArrayList("songList", songList)
        i.putExtras(b)
        i.putExtra("albumId", selectedAlbumId);
        i.putExtra("pos", pos)
        startService(i)
    }


    inner class MyAsyncTask : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            Thread.sleep(1000)
            return null
        }

        override fun onPostExecute(result: Void?) {
            playsong(selectedPosition)

        }

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

            val progress =
                Utility.getProgressPercentage(currentDuration.toLong(), totalDuration.toLong())
            player_seekbar!!.progress = progress


            Utility.showTime(
                currentDuration,
                txt_start_time!!,
                totalDuration,
                txt_end_time!!
            )// show time

            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100)
        }
    }

    fun resetTextProgress() {
        Utility.showTime(0, txt_start_time!!, 0, txt_end_time!!)// show time
    }


    open fun startDownload(pos: Int) {
        selectedDownloadPosition = pos

        if (!Utility.isNetworkAvailable(this)) {
            Utility.showMessageDialog(
                this,
                resources.getString(R.string.alert_no_internet_connection)
            )
            return
        }
        if (checkPermission()) {
            Utility.updatePlayerView(txt_start_time!!, txt_end_time!!, player_seekbar!!, false)
            Utility.displayToast(context, "Your download has been started in the notification bar.")
            val intent = Intent(this, DownloadService::class.java)
            urllist.add(songList.get(pos).songUrl)
            val id = AppController.dbInstance.makeShortURl(songList.get(pos).songUrl)
            intent.putExtra("id", id)
            intent.putExtra("title", albumTitle)
            intent.putExtra("url", songList.get(pos).songUrl)
            intent.putExtra("fileName", songList.get(pos).songTitle)
            intent.putExtra(Constants.AppConstants.NAME, singerNmae)
            intent.putExtra(Constants.AppConstants.SUBTITLE, subTitle)
            intent.putExtra(Constants.AppConstants.VOICE_TYPE, selectedVoiceType)
            startService(intent)
            adapter.notifyDataSetChanged()
        } else
            requestPermission()

    }

    private fun checkPermission(): Boolean {
//        val result = ContextCompat.checkSelfPermission(this,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE)
//        return result == PackageManager.PERMISSION_GRANTED


        return Utility.isExternalStorageWritingAllowed(this)


    }

    private fun requestPermission() {

//        ActivityCompat.requestPermissions(
//            this,
//            arrayOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE),
//            PERMISSION_REQUEST_CODE
//        )

        Utility.requestReadAndWriteExternalStoragePersmission(this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                startDownload(selectedDownloadPosition)
            } else {

                Snackbar.make(
                    layoutParent!!,
                    "Permission Denied, Please allow to proceed !",
                    Snackbar.LENGTH_LONG
                ).show()

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
        /* if(musicPlayerService!=null) {
             if (musicPlayerService!!.mState!!.toString().equals(MusicPlayerService.State.Playing.toString())) {
                 player_imgbtnPlayStop.setImageResource(R.mipmap.player)
                 musicPlayerService!!.pausePlayer()
             }
         }*/
        super.onBackPressed()
    }


}
