package com.choraline.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.choraline.BaseActivity
import com.choraline.ChoralWorksDetailActivity
import com.choraline.FreeTrailActivity
import com.choraline.R
import com.choraline.models.FreeVoiceTypeData
import com.choraline.models.VoiceTypeData
import java.util.ArrayList

import kotlinx.android.synthetic.main.row_voice_type.view.*

/**
 * Created by deepak Tyagi on 5/26/2017.
 */


class VoiceTypeListAdapter(private val context: Context, open var actitvity: BaseActivity, private var websiteList: List<VoiceTypeData>) : RecyclerView.Adapter<VoiceTypeListAdapter.MyViewHolder>() {

    val radioArr: IntArray = intArrayOf(R.drawable.custom_radiobutton_1, R.drawable.custom_radiobutton_2,
            R.drawable.custom_radiobutton_3, R.drawable.custom_radiobutton_4)

     class MyViewHolder(view: View, open var outer: VoiceTypeListAdapter) : RecyclerView.ViewHolder(view) {
        lateinit  var d: VoiceTypeData
        fun bindData(data: VoiceTypeData, pos: Int)
        {
            d=data

            if(data.type.contains("Soprano", true))
                itemView.row_voicetype_radioCheck.setButtonDrawable(outer.radioArr[0])
            else if(data.type.contains("Alto", true))
                itemView.row_voicetype_radioCheck.setButtonDrawable(outer.radioArr[1])
            else if(data.type.contains("Tenor", true))
                itemView.row_voicetype_radioCheck.setButtonDrawable(outer.radioArr[2])
            else if(data.type.contains("Bass", true))
                itemView.row_voicetype_radioCheck.setButtonDrawable(outer.radioArr[3])
            else{

                itemView.row_voicetype_radioCheck.setButtonDrawable(outer.radioArr[0])
            }

            itemView.row_voicetype_radioCheck.isChecked=data.isSelected
            itemView.row_voicetype_txtVoiceTitle.text=data.type
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

                 var vd=(outer.websiteList)[i]
                 vd.isSelected=false
             }
             ((outer.websiteList)[pos]).isSelected=true
             (outer.actitvity as ChoralWorksDetailActivity)!!.setSelectedVoiceType((outer.websiteList)[pos])
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

    fun refreshList(websiteList: ArrayList<VoiceTypeData>) {
        this.websiteList = websiteList
        notifyDataSetChanged()
    }


}