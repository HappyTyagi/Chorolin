package com.choraline.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.choraline.BaseActivity
import com.choraline.PurchasedMusicSongListActivity
import com.choraline.R
import com.choraline.models.SongsData
import com.choraline.utils.Utility

open class PurchasedSongsListAdapter(
    private val context: Context,
    open var activity: BaseActivity,
    private var websiteList: MutableList<SongsData>?
) : RecyclerView.Adapter<PurchasedSongsListAdapter.MyViewHolder>() {

    open inner class MyViewHolder(view: View, private val outer: PurchasedSongsListAdapter) :
        RecyclerView.ViewHolder(view) {

        private val txtSongTitle: TextView = view.findViewById(R.id.row_songs_txtSongTitle)
        private val btnDownload: ImageButton = view.findViewById(R.id.row_songs_imgbtnDownload)
        private val btnPlay: ImageButton = view.findViewById(R.id.row_songs_imgbtnPlay)

        fun bindData(data: SongsData, pos: Int) {
            txtSongTitle.text = data.songTitle

            // Handle download button
            btnDownload.tag = pos
            btnDownload.setOnClickListener(downloadClickListener)

            val activity = context as PurchasedMusicSongListActivity
            if (activity.urllist.contains(data.songUrl)) {
                btnDownload.visibility = View.INVISIBLE
                val index = activity.urllist.indexOf(data.songUrl)
                if (activity.localFilePathList.size > index) {
                    data.localFileUri = activity.localFilePathList[index]
                }
            } else {
                btnDownload.visibility = View.VISIBLE
            }

            // Handle play button
            btnPlay.tag = pos
            btnPlay.setOnClickListener(playClickListener)

            if (data.isPlaying) {
                btnPlay.setImageResource(R.mipmap.play_on)
            } else {
                btnPlay.setImageResource(R.mipmap.play)
            }
        }

        private val downloadClickListener = View.OnClickListener { v ->
            val pos = v.tag as Int
            (outer.activity as PurchasedMusicSongListActivity).startDownload(pos)
        }

        private val playClickListener = View.OnClickListener { v ->
            val pos = v.tag as Int
            val activity = outer.activity as PurchasedMusicSongListActivity
            activity.resetTextProgress()

            val song = outer.websiteList?.get(pos) ?: return@OnClickListener

            if (activity.urllist.contains(song.songUrl)) {
                // Play already downloaded song
                activity.playsong(pos)
                outer.websiteList?.forEach { it.isPlaying = false }
                song.isPlaying = true
                outer.notifyDataSetChanged()
                return@OnClickListener
            } else {
                // Play online song
                if (!Utility.isNetworkAvailable(context)) {
                    Utility.showMessageDialog(
                        context,
                        context.getString(R.string.alert_no_internet_connection)
                    )
                    return@OnClickListener
                }
                activity.playsong(pos)
                outer.websiteList?.forEach { it.isPlaying = false }
                song.isPlaying = true
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
