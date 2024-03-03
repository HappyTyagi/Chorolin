package com.choraline

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
//import androidx.core.content.ContextCompat
//import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
//import android.support.v7.widget.GridLayoutManager
import androidx.appcompat.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.choraline.adapters.ChoralWorksListAdapter

import com.choraline.models.ChoralWorksData
import com.choraline.models.ChoralWorksModel
import com.choraline.models.SongDetailModel
import com.choraline.network.APIListener
import com.choraline.network.Webservices
import com.choraline.utils.AppController
import com.choraline.utils.Constants
import com.choraline.utils.GridSpacingItemDecoration
import com.choraline.utils.Utility
import com.google.gson.Gson
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.activity_choral_works.*

class ChoralWorksActivity : BaseActivity(), View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, APIListener{

    val TAG : String? = ChoralWorksActivity::class.simpleName
    private lateinit var context : Context
    //var toolbar: Toolbar? = null

    var choralWorksModel = ChoralWorksModel()
    lateinit var adapter: ChoralWorksListAdapter
    var albumList = ArrayList<ChoralWorksData>()
    var selectedComposerTitle: String = ""
    var selectedSongId: String = ""
    var type = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choral_works)
        context=this@ChoralWorksActivity
      //  toolbar = findViewById<Toolbar>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        if(intent!=null)
        {
            Log.d(TAG,"-------------------------------------------------------")
            Log.d(TAG, intent!!.getStringExtra("data")!!)
            choralWorksModel=Gson().fromJson(intent.getStringExtra("data"), ChoralWorksModel::class.java);
            selectedComposerTitle = intent!!.getStringExtra("title")!!;
            type = intent.getStringExtra("type")!!
        }
        initUI()
    }

    fun initUI()
    {

        choralworks_swipeRefreshLayout.setOnRefreshListener(this)
        choralworks_txtWithOutSinger.setOnClickListener(this)
        choralworks_txtWithSinger.setOnClickListener(this)
        tootlbar_imgbtnShare.setOnClickListener(this)
        adapter = ChoralWorksListAdapter(context, this, albumList)
        val mLayoutManager = GridLayoutManager(context, 2)
        choralworks_recyclerChoralWorks.setLayoutManager(mLayoutManager)
        val spacingInPixels = 25
        choralworks_recyclerChoralWorks.addItemDecoration(GridSpacingItemDecoration(2, spacingInPixels, false, 0))
        choralworks_recyclerChoralWorks.setAdapter(adapter)
        manageTabSelection(1)
        setValues()
    }

    fun setValues()
    {
        if(choralWorksModel!=null) {
            albumList.clear()
            albumList.addAll(choralWorksModel!!.response!!.paidsongList)

            Picasso.with(context).load(choralWorksModel!!.response!!.bannerImage).into(choralworks_imgBanner)

            if(choralWorksModel.response!!.paidsongwitsingerList.size>0)
                choralworks_layoutTab.visibility=View.VISIBLE
            else
                choralworks_layoutTab.visibility=View.GONE
            adapter.notifyDataSetChanged()
        }
    }

    override fun onClick(v: View?) {

        if(v==tootlbar_imgbtnShare)
        {
            Utility!!.shareApp(context)
        }
        else if(v==choralworks_txtWithOutSinger)
        {
            manageTabSelection(1)
            albumList.clear()
            albumList.addAll(choralWorksModel!!.response!!.paidsongList)
            adapter.refreshList(albumList)
        }
        else if(v==choralworks_txtWithSinger)
        {
            manageTabSelection(2)
            albumList.clear()
            albumList.addAll(choralWorksModel!!.response!!.paidsongwitsingerList)
            adapter.refreshList(albumList)
        }
    }

    override fun onRefresh() {
        choralworks_swipeRefreshLayout.isRefreshing=false
       // getChoralWorks(selectedComposerTitle)
    }

    fun showDetail(pos: Int)
    {
        selectedSongId=albumList[pos]!!.albumId
        getSongDetail(selectedSongId)
    }

    fun getChoralWorks(composerName: String)
    {
      //  Webservices(context, this, false, "").callGetChoralWorksAPI(composerName, AppController.dbInstance.getComposerAlbumTimeStamp(composerName),Constants.API_CHORAL_WORKS)
    }

    fun getSongDetail(id: String)
    {
        Webservices(context, this, true, "Please wait...").callGetSongDetailAPI(id, Constants.API_SONG_DETAIL)
    }

    override fun onApiSuccess(obj: Any, api: Int) {

        if(api == Constants.API_SONG_DETAIL)
        {
            var result = obj as SongDetailModel
            if(result !=null)
            {
                if(result!!.status)
                {
                    if(result!!.response!=null)
                    {
                        val intents = Intent(context, ChoralWorksDetailActivity::class.java)
                        intents.putExtra("id", selectedSongId)
                        intents.putExtra("data", Gson().toJson(result))
                        intents.putExtra("type",type)
                        startActivity(intents)
                    }
                }
                else
                    Utility!!.showMessageDialog(context, result!!.message)
            }
        }
        else if(api == Constants.API_CHORAL_WORKS)
        {
            if(choralworks_swipeRefreshLayout.isRefreshing)
                choralworks_swipeRefreshLayout.isRefreshing =false
            var result = obj as ChoralWorksModel
            if(result.status)
            {
                if(result.response!=null)
                {
                    choralWorksModel=result
                    setValues()
                }
            }
            else if(!result.status)
            {
                Utility!!.showMessageDialog(context, result!!.message)
            }
        }
    }

    override fun onApiFailure(throwable: Throwable,api: Int) {

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.getItemId()) {
            android.R.id.home -> {
                this.finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    fun manageTabSelection(pos: Int)
    {
        if(pos==1)
        {

            choralworks_txtWithOutSinger.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
            choralworks_txtWithOutSinger.setBackgroundColor(Color.WHITE)

            choralworks_txtWithSinger.setTextColor(Color.WHITE)
            choralworks_txtWithSinger.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary))
        }
        else if(pos==2)
        {
            choralworks_txtWithSinger.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
            choralworks_txtWithSinger.setBackgroundColor(Color.WHITE)

            choralworks_txtWithOutSinger.setTextColor(Color.WHITE)
            choralworks_txtWithOutSinger.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary))
        }
    }

}
