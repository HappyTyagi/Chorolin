package com.choraline.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import com.choraline.BaseActivity
import com.choraline.ChoralWorksDetailActivity
import com.choraline.R
import com.choraline.models.VoiceTypeData
import java.util.ArrayList


/**
 * Created by deepak Tyagi on 5/26/2017.
 */


class VoiceTypeListAdapter(private val context: Context, open var actitvity: BaseActivity, private var websiteList: List<VoiceTypeData>) : RecyclerView.Adapter<VoiceTypeListAdapter.MyViewHolder>() {

    val radioArr: IntArray = intArrayOf(R.drawable.custom_radiobutton_1, R.drawable.custom_radiobutton_2,
            R.drawable.custom_radiobutton_3, R.drawable.custom_radiobutton_4)

    class MyViewHolder(view: View, var outer: VoiceTypeListAdapter) : RecyclerView.ViewHolder(view) {

        private val radioCheck = view.findViewById<RadioButton>(R.id.row_voicetype_radioCheck)
        private val txtVoiceTitle = view.findViewById<TextView>(R.id.row_voicetype_txtVoiceTitle)
        private val layoutRow = view.findViewById<LinearLayout>(R.id.row_voicetype_layoutRow)

        lateinit var d: VoiceTypeData

        fun bindData(data: VoiceTypeData, pos: Int) {
            d = data

            // set drawable based on type
            when {
                data.type.contains("Soprano", true) -> radioCheck.setButtonDrawable(outer.radioArr[0])
                data.type.contains("Alto", true)    -> radioCheck.setButtonDrawable(outer.radioArr[1])
                data.type.contains("Tenor", true)   -> radioCheck.setButtonDrawable(outer.radioArr[2])
                data.type.contains("Bass", true)    -> radioCheck.setButtonDrawable(outer.radioArr[3])
                else -> radioCheck.setButtonDrawable(outer.radioArr[0])
            }

            radioCheck.isChecked = data.isSelected
            txtVoiceTitle.text = data.type

            layoutRow.tag = "$pos"
            radioCheck.tag = "$pos"

            layoutRow.setOnClickListener(downloadClickListener)
            radioCheck.setOnClickListener(downloadClickListener)
        }

        private val downloadClickListener = View.OnClickListener { v ->
            val pos = (v.tag as String).toInt()

            // Unselect all
            outer.websiteList.forEach { it.isSelected = false }

            // Select current
            outer.websiteList[pos].isSelected = true
            (outer.actitvity as ChoralWorksDetailActivity)
                .setSelectedVoiceType(outer.websiteList[pos])

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