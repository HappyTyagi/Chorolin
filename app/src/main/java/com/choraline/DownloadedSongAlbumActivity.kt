package com.choraline

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.choraline.adapters.PurchasedMusicAlbumListAdapter
import com.choraline.models.PurchasedMusicData
import com.choraline.utils.Constants
import com.choraline.utils.GridSpacingItemDecoration
import com.choraline.utils.Utility
import java.io.File

class DownloadedSongAlbumActivity : BaseActivity() {



    private var albumList=ArrayList<PurchasedMusicData>()

    private lateinit var adapter: PurchasedMusicAlbumListAdapter
    private var toolbarTile =""
    val TAG = "DownloadedSongAlbumA"
    var toolbar : Toolbar? = null
    var tootlbar_imgbtnShare : ImageButton? = null
    var recycler_view : RecyclerView? = null
    var toolbar_title : TextView? = null
    var no_data : TextView? = null
   lateinit var filedir:File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_downloaded_song_album)
         toolbar = findViewById(R.id.toolbar)
        no_data = findViewById(R.id.no_data)
        toolbar_title = findViewById(R.id.toolbar_title)
        recycler_view = findViewById(R.id.recycler_view)
        tootlbar_imgbtnShare = findViewById(R.id.tootlbar_imgbtnShare)
        setSupportActionBar(toolbar)
        toolbar!!.setNavigationOnClickListener{onBackPressed()}
        tootlbar_imgbtnShare!!.setOnClickListener{Utility.shareApp(this)}

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar!!.setTitle("ssssss")

      //   type = intent.getIntExtra(Constants.AppConstants.TYPE,0)
        toolbarTile = intent.getStringExtra(Constants.AppConstants.TOOLBARTILE)!!
        if (toolbarTile.length>0)
        {
            toolbar_title!!.text = toolbarTile
        }else
        {
            toolbar_title!!.text = getString(R.string.download)
        }

        adapter = PurchasedMusicAlbumListAdapter(this,this,albumList)
        val mLayoutManager = GridLayoutManager(this, 2)
        recycler_view!!.setLayoutManager(mLayoutManager)
        val spacingInPixels = 25
        recycler_view!!.addItemDecoration(GridSpacingItemDecoration(2, spacingInPixels, true, 0))
        recycler_view!!.setAdapter(adapter)



    }

    override fun onResume() {
        super.onResume()

        albumList.clear()
        var files: Array<File>? = null;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val appSpecificInternalStorageDirectory: File = this.getDir("Choralin", Context.MODE_PRIVATE)
             files = File(appSpecificInternalStorageDirectory, "").listFiles()





        if (files!=null) {
            for (i in 0 until files.size) {
                Log.d(TAG, "FileName:" + files[i].name)
                var model = PurchasedMusicData()
                model.title = files[i].name
                albumList.add(model)
            }
        }

        adapter.notifyDataSetChanged()

        if(albumList.size>0)
        {
            no_data!!.visibility = View.GONE
            recycler_view!!.visibility = View.VISIBLE

        }else
        {
            no_data!!.visibility = View.VISIBLE
            recycler_view!!.visibility = View.GONE
        }

    }


    open fun nextActivity( title:String)
    {
       val  paths =title
        var intent: Intent? =null
        /*if (type==2)
        {*/
            intent = Intent(this,DownloadActivity::class.java)
            intent!!.putExtra(Constants.AppConstants.PATH,paths)
            intent!!.putExtra(Constants.AppConstants.TOOLBARTILE,toolbarTile+" "+title)
            startActivity(intent)
       /* }else
        {
            intent = Intent(this,DownloadedSongAlbumActivity::class.java)
            if (toolbarTile.length>0) intent!!.putExtra(Constants.AppConstants.TOOLBARTILE,toolbarTile+"-"+title)
            else intent!!.putExtra(Constants.AppConstants.TOOLBARTILE,title)
            intent!!.putExtra(Constants.AppConstants.PATH,paths)
            var t = type+1
            intent!!.putExtra(Constants.AppConstants.TYPE,t)
            startActivity(intent)
        }*/


    }


    override fun onBackPressed() {

        super.onBackPressed()
    }

}
