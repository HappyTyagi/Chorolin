package com.choraline

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.choraline.adapters.PurchasedMusicAlbumListAdapter
import com.choraline.models.PurchasedMusicData
import com.choraline.models.PurchasedMusicModel
import com.choraline.models.SongsData
import com.choraline.network.APIListener
import com.choraline.network.Webservices
import com.choraline.utils.AppController
import com.choraline.utils.Constants
import com.choraline.utils.GridSpacingItemDecoration
import com.choraline.utils.Utility
import com.google.gson.Gson
import java.io.File

class PurchasedMusicActivity : BaseActivity(), View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, APIListener {

    private lateinit var context: Context
    private var albumList=ArrayList<PurchasedMusicData>()
    var toolbar : Toolbar? = null
    var no_data : TextView? = null
    var purchasedmusic_swipeRefreshLayout : SwipeRefreshLayout? = null
    var purchasedmusic_recycler : RecyclerView? = null
    var tootlbar_imgbtnShare : ImageButton? = null
    private lateinit var adapter: PurchasedMusicAlbumListAdapter
    private lateinit var dialog: ProgressDialog

    companion object {
        val TAG = "PurchasedMusicActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchased_music)
        context=this@PurchasedMusicActivity
        dialog = ProgressDialog(this)
        dialog.setMessage("Please wait...")
        dialog.setCancelable(false)
        toolbar = findViewById(R.id.toolbar)
        no_data = findViewById(R.id.no_data)
        purchasedmusic_swipeRefreshLayout = findViewById(R.id.purchasedmusic_swipeRefreshLayout)
        purchasedmusic_recycler = findViewById(R.id.purchasedmusic_recycler)
        tootlbar_imgbtnShare = findViewById(R.id.tootlbar_imgbtnShare)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        adapter= PurchasedMusicAlbumListAdapter(context, this, albumList)
        initUI()

    }

    fun initUI()
    {
        val mLayoutManager = GridLayoutManager(context, 2)
        purchasedmusic_recycler!!.setLayoutManager(mLayoutManager)
        val spacingInPixels = 25
        purchasedmusic_recycler!!.addItemDecoration(GridSpacingItemDecoration(2, spacingInPixels, true, 0))
        purchasedmusic_recycler!!.setAdapter(adapter)
        purchasedmusic_swipeRefreshLayout!!.setOnRefreshListener(this)
        tootlbar_imgbtnShare!!.setOnClickListener(this)
        //Utility!!.showSnakeBar(layoutParent, "Under Development")
        getPurchasedMusic()
    }

    override fun onClick(v: View?) {

        if(v==tootlbar_imgbtnShare)
        {
            Utility!!.shareApp(context)
        }
    }

    override fun onRefresh() {

        getPurchasedMusic()
    }

    fun getPurchasedMusic()
    {
        Webservices(context, this, !(purchasedmusic_swipeRefreshLayout!!.isRefreshing), "Please wait...").callPurchasedMusicAPI(AppController!!.appPref!!.userId,
                Constants.API_GET_PURCHASED_MUSIC);
    }

    fun showSongList(pos: Int)
    {
        val intents = Intent(context, PurchasedMusicSongListActivity::class.java)
        intents.putExtra("title", albumList.get(pos).title+" "+albumList.get(pos).subtitle)
        intents.putExtra("voiceType", albumList.get(pos).voiceType)
        intents.putExtra("songList", Gson().toJson(albumList.get(pos).songlist))

        Log.d(TAG,"songList    "+albumList.get(pos).songlist.size)
        intents.putExtra(Constants.AppConstants.NAME,albumList.get(pos).title)
        intents.putExtra(Constants.AppConstants.SUBTITLE,albumList.get(pos).subtitle)
        intents.putExtra("albumId", albumList.get(pos).id)

        startActivity(intents)
    }

    override fun onApiSuccess(obj: Any, api: Int) {

        if(api == Constants.API_GET_PURCHASED_MUSIC)
        {
            if(purchasedmusic_swipeRefreshLayout!!.isRefreshing)
                purchasedmusic_swipeRefreshLayout!!.isRefreshing=false

            val result=obj as PurchasedMusicModel
            AppController.appPref.purchaseSongTimeStamp=result.lastDate
            if(result.status)
            {
                AppController.dbInstance.clearPurchaseAlbumList()
                if(result!!.response!=null)
                {
                    if(result!!.response!!.album!=null && result!!.response!!.album.size>0)
                    {
                      AppController.dbInstance.clearPurchaseAlbumList()
                      //  albumList!!.addAll(result!!.response!!.album)
                       // adapter.refreshList(albumList)
                        InsertDataIntoDB().execute(result!!.response!!.album)

                     //  AppController.dbInstance.insertPurchaseData(result!!.response!!.album)

                    }
                }
                else
                {
                    Utility!!.showMessageDialog(context, result!!.message)
                }
            }
            else
            {
                Utility!!.showMessageDialog(context, result!!.message)
            }
        }
     // getSongDataFromDB()

       GetSongDataFromDBAsyncTask().execute()
    }

    override fun onApiFailure(throwable: Throwable,api: Int) {

        if(purchasedmusic_swipeRefreshLayout!!.isRefreshing)
            purchasedmusic_swipeRefreshLayout!!.isRefreshing=false
      //  getSongDataFromDB()
        GetSongDataFromDBAsyncTask().execute()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                this.finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }




    inner  class InsertDataIntoDB : AsyncTask<ArrayList<PurchasedMusicData>, Void, String>()
    {
        override fun onPreExecute() {
            super.onPreExecute()
            dialog.show()
        }
        override fun doInBackground(vararg params: ArrayList<PurchasedMusicData>?) :String{
            AppController.dbInstance.insertPurchaseData(params.get(0)!!)

            return "";

        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            dialog.dismiss()

        }
    }


   inner class GetSongDataFromDBAsyncTask : AsyncTask<Void, Void, ArrayList<PurchasedMusicData>>()
    {
        override fun onPreExecute() {
            super.onPreExecute()
            dialog.show()
        }
        override fun doInBackground(vararg params: Void?): ArrayList<PurchasedMusicData> {

            return AppController.dbInstance.getPurchaseSongList()
        }

        override fun onPostExecute(result: ArrayList<PurchasedMusicData>?) {
            super.onPostExecute(result)
            albumList!!.clear()
            albumList.addAll(result!!)


            if(albumList.size==0)
            {
                no_data!!.visibility = View.VISIBLE
                purchasedmusic_recycler!!.visibility=View.GONE

            }else
            {
                no_data!!.visibility = View.GONE
                purchasedmusic_recycler!!.visibility=View.VISIBLE
            }
             dialog.dismiss()
            adapter.notifyDataSetChanged()
            Log.d(TAG,"1111111111111111111111111111111111111111111111=>"+albumList.size)

        }

    }



    fun getSongDataFromDB()
    {
        albumList!!.clear()
        albumList.addAll(AppController.dbInstance.getPurchaseSongList())



        Log.d(TAG,"1111111111111111111111111111111111111111111111=>"+albumList.size)
        adapter.notifyDataSetChanged()



    }





}
