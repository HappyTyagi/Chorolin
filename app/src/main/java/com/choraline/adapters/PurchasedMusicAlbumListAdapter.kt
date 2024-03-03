package com.choraline.adapters

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.choraline.*
import com.choraline.models.PurchasedMusicData

import kotlinx.android.synthetic.main.row_choral_works.view.*

class PurchasedMusicAlbumListAdapter(private val context: Context, open var actitvity: BaseActivity, private var websiteList: List<PurchasedMusicData>) : RecyclerView.Adapter<PurchasedMusicAlbumListAdapter.MyViewHolder>() {

  inner   class MyViewHolder(view: View, open var outer: PurchasedMusicAlbumListAdapter) : RecyclerView.ViewHolder(view) {
        lateinit  var d: PurchasedMusicData
        fun bindData(data: PurchasedMusicData, pos: Int)
        {
            d=data
            itemView.row_choralworks_layoutRow.tag=""+pos
            itemView.row_choralworks_layoutRow.setOnClickListener(childClickListener)
            if (outer.actitvity is PurchasedMusicActivity)
            itemView.row_choralworks_txtWorkTitle.setText(data!!.title+"-"+data!!.subtitle+"\n"+data.voiceType)
            else itemView.row_choralworks_txtWorkTitle.setText(data!!.title)

        }


         var childClickListener: View.OnClickListener = View.OnClickListener { v ->
             val str = v.tag as String
             val pos = Integer.parseInt(str)
             if (outer.actitvity is PurchasedMusicActivity ) {
                 (outer.actitvity as PurchasedMusicActivity)!!.showSongList(pos)
             }
             else
             {
                 (outer.actitvity as DownloadedSongAlbumActivity).nextActivity(websiteList.get(pos).title)
             }
         }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_choral_works, parent, false)

        return MyViewHolder(itemView, this)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val website = websiteList!![position]
        holder.bindData(website, position)
    }

    override fun getItemCount(): Int {
        return websiteList!!.size
    }

    fun refreshList(websiteList: List<PurchasedMusicData>) {
        this.websiteList=websiteList
        notifyDataSetChanged()
    }


}