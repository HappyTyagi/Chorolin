package com.choraline

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle

import androidx.appcompat.widget.Toolbar
import android.text.Html
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.choraline.adapters.VoiceTypeListAdapter
import com.choraline.models.BaseModel
import com.choraline.models.BasketModel
import com.choraline.models.SongDetailModel
import com.choraline.models.VoiceTypeData
import com.choraline.network.APIListener
import com.choraline.network.Webservices
import com.choraline.utils.AppController
import com.choraline.utils.Constants
import com.choraline.utils.Utility
import com.google.gson.Gson
import com.squareup.picasso.Picasso



import kotlinx.android.synthetic.main.activity_choral_works_detail.*
import retrofit2.http.Field


class ChoralWorksDetailActivity : BaseActivity(), View.OnClickListener, APIListener {

    val TAG : String? = ChoralWorksDetailActivity::class.simpleName
    private lateinit var context : Context
   // var toolbar: Toolbar? = null

    lateinit var songDetailModel: SongDetailModel
    lateinit var adapter: VoiceTypeListAdapter

    var selectedCurrencyCode: String = ""
    var selectedCurrencyId: String = ""
    var selectedVoiceTypeId: String = ""
    var selectedSongId: String = ""
    var selectedSubtitle: String = ""
    var selectedPrice: String = ""
    var type = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choral_works_detail)
        context=this@ChoralWorksDetailActivity
      //  toolbar = findViewById<Toolbar>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        if(intent!=null)
        {
            selectedSongId= intent.getStringExtra("id")!!;
            songDetailModel= Gson().fromJson(intent.getStringExtra("data"), SongDetailModel::class.java);
            type = intent.getStringExtra("type")!!.toInt()
            if (type==0)
            {
                choralworksdetail_txtSelectCurrency.visibility= View.VISIBLE
                choralworksdetail_txtPrice.visibility = View.VISIBLE
                choralworksdetail_btnAddToBasket.text = "Add to basket"
            }else{
                choralworksdetail_txtSelectCurrency.visibility= View.GONE
                choralworksdetail_txtPrice.visibility = View.GONE
                choralworksdetail_btnAddToBasket.text = "Get"
            }
        }
        initUI()
    }

    fun initUI()
    {
        if(songDetailModel!=null) {
            selectedSubtitle=songDetailModel!!.response!!.subtitle
            adapter = VoiceTypeListAdapter(context, this, songDetailModel!!.response!!.voiceType)

            //total_voice_type.text="("+songDetailModel!!.response!!.voiceType.size+" Voice type available.)"
            Picasso.with(context).load(songDetailModel!!.response!!.banner_image).into(choralworksdetail_imgComposer)
            choralworksdetail_txtWorkTitle!!.text=songDetailModel!!.response!!.subtitle
            val mLayoutManager = LinearLayoutManager(context)
            mLayoutManager!!.orientation=LinearLayout.HORIZONTAL
            choralworksdetail_recyclerVoiceType!!.layoutManager=mLayoutManager
            choralworksdetail_recyclerVoiceType!!.itemAnimator= DefaultItemAnimator()
            choralworksdetail_recyclerVoiceType.setAdapter(adapter)
            adapter.notifyDataSetChanged()



            if (songDetailModel!!.response!!.voiceType.size>2)
            {
                total_voice_type.text="("+songDetailModel!!.response!!.voiceType.size+" available. Swipe to view)"
            }else
            {
                total_voice_type.text="("+songDetailModel!!.response!!.voiceType.size+" available)"
            }



            selectedCurrencyCode = songDetailModel!!.response!!.priceValue!!.get(0)!!.currencyCode
            selectedCurrencyId = songDetailModel!!.response!!.priceValue!!.get(0)!!.curid
            selectedPrice = ""+ songDetailModel!!.response!!.priceValue!!.get(0)!!.price
            choralworksdetail_txtSelectCurrency.text = songDetailModel!!.response!!.priceValue!!.get(0)!!.currencyName
            if (Build.VERSION.SDK_INT >= 24) {
                choralworksdetail_txtPrice.text = Html.fromHtml(songDetailModel!!.response!!.priceValue!!.get(0)!!.currencySymbol + " " + songDetailModel!!.response!!.priceValue!!.get(0)!!.price.toString(), 0)
            }
            else
            {
                choralworksdetail_txtPrice.text = Html.fromHtml(songDetailModel!!.response!!.priceValue!!.get(0)!!.currencySymbol + " " + songDetailModel!!.response!!.priceValue!!.get(0)!!.price.toString())
            }

            tootlbar_imgbtnShare.setOnClickListener(this)
            choralworksdetail_txtSelectCurrency.setOnClickListener(this)
            choralworksdetail_btnAddToBasket.setOnClickListener(this)
        }
    }

    override fun onClick(v: View?) {

        if(v==tootlbar_imgbtnShare)
        {
            Utility!!.shareApp(context)
        }
        else if(v==choralworksdetail_txtSelectCurrency)
        {
            openCurrencySpinner()
        }
        else if(v==choralworksdetail_btnAddToBasket)
        {
            if(selectedVoiceTypeId==null || selectedVoiceTypeId!!.equals(""))
            {
                Utility.showSnakeBar(layoutParent, "Select Voice Part.")
            }
            else
            {
                addToBasket()
            }
        }
    }

    fun setSelectedVoiceType(voiceData: VoiceTypeData)
    {
        selectedVoiceTypeId=voiceData!!.songId;

    }

    private fun openCurrencySpinner() {
        if(songDetailModel!=null) {
            if (songDetailModel!!.response!!.priceValue != null && songDetailModel!!.response!!.priceValue.size > 0) {
                val expcount = songDetailModel!!.response!!.priceValue.size
                val arr = arrayOfNulls<String>(songDetailModel!!.response!!.priceValue.size)
                for (i in 0..expcount - 1) {
                    arr[i] = songDetailModel!!.response!!.priceValue.get(i)!!.currencyName
                }

                val expDialogListener = DialogInterface.OnClickListener { dialog, which ->
                    selectedCurrencyCode = songDetailModel!!.response!!.priceValue!!.get(which)!!.currencyCode
                    selectedCurrencyId = songDetailModel!!.response!!.priceValue!!.get(which)!!.curid
                    selectedPrice = ""+ songDetailModel!!.response!!.priceValue!!.get(which)!!.price
                    choralworksdetail_txtSelectCurrency.text = songDetailModel!!.response!!.priceValue!!.get(which)!!.currencyName

                    if (Build.VERSION.SDK_INT >= 24) {
                        choralworksdetail_txtPrice.text = Html.fromHtml(songDetailModel!!.response!!.priceValue!!.get(which)!!.currencySymbol + " " + songDetailModel!!.response!!.priceValue!!.get(which)!!.price.toString(), 0)
                    }
                    else
                    {
                        choralworksdetail_txtPrice.text = Html.fromHtml(songDetailModel!!.response!!.priceValue!!.get(which)!!.currencySymbol + " " + songDetailModel!!.response!!.priceValue!!.get(which)!!.price.toString())
                    }
                }

                val expbuilder = AlertDialog.Builder(
                        context)
                expbuilder.setTitle("Select Currency")
                expbuilder.setItems(arr, expDialogListener)
                val expdialog = expbuilder.create()
                expdialog.show()
            }
        }

    }

    fun addToBasket()
    {
        if (type==1) checkFreeGift()
            else Webservices(context, this, true, "Please wait...").callAddToBasketAPI(selectedVoiceTypeId, selectedCurrencyId,
                AppController!!.appPref!!.userId, Constants.API_ADD_TO_BASKET)
    }

    override fun onApiSuccess(obj: Any, api: Int) {

        if(api==Constants.API_ADD_TO_BASKET)
        {
            val result=obj as BasketModel
            if(result!=null)
            {
                if(result!!.status)
                {
                    if (type==0) {
                        AppController!!.appPref!!.basketData = Gson()!!.toJson(result)
                        val intents = Intent(context, BasketActivity::class.java)
                        intents.putExtra("data", Gson().toJson(result))
                        startActivity(intents)
                    }else if (type==1)
                    {
                        buyFreeGift()
                    }

                }
                else
                {
                    Utility!!.showMessageDialog(context, result!!.message)
                }
            }
        }

        if (api== Constants.FREE_GIFT_CHECK_API)
        {
            val result=obj as BaseModel
            if (result.status)
            {
                buyFreeGift()
            } else
            {
                Utility!!.showMessageDialog(context, result!!.message)
            }
        }

        if (api== Constants.FREE_GIFT_API)
        {
            val intents = Intent(context, ThankYouActivity::class.java)

            startActivity(intents)
        }
    }

    fun buyFreeGift()
    {

        Webservices(context, this, true, "Please wait...").callFreeGift(selectedVoiceTypeId, selectedCurrencyId,
                AppController!!.appPref!!.userId, Constants.FREE_GIFT_API)
    }



    fun checkFreeGift()
    {
        Webservices(context, this, true, "Please wait...").callCheckFreeGift(selectedVoiceTypeId, selectedCurrencyId,
                AppController!!.appPref!!.userId, Constants.FREE_GIFT_CHECK_API)
    }

    override fun onApiFailure(throwable: Throwable,api: Int) {
              
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.getItemId()) {
            android.R.id.home -> {
                this.finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

}
