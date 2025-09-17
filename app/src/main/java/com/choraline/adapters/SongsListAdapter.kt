package com.choraline.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.choraline.BaseActivity
import com.choraline.FreeTrailSongsActivity
import com.choraline.R
import com.choraline.models.SongsData

class SongsListAdapter(
    private val context: Context,
    var activity: BaseActivity,
    private var websiteList: MutableList<SongsData>?, // use MutableList for easier updates
    var type: Int
) : RecyclerView.Adapter<SongsListAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View, var outer: SongsListAdapter) :
        RecyclerView.ViewHolder(view) {

        private val songTitle = view.findViewById<android.widget.TextView>(R.id.row_songs_txtSongTitle)
        private val btnDownload = view.findViewById<android.widget.ImageButton>(R.id.row_songs_imgbtnDownload)
        private val btnPlay = view.findViewById<android.widget.ImageButton>(R.id.row_songs_imgbtnPlay)

        fun bindData(data: SongsData, pos: Int) {
            songTitle.text = data.songTitle

            btnDownload.tag = pos
            btnDownload.setOnClickListener(downloadClickListener)

            btnPlay.tag = pos
            btnPlay.setOnClickListener(playClickListener)

            // Hide download button if type == 0
            btnDownload.visibility = if (type == 0) View.INVISIBLE else View.VISIBLE

            // Update play button icon
            if (data.isPlaying)
                btnPlay.setImageResource(R.mipmap.play_on)
            else
                btnPlay.setImageResource(R.mipmap.play)
        }

        private val downloadClickListener = View.OnClickListener { v ->
            val pos = v.tag as Int
            (outer.activity as FreeTrailSongsActivity).startDownload(pos)
        }

        private val playClickListener = View.OnClickListener { v ->
            val pos = v.tag as Int
            (outer.activity as FreeTrailSongsActivity).playsong(pos)

            // Reset all to not playing
            outer.websiteList?.forEach { it.isPlaying = false }

            // Set the clicked one to playing
            outer.websiteList?.get(pos)?.isPlaying = true

            outer.notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.row_songs, parent, false)
        return MyViewHolder(itemView, this)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        websiteList?.get(position)?.let { holder.bindData(it, position) }
    }

    override fun getItemCount(): Int {
        return websiteList?.size ?: 0
    }

    fun refreshList(pos: Int) {
        websiteList?.forEach { it.isPlaying = false }
        if (pos >= 0 && pos < (websiteList?.size ?: 0)) {
            websiteList?.get(pos)?.isPlaying = true
        }
        notifyDataSetChanged()
    }
}
