package com.choraline

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.choraline.adapters.OredrHistoryListAdapter
import com.choraline.models.OrderHistoryData
import com.choraline.models.OrderHistoryModel
import com.choraline.network.APIListener
import com.choraline.network.Webservices
import com.choraline.utils.AppController
import com.choraline.utils.Constants
import com.choraline.utils.Utility

import kotlinx.android.synthetic.main.activity_order_history.*
import org.json.JSONArray
import kotlin.reflect.jvm.internal.impl.utils.CollectionsKt

class OrderHistoryActivity : BaseActivity(), View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, APIListener {

    val TAG : String? = OrderHistoryActivity::class.simpleName
    private lateinit var context : Context
    var toolbar: Toolbar? = null
    private var orderList=ArrayList<OrderHistoryData>()
    private lateinit var adapter: OredrHistoryListAdapter
    // lateinit var realm:Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_history)
        context=this@OrderHistoryActivity
        toolbar = findViewById<Toolbar>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar_title.text = getString(R.string.text_order_history)
      //  realm = Realm.getDefaultInstance()
        adapter=OredrHistoryListAdapter(context, this, orderList)

        initUI()
    }

    fun initUI()
    {
        val mLayoutManager = LinearLayoutManager(context)
        mLayoutManager!!.orientation= LinearLayout.VERTICAL
        orderhistory_recycler!!.layoutManager=mLayoutManager
        orderhistory_recycler!!.itemAnimator= DefaultItemAnimator()
        orderhistory_recycler.setAdapter(adapter)

        orderhistory_swiperefreshlayout!!.setOnRefreshListener(this)
        tootlbar_imgbtnShare.setOnClickListener(this)
        visit_web_ib.setOnClickListener{
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.choraline.com/"))
            startActivity(browserIntent)
        }
        //Utility!!.showSnakeBar(layoutParent, "Under Development")
        getOrderHistory()
    }

    override fun onClick(v: View?) {

        if(v==tootlbar_imgbtnShare)
        {
            Utility!!.shareApp(context)
        }
    }

    override fun onRefresh() {
        getOrderHistory()
    }


    fun getOrderHistory()
    {
        Webservices(context, this, !(orderhistory_swiperefreshlayout!!.isRefreshing), "Please wait...")!!.callGetOrderHistoryAPI(AppController!!.appPref!!.userId,
                Constants.API_GET_ORDER_HISTORY);
    }

    override fun onApiSuccess(obj: Any, api: Int) {

        if(api == Constants.API_GET_ORDER_HISTORY)
        {
            if(orderhistory_swiperefreshlayout!!.isRefreshing)
                orderhistory_swiperefreshlayout!!.isRefreshing=false

            val result=obj as OrderHistoryModel



            if(result!!.status)
            {
                AppController.appPref.historyListTimeStamp=result.lastDate

                if(result!!.response!=null && result!!.response!!.orderlist!!.size>0)
                {

                    orderList!!.clear()

                      AppController.dbInstance.insertHistoryData(result!!.response!!.orderlist)
                    adapter!!.refreshList(orderList)
                }
            }

            getHistoryDataFromDb()
        }
    }

    override fun onApiFailure(throwable: Throwable,api: Int) {

        if(orderhistory_swiperefreshlayout!!.isRefreshing)
            orderhistory_swiperefreshlayout!!.isRefreshing=false
        getHistoryDataFromDb()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> {
                this.finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }


    fun getHistoryDataFromDb()
    {
        orderList!!.clear()
        orderList!!.addAll(AppController.dbInstance.getHistoryData().reversed())

        adapter.notifyDataSetChanged()
    }


}
