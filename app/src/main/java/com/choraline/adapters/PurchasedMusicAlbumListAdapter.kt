package com.choraline.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.choraline.*
import com.choraline.models.PurchasedMusicData

class PurchasedMusicAlbumListAdapter(
    private val context: Context,
    open var activity: BaseActivity,
    private var websiteList: MutableList<PurchasedMusicData>
) : RecyclerView.Adapter<PurchasedMusicAlbumListAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View, private val outer: PurchasedMusicAlbumListAdapter) :
        RecyclerView.ViewHolder(view) {

        private val layoutRow: LinearLayout = view.findViewById(R.id.row_choralworks_layoutRow)
        private val txtWorkTitle: TextView = view.findViewById(R.id.row_choralworks_txtWorkTitle)

        fun bindData(data: PurchasedMusicData, pos: Int) {
            layoutRow.tag = pos
            layoutRow.setOnClickListener(childClickListener)

            if (outer.activity is PurchasedMusicActivity) {
                txtWorkTitle.text = "${data.title} - ${data.subtitle}\n${data.voiceType}"
            } else {
                txtWorkTitle.text = data.title
            }
        }

        private val childClickListener = View.OnClickListener { v ->
            val pos = v.tag as Int
            when (val act = outer.activity) {
                is PurchasedMusicActivity -> act.showSongList(pos)
                is DownloadedSongAlbumActivity -> act.nextActivity(outer.websiteList[pos].title)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_choral_works, parent, false)
        return MyViewHolder(itemView, this)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        websiteList[position].let { holder.bindData(it, position) }
    }

    override fun getItemCount(): Int = websiteList.size

    fun refreshList(newList: List<PurchasedMusicData>) {
        websiteList = newList.toMutableList()
        notifyDataSetChanged()
    }
}
