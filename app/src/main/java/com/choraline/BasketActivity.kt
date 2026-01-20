package com.choraline

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.telephony.TelephonyManager
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.braintreepayments.api.*
import com.braintreepayments.cardform.view.CardForm
import com.choraline.adapters.BasketListAdapter
import com.choraline.models.BasketItemData
import com.choraline.models.BasketModel
import com.choraline.models.PurchaseData
import com.choraline.models.PurchaseDataModel
import com.choraline.models.SubmitOrderModel
import com.choraline.network.APIListener
import com.choraline.network.Webservices
import com.choraline.services.DemoClientTokenProvider
import com.choraline.utils.AppController
import com.choraline.utils.AppLog
import com.choraline.utils.Constants
import com.choraline.utils.Utility
import com.google.gson.Gson
import com.saadahmedev.popupdialog.PopupDialog
import java.util.Locale

class BasketActivity : BaseActivity(), View.OnClickListener, APIListener, DropInListener {

    lateinit var billingClient: BillingClient

    private var dropInClient: DropInClient? = null
    val TAG: String = BasketActivity::class.simpleName!!.toString()
    private lateinit var context: Context
    var toolbar: Toolbar? = null
    private var isBillingReady = false

    var basket_recyclerBasket: RecyclerView? = null
    var basket_txtContinueShopping: TextView? = null
    var basket_btnPurchase: Button? = null
    var tootlbar_imgbtnShare: ImageButton? = null
    var basket_btnEnter: Button? = null
    var basket_txtTotalPrice: TextView? = null
    var basket_layoutDiscout: LinearLayout? = null
    var basket_edtDiscountCode: EditText? = null
    var layoutParent: RelativeLayout? = null

    lateinit var basketModel: BasketModel
    lateinit var adapter: BasketListAdapter
    lateinit var purchaseData: PurchaseData
    private var discountCode: String = ""
    private var productId: String = ""
    private var orderID: String = ""
    private var isCouponApply = false;
    var country = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basket)
        context = this@BasketActivity
        toolbar = findViewById(R.id.toolbar)

        country = getUserCountry()

        basket_recyclerBasket = findViewById(R.id.basket_recyclerBasket)
        basket_txtContinueShopping = findViewById(R.id.basket_txtContinueShopping)
        basket_btnPurchase = findViewById(R.id.basket_btnPurchase)
        tootlbar_imgbtnShare = findViewById(R.id.tootlbar_imgbtnShare)
        basket_btnEnter = findViewById(R.id.basket_btnEnter)
        basket_txtTotalPrice = findViewById(R.id.basket_txtTotalPrice)
        basket_layoutDiscout = findViewById(R.id.basket_layoutDiscout)
        basket_edtDiscountCode = findViewById(R.id.basket_edtDiscountCode)
        layoutParent = findViewById(R.id.layoutParent)


        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        if (intent != null) {
            Log.e("result", intent.getStringExtra("data").toString())
//            getBasket()
            basketModel = Gson().fromJson(intent.getStringExtra("data"), BasketModel::class.java);

        }

        initUI()
        initBilling(this)
        dropInClient = DropInClient(this, DemoClientTokenProvider(this))
        dropInClient!!.setListener(this@BasketActivity);

    }

    fun initBilling(context: Context) {
        billingClient = BillingClient.newBuilder(context)
            .enablePendingPurchases()
            .setListener(purchaseListener)
            .build()

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(result: BillingResult) {
                if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                    isBillingReady = true
                    Log.d("BILLING", "Billing Client READY")
                }
            }

            override fun onBillingServiceDisconnected() {
                isBillingReady = false
            }
        })
    }

    fun getBasket() {


        Webservices(context, this, false, "").callGetBasketAPI(
            AppController.appPref.userId,
            Constants.API_GET_BASKET
        );

    }


    val purchaseListener = PurchasesUpdatedListener { result, purchases ->
        if (result.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (purchase in purchases) {
                handlePurchase(purchase)
            }
        }
    }


    fun handlePurchase(purchase: Purchase) {

        val gson = Gson()
        val json = gson.toJson(purchase)
        Log.d("PURCHASE_JSON", json)
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            Log.d("BILLING", "Purchase SUCCESS")
//            val productId = purchase.products.first()
            unlockSong(purchase)

            if (!purchase.isAcknowledged) {
                val params = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                    .build()

                billingClient.acknowledgePurchase(params) {
                    Log.d("BILLING", "Purchase acknowledged")
                }
            }

        } else {
            Log.d("BILLING", "Purchase FAILED or PENDING")
        }
    }

    fun unlockSong(purcahse: Purchase) {
        doGoogleSubmitOrder(purcahse)

    }


    private var isBillingInProgress = false

    fun buySong(activity: Activity, productId: String) {

        Log.e("productId", productId)
        // 1️⃣ Activity state check
        if (activity.isFinishing || activity.isDestroyed) {
            Log.e("BILLING", "Invalid activity state")
            return
        }

        // 2️⃣ Prevent multiple calls
        if (isBillingInProgress) {
            Log.e("BILLING", "Billing already in progress")
            return
        }

        // 3️⃣ Billing connection check
        if (!billingClient.isReady) {
            Log.e("BILLING", "Billing not ready, reconnecting")
            startBillingConnection()
            return
        }

        isBillingInProgress = true

        // 4️⃣ ASYNC check if THIS product is already owned
        billingClient.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        ) { billingResult, purchasesList ->

            if (billingResult.responseCode != BillingClient.BillingResponseCode.OK) {
                Log.e("BILLING", "Purchase query failed")
                isBillingInProgress = false
                return@queryPurchasesAsync
            }

            val alreadyOwned = purchasesList.any { purchase ->
                purchase.products.contains(productId) // ✅ correct for Billing v5+
            }

            if (alreadyOwned) {
                Log.e("BILLING", "Product already owned: $productId")
                Utility.showSnakeBar(layoutParent!!, "Product already owned")
                isBillingInProgress = false
                return@queryPurchasesAsync
            }

            // 5️⃣ Query product details
            val query = QueryProductDetailsParams.newBuilder()
                .setProductList(
                    listOf(
                        QueryProductDetailsParams.Product.newBuilder()
                            .setProductId(productId)
                            .setProductType(BillingClient.ProductType.INAPP)
                            .build()
                    )
                )
                .build()

            billingClient.queryProductDetailsAsync(query) { result, detailsList ->

                Log.e("BILLING1", result.toString())
                Log.e("BILLING1", detailsList.toString())


                if (result.responseCode != BillingClient.BillingResponseCode.OK) {
                    Log.e("BILLING", "Product query failed: ${result.debugMessage}")
                    isBillingInProgress = false
                    return@queryProductDetailsAsync
                }

                if (detailsList.isEmpty()) {
                    Log.e("BILLING", "Product not found in Play Console")
                    isBillingInProgress = false
                    return@queryProductDetailsAsync
                }

                // 6️⃣ Launch billing flow
                val billingParams = BillingFlowParams.newBuilder()
                    .setProductDetailsParamsList(
                        listOf(
                            BillingFlowParams.ProductDetailsParams.newBuilder()
                                .setProductDetails(detailsList[0])
                                .build()
                        )
                    )
                    .build()

                val launchResult = billingClient.launchBillingFlow(activity, billingParams)

                Log.d("BILLING_LAUNCH_CODE", launchResult.responseCode.toString())
                Log.d("BILLING_LAUNCH_MSG", launchResult.debugMessage)

                if (launchResult.responseCode != BillingClient.BillingResponseCode.OK) {
                    isBillingInProgress = false
                }
            }
        }
    }

    private fun startBillingConnection() {

        billingClient.startConnection(object : BillingClientStateListener {

            override fun onBillingSetupFinished(billingResult: BillingResult) {

                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    Log.d("BILLING", "Billing setup successful")
                } else {
                    Log.e(
                        "BILLING",
                        "Billing setup failed: ${billingResult.debugMessage}"
                    )
                }
            }

            override fun onBillingServiceDisconnected() {
                Log.e("BILLING", "Billing service disconnected")
                // Google Play will reconnect automatically, or you can retry later
            }
        })
    }

    fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchases: MutableList<Purchase>?
    ) {
        isBillingInProgress = false

        when (billingResult.responseCode) {

            BillingClient.BillingResponseCode.OK -> {
                Log.d("BILLING", "Payment success")
            }

            BillingClient.BillingResponseCode.USER_CANCELED -> {
                Log.e("BILLING", "User cancelled payment")
            }

            else -> {
                Log.e("BILLING", "Payment failed: ${billingResult.debugMessage}")
            }
        }
    }


    fun initUI() {
        if (basketModel != null) {
            adapter = BasketListAdapter(
                context,
                this,
                basketModel.response!!.list,
                basketModel.response!!.currency_symbol
            )
            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            basket_recyclerBasket!!.layoutManager = layoutManager
            basket_recyclerBasket!!.itemAnimator = DefaultItemAnimator()
            basket_recyclerBasket!!.setAdapter(adapter)

            val loginString = SpannableString(getString(R.string.text_continue_shopping))
            loginString.setSpan(
                StyleSpan(Typeface.NORMAL),
                0,
                loginString.length,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE
            )
            val signupClickableSpan = object : ClickableSpan() {
                override fun updateDrawState(ds: TextPaint) {
                    ds.color = Color.WHITE
                    ds.isUnderlineText = true
                }

                override fun onClick(textView: View) {
                    finishAffinity()
                    val int = Intent(context, HomeActivity::class.java)
                    int.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    int.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    int.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(int)
                }
            }

            loginString.setSpan(
                signupClickableSpan,
                loginString.toString().indexOf("?") + 1,
                loginString.length,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE
            )
            basket_txtContinueShopping!!.setText(loginString)
            basket_txtContinueShopping!!.setMovementMethod(LinkMovementMethod.getInstance())

            setValues()

            tootlbar_imgbtnShare!!.setOnClickListener(this)
            basket_btnEnter!!.setOnClickListener(this)
            basket_btnPurchase!!.setOnClickListener(this)
        }
    }

    fun setValues() {
        if (basketModel.response!!.list.size == 0) {
            basket_layoutDiscout!!.visibility = View.GONE
            basket_txtTotalPrice!!.visibility = View.INVISIBLE
            basket_btnPurchase!!.visibility = View.GONE
        } else {
            basket_layoutDiscout!!.visibility = View.GONE
            basket_txtTotalPrice!!.visibility = View.VISIBLE
            basket_btnPurchase!!.visibility = View.VISIBLE
        }

        basket_txtTotalPrice!!.text = Html.fromHtml(
            basketModel.response!!.currency_symbol + " " + basketModel.response!!.subtotal,
            0
        )
    }

    override fun onClick(v: View?) {
        Utility.hideSoftKeyboard(this)
        if (v == tootlbar_imgbtnShare) {
            Utility.shareApp(context)
        } else if (v == basket_btnEnter) {
            if (basket_edtDiscountCode!!.text.toString().equals("")) {
                Utility.showSnakeBar(layoutParent!!, "Please Enter the Discount Code.")
            } else {
                if (isCouponApply) {
                    removeDiscountCoupon()
                } else
                    applyDiscountCode()
            }
        } else if (v == basket_btnPurchase) {


            var listItem = basketModel.response!!.list;


//            if (country == "IN" || country == "RU") {
//                doPurchase()
//            } else {
            if (listItem.size > 1) {
                PopupDialog.getInstance(context)
                    .statusDialogBuilder()
                    .createWarningDialog()
                    .setHeading("Pending")
                    .setDescription("When purchasing items in an app through Google Play services, we can only buy one product at a time.")
                    .build { dialog ->
                        dialog.dismiss()
                    }
                    .show()
            } else {
                getBasket()

                doPurchase()
            }
//            }
        }
    }

    fun deleteBasketItem(pos: Int) {
        Webservices(context, this, true, "Please Wait...").callDeleteBasketItemAPI(
            basketModel.response!!.list[pos].id,
            basketModel.response!!.currency_id,
            AppController.appPref.userId,
            Constants.API_DELETE_BASKET_ITEM
        )
    }

    fun applyDiscountCode() {
        Webservices(context, this, true, "Please Wait...").callApplyDiscountcodeAPI(
            basket_edtDiscountCode!!.text.toString(),
            basketModel.response!!.currency_id,
            AppController.appPref.userId,
            Constants.API_APPLY_DISCOUNT_CODE
        )
    }


    fun removeDiscountCoupon() {
        Webservices(context, this, true, "Please Wait...").callRemoveCoupon(
            basketModel.response!!.currency_id,
            AppController.appPref.userId,
            Constants.API_APPLY_DISCOUNT_CODE
        )

    }


    fun doPurchase() {


        Webservices(context, this, true, "Please Wait...").callPurchaseAPI(
            AppController.appPref.userId,
            discountCode, basketModel.response!!.subtotal,
            basketModel.response!!.currency_id, Constants.API_PURCHASE
        )


    }

    fun doSubmitOrder(nonce: String) {
        Webservices(context, this, true, "Please Wait...").callSubmitOrderAPI(
            AppController.appPref.userId,
            purchaseData.orderId, "", Constants.API_SUBMIT_ORDER, nonce.toString()
        )
    }


    fun doGoogleSubmitOrder(purchase: Purchase) {
        if (!::purchaseData.isInitialized) {
            Log.e("BILLING", "purchaseData is not initialized")
            return
        }

        Webservices(context, this, true, "Please Wait...")
            .callGoogleSubmitOrderAPI(
                AppController.appPref.userId,
                productId,
                orderID,
                Constants.API_GOOGLE_SUBMIT_ORDER,
                purchase
            )
    }

    override fun onApiSuccess(obj: Any, api: Int) {

        if (api == Constants.API_APPLY_DISCOUNT_CODE) {
            val result = obj as BasketModel
            if (result != null) {
                if (result.status) {
                    AppController.appPref.basketData = Gson().toJson(result)
                    basketModel = result
                    adapter.refreshList(basketModel.response!!.list)
                    setValues()
                    discountCode = basket_edtDiscountCode!!.text.toString()
                    Utility.showMessageDialog(context, result.message)

                    isCouponApply = !isCouponApply;
                    if (isCouponApply) {
                        // basket_edtDiscountCode.isFocusable = false
                        basket_edtDiscountCode!!.isEnabled = false;
                        basket_btnEnter!!.text = "REMOVE"
                    } else {
                        // basket_edtDiscountCode.isFocusable = true
                        basket_edtDiscountCode!!.isEnabled = true;
                        basket_btnEnter!!.text = "ENTER"
                        basket_edtDiscountCode!!.setText("")
                    }
                } else {
                    Utility.showMessageDialog(context, result.message)
                }
            }
        }
        if (api == Constants.API_DELETE_BASKET_ITEM) {
            val result = obj as BasketModel
            if (result != null) {
                if (result.status) {
                    AppController.appPref.basketData = Gson().toJson(result)

                    basketModel = result
                    adapter.refreshList(basketModel.response!!.list)

                    if (basketModel.response!!.list.size == 0) {
                        AppController.appPref.basketData = ""
                    }
                    setValues()
                    Utility.showSnakeBar(layoutParent!!, result.message)
                } else {
                    Utility.showMessageDialog(context, result.message)

                }
            }
            getBasket()
        }
        if (api == Constants.API_PURCHASE) {
            val result = obj as PurchaseDataModel
            if (result.status) {
                if (!result.response!!.orderId.equals("")) {

                    // FIX: initialize purchaseData here
                    purchaseData = result.response!!

                    val songId = basketModel.response!!.list[0].songId
                    val barcode = basketModel.response!!.list[0].barcode
                    productId = songId + "_" + barcode.lowercase();
                    orderID = result.response!!.orderId
                    buySong(this, productId)
                }
            } else {
                Utility.showSnakeBar(layoutParent!!, result.message)
            }
        }
        if (api == Constants.API_GOOGLE_SUBMIT_ORDER) {
            val result = obj as SubmitOrderModel
            if (result.status) {
                AppController.appPref.basketData = ""

                var i = Intent(context, HomeActivity::class.java)
                i.putExtra("orderPlaced", true)
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(i)
            } else {
                Utility.showMessageDialog(context, result.message)
            }
        }
        if (api == Constants.API_GET_BASKET) {
            val result = obj as BasketModel
            if (result != null) {
                if (result!!.status) {
                    AppController!!.appPref!!.basketData = Gson()!!.toJson(result)
                    basketModel = result!!

                } else {
                    AppController!!.appPref!!.basketData = ""
                }

            }
        }
    }

    fun getUserCountry(): String {
        val telephony = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        val sim = telephony.simCountryIso?.uppercase()
        val net = telephony.networkCountryIso?.uppercase()
        val loc = Locale.getDefault().country.uppercase()

        return sim ?: net ?: loc
    }

    override fun onApiFailure(throwable: Throwable, api: Int) {

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                this.finish()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    companion object {
        /**
         * - Set to PayPalConfiguration.ENVIRONMENT_PRODUCTION to move real money.
         * - Set to PayPalConfiguration.ENVIRONMENT_SANDBOX to use your test credentials
         * from https://developer.paypal.com
         * - Set to PayPalConfiguration.ENVIRONMENT_NO_NETWORK to kick the tires
         * without communicating to PayPal's servers.
         */
        // private val CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_PRODUCTION

        // note that these credentials will differ between live & sandbox environments.
        //sandbox
        private val CONFIG_CLIENT_ID =
            "AT6Jwd-fBE_8NSAqw7gY3QqhdgU_mYdpgW5GRvbgwHwpRAFTVY2M--EWTSrHE-yheiO7iBJH4K2qCBme"
        //Production
        //private val CONFIG_CLIENT_ID = "AedLlsTwR39ppIdFlsn91iaa1Ldx_YocGdxMlSY1IOzN7XNFfyxMfglcS_RAiyWqXfcDJPtqD9yiYOF2"

        //sandbox
        private val CONFIG_CLIENT_SECRET =
            "EC5_xgoWPUdYTaSYvKWbnxp9gs1FSW1Wf_LI_nQXJ5bNvXhOG4q4mTv-Iqz8Ik5De-3YgMYmpApaDBH4"
        //Production
        //private val CONFIG_CLIENT_SECRET = "EFrveA52pr6Zze8E8XMAWKl7OsxsPHEEHQF_EOYRw0G1moKyfJBx-7xg-_pumsQtaZQ5wLF-2KWgKHI8"

        private val REQUEST_CODE_PAYMENT = 1

        /*    private val config = PayPalConfiguration()
                    .environment(CONFIG_ENVIRONMENT)
                    .clientId(CONFIG_CLIENT_ID)
                    .merchantName("ChoraLine Merchant")
                    .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
                    .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"))*/
    }

    fun makePayement(purchase: PurchaseDataModel) {

        var dropInRequest = DropInRequest()
        dropInRequest.maskCardNumber = true
        dropInRequest.maskSecurityCode = true
        dropInRequest.threeDSecureRequest = demoThreeDSecureRequest(purchase)
        dropInRequest.venmoRequest = VenmoRequest(VenmoPaymentMethodUsage.SINGLE_USE)
        dropInRequest.allowVaultCardOverride = true
        dropInRequest.vaultCardDefaultValue = true
        dropInRequest.isVaultManagerEnabled = false
        dropInRequest.cardholderNameStatus = CardForm.FIELD_OPTIONAL

        dropInClient!!.launchDropIn(dropInRequest)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        AppLog.debugD(
            TAG,
            "requestCode :: " + requestCode + " resultCode :: " + resultCode + " data :: " + data
        )


        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                var result =
                    data!!.getParcelableExtra<DropInResult>(DropInResult.EXTRA_DROP_IN_RESULT)
                // use the result to update your UI and send the payment method nonce to your server
                var paymentNonce = result!!.getPaymentMethodNonce()!!.string


                doSubmitOrder(paymentNonce)

            } else if (resultCode == Activity.RESULT_CANCELED) {
                // the user canceled
                Utility.displayToast(this, "Subscription Cancelled")
            } else {
                Utility.displayToast(this, "Error to proceed")
                // handle errors here, an exception may be available in
                // val error = data.getSerializableExtra(DropInActivity.EXTRA_ERROR) as Exception
            }
        }

        if (requestCode == REQUEST_CODE_PAYMENT) {
        }/*{
            if (resultCode != RESULT_CANCELED && resultCode == RESULT_OK) {
                val confirm = data.getParcelableExtra<PaymentConfirmation>(PaymentActivity.EXTRA_RESULT_CONFIRMATION) as PaymentConfirmation
                if (confirm != null) {
                    try {
                        AppLog.debugV(TAG, confirm.toJSONObject().toString(4))
                        Log.d(TAG,confirm.toJSONObject().toString())
                        AppLog.debugV(TAG, confirm.payment.toJSONObject().toString(4))

                        var paymentDetails = confirm.toJSONObject().toString(4);
                        AppLog.debugV("paymentExample", paymentDetails);

                            var jsonDetails = JSONObject(paymentDetails)
                            var jsonObject=jsonDetails.getJSONObject("response");
                            var transactionId=jsonObject.getString("id");
                            //makePayment("PAYPAL","",transactionId);
                        doSubmitOrder(transactionId)



                    } catch (e: JSONException) {
                        AppLog.debugE(TAG, "an extremely unlikely failure occurred: "+e)
                    }

                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                AppLog.debugV(TAG, "The user canceled.")
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                AppLog.debugV(TAG, "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.")
            }
        }*/
    }

    /**
     * Called when a [DropInResult] is created without error.
     * @param dropInResult a [DropInResult]
     */
    override fun onDropInSuccess(dropInResult: DropInResult) {
        var paymentNonce = dropInResult.getPaymentMethodNonce()!!.string
//          var paymentNonce = dropInResult.p

//        if (paymentNonce is CardNonce) {
//            var cardNonce = paymentNonce as CardNonce
//        } else if (paymentNonce is PayPalAccountNonce) {
//            var paypalAccountNonce = paymentNonce as PayPalAccountNonce
//        }

        doSubmitOrder(paymentNonce!!)
    }

    private fun demoThreeDSecureRequest(purchase: PurchaseDataModel): ThreeDSecureRequest? {
        val billingAddress = ThreeDSecurePostalAddress()
        billingAddress.givenName = purchase.response!!.email
        billingAddress.surname = purchase.response!!.choir_name
        billingAddress.phoneNumber = purchase.response!!.telephone
        billingAddress.streetAddress = purchase.response!!.email
        billingAddress.extendedAddress = purchase.response!!.email
        billingAddress.locality = purchase.response!!.email
//        billingAddress.region =  purchase.response!!.email
//        billingAddress.postalCode = "12345"
//        billingAddress.countryCodeAlpha2 =  purchase.response!!.email
        val additionalInformation = ThreeDSecureAdditionalInformation()
        additionalInformation.accountId = "" + System.currentTimeMillis()
        val threeDSecureRequest = ThreeDSecureRequest()
        threeDSecureRequest.amount = purchase.response!!.subTotal
        threeDSecureRequest.versionRequested = ThreeDSecureRequest.VERSION_2
        threeDSecureRequest.email = purchase.response!!.email
        threeDSecureRequest.mobilePhoneNumber = purchase.response!!.telephone
        threeDSecureRequest.billingAddress = billingAddress
        threeDSecureRequest.additionalInformation = additionalInformation
        return threeDSecureRequest
    }


    override fun onDropInFailure(error: java.lang.Exception) {
        Utility.displayToast(this, "Error to proceed")
    }


}


