package com.choraline.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.choraline.BaseActivity
import com.choraline.ChoralWorksActivity
import com.choraline.FreeTrailSongsActivity
import com.choraline.R
import com.choraline.models.ChoralWorksData
import com.choraline.models.ComposerData

import kotlinx.android.synthetic.main.row_choral_works.view.*

class ChoralWorksListAdapter(private val context: Context, open var actitvity: BaseActivity, private var websiteList: List<ChoralWorksData>) : RecyclerView.Adapter<ChoralWorksListAdapter.MyViewHolder>() {

     class MyViewHolder(view: View, open var outer: ChoralWorksListAdapter) : RecyclerView.ViewHolder(view) {
        lateinit  var d: ChoralWorksData
        fun bindData(data: ChoralWorksData, pos: Int)
        {
            d=data
            itemView.row_choralworks_layoutRow.contentDescription=""+pos
            itemView.row_choralworks_layoutRow.setOnClickListener(childClickListener)
            itemView.row_choralworks_txtWorkTitle.setText(data.songName)

        }


         var childClickListener: View.OnClickListener = View.OnClickListener { v ->
             val str = v.contentDescription as String
             val pos = Integer.parseInt(str)
             (outer.actitvity as ChoralWorksActivity)!!.showDetail(pos)
         }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_choral_works, parent, false)

        return MyViewHolder(itemView, this)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val website = websiteList!![position] as ChoralWorksData
        (holder as MyViewHolder).bindData(website, position)
    }

    override fun getItemCount(): Int {
        return websiteList!!.size
    }

    fun refreshList(websiteList: List<ChoralWorksData>) {
        this.websiteList=websiteList
        notifyDataSetChanged()
    }


}