package com.choraline.adapters

import android.content.Context
import android.os.Build
import androidx.recyclerview.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.choraline.BaseActivity
import com.choraline.BasketActivity
import com.choraline.HomeActivity
import com.choraline.R
import com.choraline.models.BasketItemData
import com.choraline.models.ComposerData
import kotlinx.android.synthetic.main.activity_choral_works_detail.*

import kotlinx.android.synthetic.main.row_basket_item.view.*

class BasketListAdapter(private val context: Context, open var actitvity: BaseActivity,
                        private var websiteList: List<BasketItemData>, open var currency_symbol: String)
    : RecyclerView.Adapter<BasketListAdapter.MyViewHolder>() {

     class MyViewHolder(view: View, open var outer: BasketListAdapter) : RecyclerView.ViewHolder(view) {
        lateinit  var d: BasketItemData
        fun bindData(data: BasketItemData, pos: Int)
        {
            d=data
            itemView.row_basketitem_txtSongName.text=data.title+"-"+data.subtitle+"-"+data.voiceType
            if (Build.VERSION.SDK_INT >= 24) {
                itemView.row_basketitem_txtPrice.text = Html.fromHtml(outer!!.currency_symbol+" "+data!!.price, 0)
            }
            else
            {
                itemView.row_basketitem_txtPrice.text = Html.fromHtml(outer!!.currency_symbol+" "+data!!.price.toString())
            }

            itemView.with_singer.text = "("+data.withSinger+")"
            if (data.withSinger.length>1)
            {
                itemView.with_singer.visibility = View.VISIBLE
            }else
            {
                itemView.with_singer.visibility = View.GONE
            }



            itemView.row_basketitem_imgbtnDelete.tag=""+pos
            itemView.row_basketitem_imgbtnDelete.setOnClickListener(deleteListener)

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