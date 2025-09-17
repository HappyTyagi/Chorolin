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
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.choraline.adapters.OredrHistoryListAdapter
import com.choraline.models.OrderHistoryData
import com.choraline.models.OrderHistoryModel
import com.choraline.network.APIListener
import com.choraline.network.Webservices
import com.choraline.utils.AppController
import com.choraline.utils.Constants
import com.choraline.utils.Utility


class OrderHistoryActivity : BaseActivity(), View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, APIListener {

    val TAG : String? = OrderHistoryActivity::class.simpleName
    private lateinit var context : Context
    var toolbar: Toolbar? = null
    var orderhistory_recycler: RecyclerView? = null
    var toolbar_title: TextView? = null
    var visit_web_ib: ImageButton? = null
    var tootlbar_imgbtnShare: ImageButton? = null
    var orderhistory_swiperefreshlayout: SwipeRefreshLayout? = null
    private var orderList=ArrayList<OrderHistoryData>()
    private  var adapter: OredrHistoryListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_history)
        context=this@OrderHistoryActivity
        toolbar = findViewById(R.id.toolbar)
        visit_web_ib = findViewById(R.id.visit_web_ib)
        tootlbar_imgbtnShare = findViewById(R.id.tootlbar_imgbtnShare)
        orderhistory_swiperefreshlayout = findViewById(R.id.orderhistory_swiperefreshlayout)
        toolbar_title = findViewById(R.id.toolbar_title)
        orderhistory_recycler = findViewById(R.id.orderhistory_recycler)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar_title!!.text = getString(R.string.text_order_history)
      //  realm = Realm.getDefaultInstance()
        adapter=OredrHistoryListAdapter(context, this, orderList)

        initUI()
    }

    fun initUI()
    {
        val layoutManager = LinearLayoutManager(context) // or requireContext() if inside a Fragment
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        orderhistory_recycler!!.layoutManager = layoutManager
        orderhistory_recycler!!.itemAnimator= DefaultItemAnimator()
        orderhistory_recycler!!.setAdapter(adapter)

        orderhistory_swiperefreshlayout!!.setOnRefreshListener(this)
        tootlbar_imgbtnShare!!.setOnClickListener(this)
        visit_web_ib!!.setOnClickListener{
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

        adapter!!.notifyDataSetChanged()
    }


}
