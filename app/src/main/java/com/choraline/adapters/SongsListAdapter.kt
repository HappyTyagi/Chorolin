package com.choraline.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.choraline.BaseActivity
import com.choraline.FreeTrailSongsActivity
import com.choraline.R
import com.choraline.models.SongsData
import com.choraline.models.FreeVoiceTypeData


import kotlinx.android.synthetic.main.row_songs.view.*

/**
 * Created by deepak Tyagi on 5/26/2017.
 */


class  SongsListAdapter(private val context: Context, open var actitvity: BaseActivity, private var websiteList: List<SongsData>?,var type:Int) : RecyclerView.Adapter<SongsListAdapter.MyViewHolder>() {

   inner  class MyViewHolder(view: View, open var outer: SongsListAdapter) : RecyclerView.ViewHolder(view) {
        lateinit  var d: SongsData
        fun bindData(data: SongsData, pos: Int)
        {
            d=data
            itemView.row_songs_txtSongTitle.text=data.songTitle
            itemView.row_songs_imgbtnDownload.setTag(""+pos)
            itemView.row_songs_imgbtnDownload.setOnClickListener(downloadClickListener)
            itemView.row_songs_imgbtnPlay.setTag(""+pos)
            itemView.row_songs_imgbtnPlay.setOnClickListener(playClickListener)

            if (type==0)
            {
                itemView.row_songs_imgbtnDownload.visibility=View.INVISIBLE
            }else
                itemView.row_songs_imgbtnDownload.visibility=View.VISIBLE

            if(data.isPlaying)
                itemView.row_songs_imgbtnPlay.setImageResource(R.mipmap.play_on)
            else
                itemView.row_songs_imgbtnPlay.setImageResource(R.mipmap.play)
        }





         var downloadClickListener: View.OnClickListener = View.OnClickListener { v ->
             val str = v.tag as String
             val pos = Integer.parseInt(str)
             (outer.actitvity as FreeTrailSongsActivity).startDownload(pos)
         }

         var playClickListener: View.OnClickListener = View.OnClickListener { v ->
             val str = v.tag as String
             val pos = Integer.parseInt(str)
             (outer.actitvity as FreeTrailSongsActivity).playsong(pos)
             for(i in 0..outer.websiteList!!.size-1)
             {

                 var vd=(outer.websiteList as List<SongsData>)[i] as SongsData
                 vd.isPlaying=false
             }
             ((outer.websiteList as List<FreeVoiceTypeData>)[pos] as SongsData).isPlaying=true
             outer.notifyDataSetChanged()
         }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_songs, parent, false)

        return MyViewHolder(itemView, this)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val website = websiteList!![position]
        (holder as MyViewHolder).bindData(website, position)
    }

    override fun getItemCount(): Int {
        return websiteList!!.size
    }

    fun refreshList(pos: Int) {
        for(i in 0..websiteList!!.size-1)
        {
            this.websiteList!!.get(i).isPlaying=false
        }
        if(pos>=0 && pos<this.websiteList!!.size)
            this.websiteList!!.get(pos).isPlaying=true
        notifyDataSetChanged()
    }


}