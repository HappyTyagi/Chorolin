package com.choraline.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.choraline.BaseActivity
import com.choraline.ChoralWorksActivity
import com.choraline.R
import com.choraline.models.ChoralWorksData



class ChoralWorksListAdapter(private val context: Context, open var actitvity: BaseActivity, private var websiteList: List<ChoralWorksData>) : RecyclerView.Adapter<ChoralWorksListAdapter.MyViewHolder>() {

     class MyViewHolder(view: View, open var outer: ChoralWorksListAdapter) : RecyclerView.ViewHolder(view) {
        lateinit  var d: ChoralWorksData


         private val row_choralworks_layoutRow: LinearLayout = view.findViewById(R.id.row_choralworks_layoutRow)
         private val row_choralworks_txtWorkTitle: TextView = view.findViewById(R.id.row_choralworks_txtWorkTitle)


         fun bindData(data: ChoralWorksData, pos: Int)
        {
            d=data
            row_choralworks_layoutRow.contentDescription=""+pos
            row_choralworks_layoutRow.setOnClickListener(childClickListener)
            row_choralworks_txtWorkTitle.setText(data.songName)

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