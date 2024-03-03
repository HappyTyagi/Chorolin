package com.choraline

//import com.braintreepayments.api.dropin.DropInRequest
//import com.braintreepayments.api.dropin.DropInResult

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.braintreepayments.api.*
import com.braintreepayments.cardform.view.CardForm
import com.choraline.adapters.BasketListAdapter
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
import kotlinx.android.synthetic.main.activity_basket.*


class BasketActivity : BaseActivity(), View.OnClickListener, APIListener, DropInListener {

    private var dropInClient: DropInClient? = null
    val TAG: String = BasketActivity::class.simpleName!!.toString()
    private lateinit var context: Context
    var toolbar: Toolbar? = null
    lateinit var basketModel: BasketModel
    lateinit var adapter: BasketListAdapter
    lateinit var purchaseData: PurchaseData
    private var discountCode: String = ""
    private var isCouponApply = false;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basket)
        context = this@BasketActivity
        val toolbar = findViewById<Toolbar>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        if (intent != null) {
            basketModel = Gson().fromJson(intent.getStringExtra("data"), BasketModel::class.java);
        }
//        mApiInterface = APIClient.getClient()!!.create(APIInterface::class.java)
//        callTokenAPI()


//            Utility.setPayPopup(this, "1")
//        }


        initUI()


//        PopupDialog.getInstance(this)
//            .setStyle(Styles.ALERT)
//            .setHeading("Information!")
//            .setDescription(
//                "PayPal payment is temporarily unavailable. Please pay with a debit/credit card until resolved"
//            )
//            .setCancelable(false)
//            .setDismissButtonText("OK")
//            .setDismissButtonTextColor(R.color.white)
//            .setDismissButtonTextColor(R.color.white)
//            .setDismissButtonTextColor(R.color.white)
//            .setDismissButtonBackground(R.color.colorPrimary)
//            .showDialog(object : OnDialogButtonClickListener() {
//                override fun onDismissClicked(dialog: Dialog?) {
//                    super.onDismissClicked(dialog)
//                }
//            })


        dropInClient = DropInClient(this, DemoClientTokenProvider(this))
        dropInClient!!.setListener(this@BasketActivity);

    }


    fun initUI() {
        if (basketModel != null) {
            adapter = BasketListAdapter(
                context,
                this,
                basketModel!!.response!!.list,
                basketModel!!.response!!.currency_symbol
            )
            val mLayoutManager = LinearLayoutManager(context)
            mLayoutManager!!.orientation = LinearLayout.VERTICAL
            basket_recyclerBasket!!.layoutManager = mLayoutManager
            basket_recyclerBasket!!.itemAnimator = DefaultItemAnimator()
            basket_recyclerBasket.setAdapter(adapter)

            val loginString = SpannableString(getString(R.string.text_continue_shopping))
            loginString.setSpan(
                StyleSpan(Typeface.NORMAL),
                0,
                loginString!!.length,
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
            basket_txtContinueShopping.setText(loginString)
            basket_txtContinueShopping.setMovementMethod(LinkMovementMethod.getInstance())

            setValues()

            tootlbar_imgbtnShare.setOnClickListener(this)
            basket_btnEnter.setOnClickListener(this)
            basket_btnPurchase.setOnClickListener(this)
        }
    }

    fun setValues() {
        if (basketModel!!.response!!.list.size == 0) {
            basket_layoutDiscout!!.visibility = View.GONE
            basket_txtTotalPrice!!.visibility = View.INVISIBLE
            basket_btnPurchase!!.visibility = View.GONE
        } else {
            basket_layoutDiscout!!.visibility = View.GONE
            basket_txtTotalPrice!!.visibility = View.VISIBLE
            basket_btnPurchase!!.visibility = View.VISIBLE
        }

        if (Build.VERSION.SDK_INT >= 24) {
            basket_txtTotalPrice.text = Html.fromHtml(
                basketModel!!.response!!.currency_symbol + " " + basketModel!!.response!!.subtotal,
                0
            )
        } else {
            basket_txtTotalPrice.text =
                Html.fromHtml(basketModel!!.response!!.currency_symbol + " " + basketModel!!.response!!.subtotal)
        }
    }

    override fun onClick(v: View?) {
        Utility!!.hideSoftKeyboard(this)
        if (v == tootlbar_imgbtnShare) {
            Utility!!.shareApp(context)
        } else if (v == basket_btnEnter) {
            if (basket_edtDiscountCode!!.text.toString().equals("")) {
                Utility!!.showSnakeBar(layoutParent, "Please Enter the Discount Code.")
            } else {
                if (isCouponApply) {
                    removeDiscountCoupon()
                } else
                    applyDiscountCode()
            }
        } else if (v == basket_btnPurchase) {
            doPurchase()
        }

    }

    fun deleteBasketItem(pos: Int) {
        Webservices(context, this, true, "Please Wait...").callDeleteBasketItemAPI(
            basketModel!!.response!!.list[pos]!!.id,
            basketModel!!.response!!.currency_id,
            AppController!!.appPref!!.userId,
            Constants.API_DELETE_BASKET_ITEM
        )
    }

    fun applyDiscountCode() {
        Webservices(context, this, true, "Please Wait...").callApplyDiscountcodeAPI(
            basket_edtDiscountCode!!.text.toString(),
            basketModel!!.response!!.currency_id,
            AppController!!.appPref!!.userId,
            Constants.API_APPLY_DISCOUNT_CODE
        )
    }


    fun removeDiscountCoupon() {
        Webservices(context, this, true, "Please Wait...").callRemoveCoupon(
            basketModel!!.response!!.currency_id,
            AppController!!.appPref!!.userId,
            Constants.API_APPLY_DISCOUNT_CODE
        )

    }


    fun doPurchase() {
        Webservices(context, this, true, "Please Wait...").callPurchaseAPI(
            AppController!!.appPref!!.userId,
            discountCode, basketModel!!.response!!.subtotal,
            basketModel!!.response!!.currency_id, Constants.API_PURCHASE
        )


    }

    fun doSubmitOrder(nonce: String) {
        Webservices(context, this, true, "Please Wait...").callSubmitOrderAPI(
            AppController!!.appPref!!.userId,
            purchaseData!!.orderId, "", Constants.API_SUBMIT_ORDER, nonce.toString()
        )
    }

    override fun onApiSuccess(obj: Any, api: Int) {

        if (api == Constants.API_APPLY_DISCOUNT_CODE) {
            val result = obj as BasketModel
            if (result != null) {
                if (result!!.status) {
                    AppController!!.appPref!!.basketData = Gson()!!.toJson(result)
                    basketModel = result
                    adapter!!.refreshList(basketModel!!.response!!.list)
                    setValues()
                    discountCode = basket_edtDiscountCode.text.toString()
                    Utility!!.showMessageDialog(context, result.message)

                    isCouponApply = !isCouponApply;
                    if (isCouponApply) {
                        // basket_edtDiscountCode.isFocusable = false
                        basket_edtDiscountCode.isEnabled = false;
                        basket_btnEnter.text = "REMOVE"
                    } else {
                        // basket_edtDiscountCode.isFocusable = true
                        basket_edtDiscountCode.isEnabled = true;
                        basket_btnEnter.text = "ENTER"
                        basket_edtDiscountCode.setText("")
                    }
                } else {
                    Utility!!.showMessageDialog(context, result!!.message)
                }
            }
        }
        if (api == Constants!!.API_DELETE_BASKET_ITEM) {
            val result = obj as BasketModel
            if (result != null) {
                if (result!!.status) {
                    AppController!!.appPref!!.basketData = Gson()!!.toJson(result)

                    basketModel = result
                    adapter!!.refreshList(basketModel!!.response!!.list)

                    if (basketModel!!.response!!.list.size == 0) {
                        AppController!!.appPref!!.basketData = ""
                    }
                    setValues()
                    Utility!!.showSnakeBar(layoutParent, result!!.message)
                } else {
                    Utility!!.showMessageDialog(context, result!!.message)

                }
            }
        }
        if (api == Constants!!.API_PURCHASE) {
            val result = obj as PurchaseDataModel
            if (result!!.status) {
                if (!result!!.response!!.orderId.equals("")) {
                    purchaseData = result!!.response as PurchaseData
                    makePayement(result)
                }
            } else {
                Utility!!.showSnakeBar(layoutParent, result!!.message)
            }
        }
        if (api == Constants!!.API_SUBMIT_ORDER) {
            val result = obj as SubmitOrderModel
            if (result!!.status) {
                AppController!!.appPref!!.basketData = ""

                var i = Intent(context, HomeActivity::class.java)
                i.putExtra("orderPlaced", true)
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(i)
            } else {
                Utility!!.showMessageDialog(context, result!!.message)
            }
        }

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

        /*  val thingToBuy = PayPalPayment(BigDecimal(purchase!!.subTotal), purchase!!.currency_code, "ChoraLine",
                  PayPalPayment.PAYMENT_INTENT_SALE)

          val intent = Intent(this@BasketActivity, PaymentActivity::class.java)
          // send the same configuration for restart resiliency
          intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config)
          intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy)
          startActivityForResult(intent, REQUEST_CODE_PAYMENT)*/
//        var dropInClient = DropInClient(this, purchase.response!!.braintreeToken)
//        var dropInRequest = DropInRequest()
//        dropInClient.launchDropIn(dropInRequest)


//        dropInClient = DropInClient(this,  DemoClientTokenProvider(this))

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


//        var dropInRequest = DropInRequest().clientToken(purchase.response!!.braintreeToken);
//        startActivityForResult(dropInRequest.getIntent(this), REQUEST_CODE_PAYMENT);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        AppLog!!.debugD(
            TAG,
            "requestCode :: " + requestCode + " resultCode :: " + resultCode + " data :: " + data
        )


        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                var result =
                    data!!.getParcelableExtra<DropInResult>(DropInResult.EXTRA_DROP_IN_RESULT)
                // use the result to update your UI and send the payment method nonce to your server
                var paymentNonce = result!!.getPaymentMethodNonce()!!.string


                doSubmitOrder(paymentNonce!!)

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
                val confirm = data!!.getParcelableExtra<PaymentConfirmation>(PaymentActivity.EXTRA_RESULT_CONFIRMATION) as PaymentConfirmation
                if (confirm != null) {
                    try {
                        AppLog.debugV(TAG, confirm.toJSONObject().toString(4))
                        Log.d(TAG,confirm.toJSONObject().toString())
                        AppLog.debugV(TAG, confirm.payment.toJSONObject().toString(4))

                        var paymentDetails = confirm.toJSONObject().toString(4);
                        AppLog.debugV("paymentExample", paymentDetails);

                            var jsonDetails = JSONObject(paymentDetails)
                            var jsonObject=jsonDetails!!.getJSONObject("response");
                            var transactionId=jsonObject!!.getString("id");
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
          var paymentNonce = dropInResult!!.getPaymentMethodNonce()!!.string
//          var paymentNonce = dropInResult!!.p

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
        billingAddress.streetAddress =  purchase.response!!.email
        billingAddress.extendedAddress = purchase.response!!.email
        billingAddress.locality = purchase.response!!.email
//        billingAddress.region =  purchase.response!!.email
//        billingAddress.postalCode = "12345"
//        billingAddress.countryCodeAlpha2 =  purchase.response!!.email
        val additionalInformation = ThreeDSecureAdditionalInformation()
        additionalInformation.accountId = ""+System.currentTimeMillis()
        val threeDSecureRequest = ThreeDSecureRequest()
        threeDSecureRequest.amount = purchase.response!!.subTotal
        threeDSecureRequest.versionRequested = ThreeDSecureRequest.VERSION_2
        threeDSecureRequest.email = purchase.response!!.email
        threeDSecureRequest.mobilePhoneNumber = purchase.response!!.telephone
        threeDSecureRequest.billingAddress = billingAddress
        threeDSecureRequest.additionalInformation = additionalInformation
        return threeDSecureRequest
    }

    /**
     * Called when DropIn has finished with an error.
     * @param error explains reason for DropIn failure.
     */
    override fun onDropInFailure(error: java.lang.Exception) {
        Utility.displayToast(this, "Error to proceed")
    }


//    fun callTokenAPI() {
//        if (Utility!!.isNetworkAvailable(this)) {
//            var call = mApiInterface!!.getToken(AppController.appPref.accessToken)
//            call.enqueue(object : Callback<GetTokeModel> {
//                override fun onResponse(
//                    call: Call<GetTokeModel>,
//                    response: Response<GetTokeModel>
//                ) {
//                    val mObject = response.body()
//                    if (mObject != null) {
//                        Utility.displayToast(this@BasketActivity, mObject.response!!.braintreeToken)
//                        paypayToken = mObject.response!!.braintreeToken
//                    }
//                }
//
//                override fun onFailure(call: Call<GetTokeModel>, t: Throwable) {
//                    Utility.displayToast(this@BasketActivity, "mObject.response!!.braintreeToken")
//                }
//            })
//
//        } else {
//
//        }
//    }


}


