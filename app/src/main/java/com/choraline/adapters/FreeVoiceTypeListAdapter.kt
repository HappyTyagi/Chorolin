package com.choraline.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.choraline.BaseActivity
import com.choraline.FreeTrailActivity
import com.choraline.R
import com.choraline.models.FreeVoiceTypeData

class FreeVoiceTypeListAdapter(
    private val context: Context,
    open var activity: BaseActivity,
    private var websiteList: MutableList<FreeVoiceTypeData>
) : RecyclerView.Adapter<FreeVoiceTypeListAdapter.MyViewHolder>() {

    private val radioArr: IntArray = intArrayOf(
        R.drawable.custom_radiobutton_1,
        R.drawable.custom_radiobutton_2,
        R.drawable.custom_radiobutton_3,
        R.drawable.custom_radiobutton_4
    )

    inner class MyViewHolder(view: View, private val outer: FreeVoiceTypeListAdapter) :
        RecyclerView.ViewHolder(view) {

        private val radioCheck: RadioButton = view.findViewById(R.id.row_voicetype_radioCheck)
        private val txtVoiceTitle: TextView = view.findViewById(R.id.row_voicetype_txtVoiceTitle)
        private val layoutRow: LinearLayout = view.findViewById(R.id.row_voicetype_layoutRow)

        fun bindData(data: FreeVoiceTypeData, pos: Int) {
            // assign drawable based on voice type
            when {
                data.attr1.contains("Soprano", true) -> radioCheck.setButtonDrawable(outer.radioArr[0])
                data.attr1.contains("Alto", true) -> radioCheck.setButtonDrawable(outer.radioArr[1])
                data.attr1.contains("Tenor", true) -> radioCheck.setButtonDrawable(outer.radioArr[2])
                data.attr1.contains("Bass", true) -> radioCheck.setButtonDrawable(outer.radioArr[3])
            }

            radioCheck.isChecked = data.isSelected
            txtVoiceTitle.text = data.attr1

            // set tags for click listeners
            layoutRow.tag = pos
            radioCheck.tag = pos

            layoutRow.setOnClickListener(downloadClickListener)
            radioCheck.setOnClickListener(downloadClickListener)
        }

        private val downloadClickListener = View.OnClickListener { v ->
            val pos = v.tag.toString().toInt()

            // unselect all
            for (i in outer.websiteList.indices) {
                outer.websiteList[i].isSelected = false
            }

            // select clicked one
            outer.websiteList[pos].isSelected = true

            // notify activity
            (outer.activity as? FreeTrailActivity)?.setSelectedVoiceType(outer.websiteList[pos])

            // refresh list
            outer.notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.row_voice_type, parent, false)
        return MyViewHolder(itemView, this)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindData(websiteList[position], position)
    }

    override fun getItemCount(): Int = websiteList.size

    fun refreshList(newList: List<FreeVoiceTypeData>) {
        websiteList.clear()
        websiteList.addAll(newList)
        notifyDataSetChanged()
    }
}
