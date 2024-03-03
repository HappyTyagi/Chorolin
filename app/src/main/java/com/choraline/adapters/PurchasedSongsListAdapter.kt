package com.choraline.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.choraline.BaseActivity
import com.choraline.FreeTrailSongsActivity
import com.choraline.PurchasedMusicSongListActivity
import com.choraline.R
import com.choraline.models.FreeVoiceTypeData
import com.choraline.models.PurchasedSongsData
import com.choraline.models.SongsData
import com.choraline.utils.AppController
import com.choraline.utils.Utility

/**
 * Created by deepak Tyagi on 5/26/2017.
 */

import kotlinx.android.synthetic.main.row_songs.view.*

open class PurchasedSongsListAdapter(val context: Context, open var actitvity: BaseActivity, private var websiteList: List<SongsData>?) : RecyclerView.Adapter<PurchasedSongsListAdapter.MyViewHolder>() {

    open inner class MyViewHolder(view: View, open var outer: PurchasedSongsListAdapter) : RecyclerView.ViewHolder(view) {
        lateinit var d: SongsData
        fun bindData(data: SongsData, pos: Int) {
            d = data
            itemView.row_songs_txtSongTitle.text = data.songTitle
            itemView.row_songs_imgbtnDownload.tag = "" + pos
            itemView.row_songs_imgbtnDownload.setOnClickListener(downloadClickListener)

            if ((context as PurchasedMusicSongListActivity).urllist.contains(data.songUrl)) {
                itemView.row_songs_imgbtnDownload.visibility = View.INVISIBLE
                if ((context).localFilePathList.size > (context).urllist.indexOf(data.songUrl))
                    data.localFileUri = (context).localFilePathList.get((context).urllist.indexOf(data.songUrl))

            } else {
                itemView.row_songs_imgbtnDownload.visibility = View.VISIBLE
            }


            itemView.row_songs_imgbtnPlay.setTag("" + pos)
            itemView.row_songs_imgbtnPlay.setOnClickListener(playClickListener)
            if (data.isPlaying)
                itemView.row_songs_imgbtnPlay.setImageResource(R.mipmap.play_on)
            else
                itemView.row_songs_imgbtnPlay.setImageResource(R.mipmap.play)
            ((actitvity))
        }


        var downloadClickListener: View.OnClickListener = View.OnClickListener { v ->
            val str = v.tag as String
            val pos = Integer.parseInt(str)
            (outer.actitvity as PurchasedMusicSongListActivity).startDownload(pos)
        }

        var playClickListener: View.OnClickListener = View.OnClickListener { v ->
            val str = v.tag as String
            val pos = Integer.parseInt(str)
            (outer.actitvity as PurchasedMusicSongListActivity).resetTextProgress()
            if ((context as PurchasedMusicSongListActivity).urllist.contains(websiteList!!.get(pos).songUrl)) {

                (outer.actitvity as PurchasedMusicSongListActivity).playsong(pos)
                for (i in 0..outer.websiteList!!.size - 1) {

                    var vd = (outer.websiteList as List<SongsData>)[i]
                    vd.isPlaying = false
                }
                ((outer.websiteList as List<SongsData>)[pos]).isPlaying = true
                outer.notifyDataSetChanged()

                return@OnClickListener

            } else {
                if (!Utility.isNetworkAvailable(context)) {
                    Utility.showMessageDialog(context!!, context.getString(R.string.alert_no_internet_connection))
                    return@OnClickListener
                }

                (outer.actitvity as PurchasedMusicSongListActivity).playsong(pos)
                for (i in 0..outer.websiteList!!.size - 1) {

                    var vd = (outer.websiteList as List<SongsData>)[i]
                    vd.isPlaying = false
                }
                ((outer.websiteList as List<SongsData>)[pos]).isPlaying = true
                outer.notifyDataSetChanged()
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_songs, parent, false)

        return MyViewHolder(itemView, this)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val website = websiteList!![position]
        (holder).bindData(website, position)
    }

    override fun getItemCount(): Int {
        return websiteList!!.size
    }

    fun refreshList(pos: Int) {
        for (i in 0..websiteList!!.size - 1) {
            this.websiteList!!.get(i).isPlaying = false
        }
        if (pos >= 0 && pos < this.websiteList!!.size)
            this.websiteList!!.get(pos).isPlaying = true
        notifyDataSetChanged()
    }


}