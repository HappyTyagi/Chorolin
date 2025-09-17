package com.choraline.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.choraline.BaseActivity
import com.choraline.HomeActivity
import com.choraline.R
import com.choraline.models.ComposerData


/**
 * Created by deepak Tyagi on 5/26/2017.
 */



class ComposersListAdapter(private val context: Context, open var actitvity: BaseActivity, private var websiteList: List<ComposerData>) : RecyclerView.Adapter<ComposersListAdapter.MyViewHolder>() {

   inner class MyViewHolder(view: View, open var outer: ComposersListAdapter) : RecyclerView.ViewHolder(view) {

       private val row_composers_imgComposer: ImageView = view.findViewById(R.id.row_composers_imgComposer)


       lateinit var d: ComposerData
        fun bindData(data: ComposerData, pos: Int) {
            d = data
           /* Picasso.with(outer.context)
                    .load(d.composerImage)
                    .placeholder(R.mipmap.app_icon)
                    .into(itemView.row_composers_imgComposer)*/
            Glide.with(context)
                    .load(d.composerImage)
                    .placeholder(R.mipmap .app_icon)
                    .into(row_composers_imgComposer);
           // Glide.with(context).lo
            row_composers_imgComposer.contentDescription = "" + pos
            row_composers_imgComposer.setOnClickListener(childClickListener)

        }


        var childClickListener: View.OnClickListener = View.OnClickListener { v ->
            val str = v.contentDescription as String
            val pos = Integer.parseInt(str)
            (outer.actitvity as HomeActivity).showChoralWorks(pos)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_composers, parent, false)

        return MyViewHolder(itemView, this)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val website = websiteList!![position] as ComposerData
        (holder as MyViewHolder).bindData(website, position)
    }

    override fun getItemCount(): Int {
        return websiteList!!.size
    }

    fun refreshList(websiteList: List<ComposerData>) {
        this.websiteList = websiteList
        notifyDataSetChanged()
    }


}