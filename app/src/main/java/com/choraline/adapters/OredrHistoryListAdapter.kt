package com.choraline.adapters

import android.content.Context
import android.os.Build
import androidx.recyclerview.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.choraline.BaseActivity
import com.choraline.R
import com.choraline.models.OrderHistoryData
import android.widget.LinearLayout
import com.choraline.models.OrderHistoryItemData

import kotlinx.android.synthetic.main.row_order_history.view.*
import kotlinx.android.synthetic.main.subrow_order_history.view.*

class OredrHistoryListAdapter(private val context: Context, open var actitvity: BaseActivity, private var websiteList: List<OrderHistoryData>) : RecyclerView.Adapter<OredrHistoryListAdapter.MyViewHolder>() {

     class MyViewHolder(view: View, open var outer: OredrHistoryListAdapter) : RecyclerView.ViewHolder(view) {
        lateinit  var d: OrderHistoryData
        fun bindData(data: OrderHistoryData, pos: Int)
        {
            d=data
            itemView.row_order_history_txtOrderDate.text=data!!.order_date
            outer!!.addOrderItem(itemView.row_order_history_layoutItemContainer, data!!.list, pos)
            if (Build.VERSION.SDK_INT >= 24) {
                itemView.row_order_history_txtSubtotal.text = Html.fromHtml(data!!.currency_symbol + " " + data!!.subtotal, 0)
                itemView.discount.text = Html.fromHtml( data!!.discount_percentage+"%", 0)

            }
            else
            {
                itemView.row_order_history_txtSubtotal.text = Html.fromHtml(data!!.currency_symbol + " " + data!!.subtotal.toString())
                itemView.discount.text = Html.fromHtml( data!!.discount_percentage.toString()+"%")


            }

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_order_history, parent, false)

        return MyViewHolder(itemView, this)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val website = websiteList!![position]
        (holder).bindData(website, position)
    }

    override fun getItemCount(): Int {
        return websiteList!!.size
    }

    fun refreshList(websiteList: List<OrderHistoryData>) {
        this.websiteList=websiteList
        notifyDataSetChanged()
    }

    private fun addOrderItem(container: LinearLayout, orderItemList: ArrayList<OrderHistoryItemData>, group: Int) {
        container.removeAllViews()
        for (i in 0..orderItemList!!.size - 1) {
            val view = LayoutInflater.from(context)!!.inflate(R.layout.subrow_order_history, null)
            view.subrow_order_history_txtTitle!!.text= orderItemList!!.get(i)!!.title+" "+ orderItemList!!.get(i)!!.subtitle
            view.voice_type.text=orderItemList!!.get(i)!!.voiceType
            if (Build.VERSION.SDK_INT >= 24) {
                view.subrow_order_history_txtPrice!!.text = Html.fromHtml(websiteList!!.get(group)!!.currency_symbol + " " + orderItemList!!.get(i)!!.price, 0)
            }
            else
            {
                view.subrow_order_history_txtPrice!!.text = Html.fromHtml(websiteList!!.get(group)!!.currency_symbol + " " + orderItemList!!.get(i)!!.price)
            }
            container!!.addView(view)
        }

    }


}