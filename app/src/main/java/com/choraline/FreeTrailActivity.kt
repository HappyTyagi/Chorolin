package com.choraline

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.widget.Toolbar
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import com.choraline.adapters.FreeVoiceTypeListAdapter
import com.choraline.models.FreeTrailData
import com.choraline.models.SongsModel
import com.choraline.models.FreeVoiceTypeData
import com.choraline.network.APIListener
import com.choraline.network.InformationStorage
import com.choraline.network.Webservices
import com.choraline.utils.Constants
import com.choraline.utils.Utility
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Picasso



class FreeTrailActivity : BaseActivity(), View.OnClickListener, NavigationView.OnNavigationItemSelectedListener, APIListener {

    val TAG: String? = HomeActivity::class.simpleName
    lateinit var context: Context
    private var intents: Intent? = null
    var voiceList = ArrayList<FreeVoiceTypeData>()
    private lateinit var adapter: FreeVoiceTypeListAdapter
    private lateinit var freeTrailModel: FreeTrailData
    private var selectedVoiceType: FreeVoiceTypeData? = null
    var toggle: ActionBarDrawerToggle? = null
    var toolbar : Toolbar? = null
    var freetrail_navigationView : NavigationView? = null
    var freetrail_drawerlayout : DrawerLayout? = null
    var tootlbar_imgbtnShare : ImageButton? = null
    var freetrail_txtBack : TextView? = null
    var freetrail_txtComposerName : TextView? = null
    var freetrail_imgComposer : ImageView? = null
    var freetrail_txtCategoryName : TextView? = null
    var total_voice_type : TextView? = null
    var layoutParent : RelativeLayout? = null
    var freetrail_btnProceed : Button? = null
    var freetrail_recyclerVoiceType : RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_free_trail)
        context = this
        toolbar = findViewById(R.id.toolbar)
        layoutParent = findViewById(R.id.layoutParent)
        total_voice_type = findViewById(R.id.total_voice_type)
        freetrail_imgComposer = findViewById(R.id.freetrail_imgComposer)
        freetrail_txtCategoryName = findViewById(R.id.freetrail_txtCategoryName)
        freetrail_recyclerVoiceType = findViewById(R.id.freetrail_recyclerVoiceType)
        freetrail_txtBack = findViewById(R.id.freetrail_txtBack)
        freetrail_btnProceed = findViewById(R.id.freetrail_btnProceed)
        freetrail_txtComposerName = findViewById(R.id.freetrail_txtComposerName)
        freetrail_navigationView = findViewById(R.id.freetrail_navigationView)
        freetrail_drawerlayout = findViewById(R.id.freetrail_drawerlayout)
        tootlbar_imgbtnShare = findViewById(R.id.tootlbar_imgbtnShare)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setupDrawer()
        initUI()
    }

    fun setupDrawer() {

        freetrail_navigationView!!.setNavigationItemSelectedListener(this)
        toggle = object : ActionBarDrawerToggle(this, freetrail_drawerlayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
            }

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
            }
        }
        freetrail_drawerlayout!!.addDrawerListener(toggle as ActionBarDrawerToggle)
        toggle!!.syncState()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (toggle!!.onOptionsItemSelected(item)) {
            return true
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item)
    }

    fun initUI() {
        tootlbar_imgbtnShare!!.setOnClickListener(this)
        val loginString = SpannableString(getString(R.string.text_back))
        loginString.setSpan(StyleSpan(Typeface.BOLD), 0, loginString.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        val signupClickableSpan = object : ClickableSpan() {
            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = true
            }

            override fun onClick(textView: View) {

                finish()
            }
        }

        loginString.setSpan(signupClickableSpan, 0, loginString.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        freetrail_txtBack!!.setText(loginString)
        freetrail_txtBack!!.setMovementMethod(LinkMovementMethod.getInstance())
        if (InformationStorage.instance.freeTrailModel != null) {
            freeTrailModel = InformationStorage.instance.freeTrailModel as FreeTrailData

            /*GlideApp.with(context)
                    .load(freeTrailModel.composerImage)
                    .placeholder(R.mipmap.jenkins)
                    .fitCenter()
                    .into(freetrail_imgComposer);*/
            Picasso.get()
                .load(freeTrailModel.bannerImage)
                .into(freetrail_imgComposer)

            if (freeTrailModel!!.voicetype!!.size > 0) {
                for (i in 0..freeTrailModel!!.voicetype!!.size - 1)
                    freeTrailModel!!.voicetype[i].isSelected = false;
            }

            freetrail_txtComposerName!!.setText(freeTrailModel.composerName)
            freetrail_txtCategoryName!!.setText(freeTrailModel.categoryName)
            freeTrailModel.voicetype[0].isSelected = true
            selectedVoiceType = freeTrailModel.voicetype[0]
            adapter = FreeVoiceTypeListAdapter(context, this, freeTrailModel.voicetype)

            val mLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            freetrail_recyclerVoiceType!!.layoutManager = mLayoutManager

            freetrail_recyclerVoiceType!!.setLayoutManager(mLayoutManager)
            freetrail_recyclerVoiceType!!.setItemAnimator(DefaultItemAnimator())
            freetrail_recyclerVoiceType!!.setAdapter(adapter)

            if (freeTrailModel.voicetype.size > 2) {
                //
                total_voice_type!!.text = "(" + freeTrailModel.voicetype.size + " available. Swipe to view)"
            } else {
                total_voice_type!!.text = "(" + freeTrailModel.voicetype.size + " available)"
            }




            freetrail_btnProceed!!.setOnClickListener(this)
        }

    }

    override fun onClick(v: View?) {

        if (v == tootlbar_imgbtnShare) {
            Utility.shareApp(context)
        } else if (v == freetrail_btnProceed) {
            if (selectedVoiceType != null && selectedVoiceType!!.attr1 != "") {
                getSongsList()
            } else {
                Utility.showSnakeBar(layoutParent!!, "Select Voice Part.")
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when (id) {
            R.id.action_join -> {
                intents = Intent(context, SignupActivity::class.java)
                startActivity(intents)
            }
            R.id.action_about_us -> {
                intents = Intent(context, AboutUsActivity::class.java)
                startActivity(intents)
                // Utility.openBrowawer(this,Constants.ABOUT_US_URL)

            }
            R.id.action_faq -> {
                intents = Intent(context, FAQActivity::class.java)
                startActivity(intents)
                // Utility.openBrowawer(this,Constants.FAQ_URL)
            }
            R.id.action_contact_us -> {
                intents = Intent(context, ContactUsActivity::class.java)
                startActivity(intents)
            }
            R.id.action_terms_and_conditions -> {
                intents = Intent(context, TermsAndConditionsActivity::class.java)
                startActivity(intents)
                // Utility.openBrowawer(this,Constants.TERMS_AND_CONDITIONS_URL)
            }
            R.id.action_privacy_policy -> {
                intents = Intent(context, PrivacyPlicyActivity::class.java)
                startActivity(intents)

                //Utility.openBrowawer(this,Constants.PRIVACY_POLICY_URL)
            }

            R.id.visit_web -> {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.choraline.com/"))
                startActivity(browserIntent)
            }
        }

        freetrail_drawerlayout!!.closeDrawers()
        return true
    }

    fun setSelectedVoiceType(voiceData: FreeVoiceTypeData) {
        selectedVoiceType = voiceData;
    }

    fun getSongsList() {
        Webservices(context, this, true, "Please wait...").callGetSongsListAPI(selectedVoiceType!!.attr1,
                Constants.API_GET_FREE_TRAIL_MUSIC_LIST)
    }

    override fun onApiSuccess(obj: Any, api: Int) {

        if (api == Constants.API_GET_FREE_TRAIL_MUSIC_LIST) {
            val result = obj as SongsModel
            if (result.status) {
                if (result.response != null) {
                    if (result.response!!.songlist != null) {
                        var b = Bundle()
                        //result.response!!.songlist!!.add(result.response!!.songlist!!.get(0))
                        b.putParcelableArrayList("songsList", result.response!!.songlist)
                        b.putString("voiceType", selectedVoiceType!!.attr1)
                        intents = Intent(context, FreeTrailSongsActivity::class.java)
                        intents!!.putExtras(b)
                        startActivity(intents)
                    }
                }
            }
        }
    }

    override fun onApiFailure(throwable: Throwable, api: Int) {

    }

}
