package com.choraline

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import com.google.android.material.navigation.NavigationView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.choraline.adapters.ComposersListAdapter
import com.choraline.models.*
import com.choraline.network.APIClient
import com.choraline.network.APIInterface
import com.choraline.network.APIListener
import com.choraline.network.Webservices
import com.choraline.utils.*
import com.google.gson.Gson
import com.saadahmedsoft.popupdialog.PopupDialog
import com.saadahmedsoft.popupdialog.Styles
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener

import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.content_home.*
import java.io.File
import java.io.IOException


class HomeActivity : BaseActivity(), View.OnClickListener, SwipeRefreshLayout.OnRefreshListener,
    NavigationView.OnNavigationItemSelectedListener, APIListener {

    val TAG: String = HomeActivity::class.simpleName!!.toString()
    private lateinit var context: Context
    var toolbar: Toolbar? = null
    var drawer: DrawerLayout? = null;
    var navigationView: NavigationView? = null
    var toggle: ActionBarDrawerToggle? = null
    private var intents: Intent? = null

    private var composerList = ArrayList<ComposerData>()
    private lateinit var adapter: ComposersListAdapter
    var selectedComposerPosition: Int = -1
    private lateinit var dialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        context = this@HomeActivity

        checkForUpdate()

        val toolbar = findViewById<Toolbar>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setupDrawer()

        dialog = ProgressDialog(this)
        dialog.setMessage("Please wait...")
        dialog.setCancelable(false)
        if (!Utility.isExternalStorageReadingAllowed(this) || !Utility.isExternalStorageWritingAllowed(
                this
            )
        ) {
            Utility.requestReadAndWriteExternalStoragePersmission(this)
        }
        adapter = ComposersListAdapter(context, this@HomeActivity, composerList)
        initUI()


//        if (Utility.getPopUp(this).equals("1")) {
//        } else {

        PopupDialog.getInstance(this)
            .setStyle(Styles.ALERT)
            .setHeading("Information!")
            .setDismissButtonText("OK")
            .setDismissButtonTextColor(R.color.white)
            .setDismissButtonTextColor(R.color.white)
            .setDismissButtonBackground(R.color.colorPrimary)
            .setDescription(
                "Any music in the Downloads section will have to be moved across again from the Purchased section. \n" +
                        "      Thank You"
            )
            .setCancelable(false)
            .showDialog(object : OnDialogButtonClickListener() {
                override fun onDismissClicked(dialog: Dialog?) {
                    super.onDismissClicked(dialog)
                }
            })


//            Utility.setPopup(this, "1")
//        }


    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (intent!!.getBooleanExtra("orderPlaced", false)) {
            var i = Intent(context, ThankYouActivity::class.java)
            startActivity(i)
        }

    }

    fun setupDrawer() {
        drawer = findViewById<DrawerLayout>(R.id.main_drawer_layout) as DrawerLayout
        navigationView = findViewById<NavigationView>(R.id.main_nav_view) as NavigationView
        navigationView!!.setNavigationItemSelectedListener(this)
        toggle = object : ActionBarDrawerToggle(
            this,
            drawer,
            toolbar,
            R.string.drawer_open,
            R.string.drawer_close
        ) {
            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
            }

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
            }
        }
        drawer!!.addDrawerListener(toggle as ActionBarDrawerToggle)
        toggle!!.syncState()
    }

    override fun onResume() {
        super.onResume()
        updateBasketCount()
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {

            R.id.action_choraline_shops -> {

            }

            R.id.action_purchased_music -> {
                intents = Intent(context, PurchasedMusicActivity::class.java)
                startActivity(intents)
            }

            R.id.action_order_history -> {
                intents = Intent(context, OrderHistoryActivity::class.java)
                startActivity(intents)
            }

            R.id.action_profile -> {
                intents = Intent(context, ProfileActivity::class.java)
                startActivity(intents)
            }

            R.id.action_about_us -> {
                intents = Intent(context, AboutUsActivity::class.java)
                startActivity(intents)
                //   Utility.openBrowawer(this,Constants.ABOUT_US_URL)
            }

            R.id.action_downloaded -> {
                intents = Intent(context, DownloadedSongAlbumActivity::class.java)
                intents!!.putExtra(Constants.AppConstants.TYPE, 0)
                intents!!.putExtra(Constants.AppConstants.TOOLBARTILE, "")
                val filedir = Environment.getExternalStorageDirectory().absolutePath + "/ChoraLine"
                intents!!.putExtra(Constants.AppConstants.TYPE, 0)
                intents!!.putExtra(Constants.AppConstants.PATH, filedir)
                startActivity(intents)
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


                // Utility.openBrowawer(this,Constants.PRIVACY_POLICY_URL)

            }

            R.id.action_logout -> {
                AppController.dbInstance.clearPurchaseTable()
                AppController.appPref.isLogin = false

                AppController.appPref.historyListTimeStamp = "0"
                AppController.appPref.purchaseSongTimeStamp = "0"

                AppController.appPref.clearSharedPreference()
                intents = Intent(context, WelcomActivity::class.java)
                startActivity(intents)
                finishAffinity()
            }


            R.id.visit_web -> {
                val browserIntent =
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://www.choraline.com/"))
                startActivity(browserIntent)

            }

        }
        drawer!!.closeDrawers()
        return true
    }

    fun initUI() {
        val mLayoutManager = GridLayoutManager(context, 3)
        home_recyclerComposer.setLayoutManager(mLayoutManager)
        val spacingInPixels = 25
        home_recyclerComposer.addItemDecoration(
            GridSpacingItemDecoration(
                3,
                spacingInPixels,
                true,
                0
            )
        )
        home_recyclerComposer.setAdapter(adapter)

        val strBasket = AppController.appPref.basketData
        if (!strBasket.equals(" ")) {
            val basketModel = Gson().fromJson(strBasket, BasketModel::class.java)
            if (basketModel != null && basketModel!!.status) {
                if (basketModel!!.response != null && basketModel!!.response!!.list != null) {
                    if (basketModel!!.response!!.list.size == 0) {
                        getBasket()
                    }

                } else {
                    getBasket()
                }
            } else {
                getBasket()
            }
        }

        if (composerList.size == 0)
            getComposerList()


        home_swipe!!.setOnRefreshListener(this)
        tootlbar_imgbtnShare!!.setOnClickListener(this)
        tootlbar_layoutBasket!!.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        if (v == tootlbar_imgbtnShare) {
            Utility!!.shareApp(context)
        } else if (v == tootlbar_layoutBasket) {
            val basketData =
                Gson()!!.fromJson(AppController!!.appPref!!.basketData, BasketModel::class.java)
            if (basketData != null) {
                val intents = Intent(context, BasketActivity::class.java)
                intents.putExtra("data", Gson().toJson(basketData))
                startActivity(intents)
            } else {
                Utility!!.showSnakeBar(layoutParent, "Your Basket is Empty");
            }
        }
    }

    override fun onRefresh() {

        getComposerList()
    }

    fun getComposerList() {
        home_recyclerComposer.visibility = View.INVISIBLE

        Webservices(
            context,
            this,
            !(home_swipe!!.isRefreshing),
            "Please wait..."
        ).callGetComposerListAPI(Constants.API_COMPOSER_LIST)

        /*  if (list.size==0) {
              Webservices(context, this, !(home_swipe!!.isRefreshing), "Please wait...").callGetComposerListAPI(Constants.API_COMPOSER_LIST)
          }else{
              composerList.addAll(list)
              if (adapter!=null) adapter.notifyDataSetChanged()
          }*/
    }

    fun getBasket() {


        Webservices(context, this, false, "").callGetBasketAPI(
            AppController.appPref.userId,
            Constants.API_GET_BASKET
        );

    }

    fun showChoralWorks(pos: Int) {
        this.selectedComposerPosition = pos
        getChoralWorks(composerList.get(pos).title)


    }


    fun openChoralWorksAcivity() {
        val result = AppController.dbInstance.getComposerSongsList(
            composerList.get(selectedComposerPosition).title,
            composerList.get(selectedComposerPosition).bannerImage
        )

        var choralWorksModel = ChoralWorksModel()
        choralWorksModel = Gson().fromJson("" + result, ChoralWorksModel::class.java);
        if (choralWorksModel.status) {
            var intents = Intent(context, ChoralWorksActivity::class.java);
            intents.putExtra("data", result)
            intents.putExtra("type", composerList.get(selectedComposerPosition).freeStatus)
            intents.putExtra("title", selectedComposerTitle)

            startActivity(intents)
        } else {
            Utility!!.showMessageDialog(context, choralWorksModel.message)
        }


    }

    var selectedComposerTitle: String = ""
    fun getChoralWorks(composerName: String) {
        selectedComposerTitle = composerName
        Webservices(context, this, true, "Please wait...").callGetChoralWorksAPI(
            composerName,
            AppController.dbInstance.getComposerAlbumTimeStamp(composerName),
            Constants.API_CHORAL_WORKS
        )
    }

    fun updateBasketCount() {
        val basketData =
            Gson()!!.fromJson(AppController!!.appPref!!.basketData, BasketModel::class.java)
        if (basketData != null) {
            if (basketData!!.response != null) {
                if (basketData!!.response!!.list != null && basketData!!.response!!.list!!.size > 0) {
                    tootlbar_txtBasketCount!!.visibility = View.VISIBLE
                    tootlbar_txtBasketCount.setText("" + basketData!!.response!!.list.size)
                } else {
                    tootlbar_txtBasketCount!!.visibility = View.GONE
                }
            } else {
                tootlbar_txtBasketCount!!.visibility = View.GONE
            }
        } else {
            tootlbar_txtBasketCount!!.visibility = View.GONE
        }
    }

    override fun onApiSuccess(obj: Any, api: Int) {

        home_recyclerComposer.visibility = View.VISIBLE
        if (api == Constants.API_COMPOSER_LIST) {
            if (home_swipe!!.isRefreshing)
                home_swipe!!.isRefreshing = false
            var result = obj as ComposerModel
            AppController.appPref.composerListTimeStamp = result.lastDate
            if (result.status) {
                if (result.response != null) {
                    composerList.clear()
                    // composerList.addAll(result!!.response!!.composerList)
                    // adapter.refreshList(composerList)

                    AppController.dbInstance.clearAlbum()

                    val insertAsync = InsertDataIntoDB()
                    val list = ArrayList<Any>()
                    list.add(obj)


                    insertAsync.execute(list)

                    // AppController.dbInstance.insertComposerListAndUpdate(result!!.response!!.composerList,result!!.response!!.updatedList,result!!.response!!.deletedList)
                }
            }

            //getComposerListFromdb()
        } else if (api == Constants.API_CHORAL_WORKS) {
            var result = obj as ChoralWorksModel
            if (result.status) {

                if (result.response != null) {
                    /* var intents = Intent(context, ChoralWorksActivity::class.java);
                     intents.putExtra("data", Gson().toJson(result))
                     intents.putExtra("title", selectedComposerTitle)*/

                    AppController.dbInstance.insertComposerSong(
                        Gson().toJson(result),
                        selectedComposerTitle,
                        result!!.response!!.updatepaidsonglist,
                        result!!.response!!.deletedpaidsonglist,
                        result!!.lastDate
                    )
                    //startActivity(intents)
                }

                openChoralWorksAcivity()
            } else if (!result.status) {
                Utility!!.showMessageDialog(context, result!!.message)
            }
        } else if (api == Constants.API_GET_BASKET) {
            val result = obj as BasketModel
            if (result != null) {
                if (result!!.status) {
                    AppController!!.appPref!!.basketData = Gson()!!.toJson(result)
                    updateBasketCount()

                } else {
                    AppController!!.appPref!!.basketData = ""
                }

            }
        }
    }


    inner class InsertDataIntoDB : AsyncTask<ArrayList<Any>, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            dialog.show()
        }

        override fun doInBackground(vararg params: ArrayList<Any>): String {

            val result = params.get(0).get(0) as ComposerModel
            AppController.dbInstance.insertComposerListAndUpdate(
                result!!.response!!.composerList,
                result!!.response!!.updatedList,
                result!!.response!!.deletedList
            )


            return "";

        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            dialog.dismiss()

            getComposerListFromdb()

        }
    }

    override fun onApiFailure(throwable: Throwable, api: Int) {
        home_recyclerComposer.visibility = View.VISIBLE
        if (home_swipe!!.isRefreshing)
            home_swipe!!.isRefreshing = false
        if (api == Constants.API_COMPOSER_LIST) getComposerListFromdb()
        if (api == Constants.API_CHORAL_WORKS) openChoralWorksAcivity()


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


    fun getComposerListFromdb() {
        composerList.clear()
        val list = AppController.dbInstance.getComposerList()
        composerList.addAll(list)
        adapter.notifyDataSetChanged()

    }


    override fun onDestroy() {
        AppLog.debugD(TAG, "home onDestroy")
        super.onDestroy()

    }


    private fun checkForUpdate() {

        var retrofit = APIClient.getClient()
        var retrofitInterface = retrofit!!.create(APIInterface::class.java)
        val request =
            retrofitInterface.appVersion("https://app.choraline.com/api/api.php?action=appVersion")
        try {


            checkForUpdateView()
        } catch (e: IOException) {

            e.printStackTrace()
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()

        }
    }


    private fun checkForUpdateView() {
        // Replace this with actual logic to check for updates
        val isUpdateAvailable = true // Assume update is available

        if (isUpdateAvailable) {
            val updateBottomSheet = UpdateBottomSheet()
            updateBottomSheet.show(supportFragmentManager, "UpdateBottomSheet")
        }
    }

}
