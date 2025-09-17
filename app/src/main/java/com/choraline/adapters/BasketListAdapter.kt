package com.choraline.adapters

import android.content.Context
import android.os.Build
import androidx.recyclerview.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.choraline.BaseActivity
import com.choraline.BasketActivity
import com.choraline.R
import com.choraline.models.BasketItemData


class BasketListAdapter(private val context: Context, open var actitvity: BaseActivity,
                        private var websiteList: List<BasketItemData>, open var currency_symbol: String)
    : RecyclerView.Adapter<BasketListAdapter.MyViewHolder>() {

     class MyViewHolder(view: View, open var outer: BasketListAdapter) : RecyclerView.ViewHolder(view) {
        lateinit  var d: BasketItemData

         private val row_basketitem_txtSongName: TextView = view.findViewById(R.id.row_basketitem_txtSongName)
         private val row_basketitem_txtPrice: TextView = view.findViewById(R.id.row_basketitem_txtPrice)
         private val with_singer: TextView = view.findViewById(R.id.with_singer)
         private val row_basketitem_imgbtnDelete: ImageButton = view.findViewById(R.id.row_basketitem_imgbtnDelete)



         fun bindData(data: BasketItemData, pos: Int)
        {
            d=data
            row_basketitem_txtSongName.text=data.title+"-"+data.subtitle+"-"+data.voiceType
            if (Build.VERSION.SDK_INT >= 24) {
                row_basketitem_txtPrice.text = Html.fromHtml(outer!!.currency_symbol+" "+data!!.price, 0)
            }
            else
            {
                row_basketitem_txtPrice.text = Html.fromHtml(outer!!.currency_symbol+" "+data!!.price.toString())
            }

            with_singer.text = "("+data.withSinger+")"
            if (data.withSinger.length>1)
            {
                with_singer.visibility = View.VISIBLE
            }else
            {
                with_singer.visibility = View.GONE
            }



            row_basketitem_imgbtnDelete.tag=""+pos
            row_basketitem_imgbtnDelete.setOnClickListener(deleteListener)

        }


         var deleteListener: View.OnClickListener = View.OnClickListener { v ->
             val str = v.tag as String
             val pos = Integer.parseInt(str)
             (outer.actitvity as BasketActivity).deleteBasketItem(pos)


         }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_basket_item, parent, false)

        return MyViewHolder(itemView, this)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val website = websiteList!![position] as BasketItemData
        (holder as MyViewHolder).bindData(website, position)
    }

    override fun getItemCount(): Int {
        return websiteList!!.size
    }

    fun refreshList(websiteList: List<BasketItemData>) {
        this.websiteList=websiteList
        notifyDataSetChanged()
    }


}