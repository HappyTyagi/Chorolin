package com.choraline.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.choraline.BaseActivity
import com.choraline.FreeTrailActivity
import com.choraline.R
import com.choraline.models.FreeVoiceTypeData


import java.util.ArrayList
import kotlinx.android.synthetic.main.row_voice_type.view.*

/**
 * Created by deepak Tyagi on 5/26/2017.
 */


class FreeVoiceTypeListAdapter(private val context: Context, open var actitvity: BaseActivity, private var websiteList: List<FreeVoiceTypeData>?) : RecyclerView.Adapter<FreeVoiceTypeListAdapter.MyViewHolder>() {

    val radioArr: IntArray = intArrayOf(R.drawable.custom_radiobutton_1, R.drawable.custom_radiobutton_2,
            R.drawable.custom_radiobutton_3, R.drawable.custom_radiobutton_4)

     class MyViewHolder(view: View, open var outer: FreeVoiceTypeListAdapter) : RecyclerView.ViewHolder(view) {
        lateinit  var d: FreeVoiceTypeData
        fun bindData(data: FreeVoiceTypeData, pos: Int)
        {
            d=data

            if(data.attr1.contains("Soprano", true))
                itemView.row_voicetype_radioCheck.setButtonDrawable(outer.radioArr[0])
            else if(data.attr1.contains("Alto", true))
                itemView.row_voicetype_radioCheck.setButtonDrawable(outer.radioArr[1])
            else if(data.attr1.contains("Tenor", true))
                itemView.row_voicetype_radioCheck.setButtonDrawable(outer.radioArr[2])
            else if(data.attr1.contains("Bass", true))
                itemView.row_voicetype_radioCheck.setButtonDrawable(outer.radioArr[3])

            itemView.row_voicetype_radioCheck.isChecked=data.isSelected
            itemView.row_voicetype_txtVoiceTitle.text=data.attr1
            itemView.row_voicetype_layoutRow.setTag(""+pos)
            itemView.row_voicetype_radioCheck.setTag(""+pos)
            itemView.row_voicetype_layoutRow.setOnClickListener(downloadClickListener)
            itemView.row_voicetype_radioCheck.setOnClickListener(downloadClickListener)

        }


         var downloadClickListener: View.OnClickListener = View.OnClickListener { v ->
             val str = v.tag as String
             val pos = Integer.parseInt(str)
             for(i in 0..outer.websiteList!!.size-1)
             {

                 var vd=(outer.websiteList as List<FreeVoiceTypeData>)[i] as FreeVoiceTypeData
                 vd.isSelected=false
             }
             ((outer.websiteList as List<FreeVoiceTypeData>)[pos] as FreeVoiceTypeData).isSelected=true
             (outer.actitvity as FreeTrailActivity)!!.setSelectedVoiceType((outer.websiteList as List<FreeVoiceTypeData>)[pos] as FreeVoiceTypeData)
             outer.notifyDataSetChanged()

         }



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_voice_type, parent, false)

        return MyViewHolder(itemView, this)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val website = websiteList!![position]
        (holder as MyViewHolder).bindData(website, position)
    }

    override fun getItemCount(): Int {
        return websiteList!!.size
    }

    fun refreshList(websiteList: ArrayList<FreeVoiceTypeData>) {
        this.websiteList = websiteList
        notifyDataSetChanged()
    }


}