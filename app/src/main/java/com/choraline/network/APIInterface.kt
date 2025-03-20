package com.choraline.network

import com.choraline.models.*
import com.choraline.utils.Constants
import com.example.example.ForceUpdateResponse
import retrofit2.Call
import okhttp3.ResponseBody
import retrofit2.http.*


/**
 * Created by deepak Tyagi on 7/10/2017.
 */
interface APIInterface {

    @POST(Constants.GET_FREE_TRAIL_URL)
    fun doGetFreeTrail(): Call<FreeTrailModel>

    @FormUrlEncoded
    @POST(Constants.GET_FREE_TRAIL_MUSIC_LIST_URL)
    fun doGetMusicList(@Field("voiceType") voiceType: String): Call<SongsModel>

    @FormUrlEncoded
    @POST(Constants.LOGIN_URL)
    fun doLogin(@Field("EmailId") email: String,
                @Field("password") password: String,
                @Field("gcmToken") gcmToken: String,
                @Field("deviceType") deviceType: String): Call<LoginModel>

    @FormUrlEncoded
    @POST(Constants.FORGOT_PASSWORD_URL)
    fun doForgotPassword(@Field("EmailId") email: String): Call<GenericResponseModel>

    @FormUrlEncoded
    @POST(Constants.SIGNUP_URL)
    fun doSignup(@Field("firstname") firstname: String,
                 @Field("lastname") lastname: String,
                 @Field("EmailId") EmailId: String,
                 @Field("conf_EmailId") conf_EmailId: String,
                 @Field("password") password: String,
                 @Field("conf_password") conf_password: String,
                 @Field("choir_name") choir_name: String,
                 @Field("gcmToken") gcmToken: String,
                 @Field("deviceType") deviceType: String): Call<LoginModel>

    @FormUrlEncoded
    @POST(Constants.GET_PROFILE_URL)
    fun doGetProfile(@Field("userId") userId: String,
                     @Field("gcmToken") gcmToken: String,
                     @Field("deviceType") deviceType: String,
                     @Field("access_token") accessToken: String
                     ): Call<LoginModel>

    @FormUrlEncoded
    @POST(Constants.UPDATE_PROFILE_URL)
    fun doUpdateProfile(@Field("userId") userId: String,
                        @Field("firstname") firstname: String,
                        @Field("lastname") lastname: String,
                        @Field("EmailId") EmailId: String,

                        @Field("conf_EmailId") conf_EmailId: String,

                        @Field("oldpassword") oldpassword: String,
                        @Field("password") password1: String,
                        @Field("password2") password2: String,
                        @Field("address1") address1: String,
                        @Field("address2") address2: String,
                        @Field("town") town: String,
                        @Field("postcode") postcode: String,
                        @Field("country") country: String,
                        @Field("telephone") telephone: String,
                        @Field("optin") optin: String,
                        @Field("choir_name") choir_name: String,
                        @Field("access_token") accessToken: String
                        ): Call<GenericResponseModel>


    @Streaming
    @GET
    fun downloadSong(@Url fileUrl: String): Call<ResponseBody>


    @GET
    fun appVersion(@Url fileUrl: String): Call<ForceUpdateResponse>

    @FormUrlEncoded
    @POST(Constants.COMPOSER_LIST_URL)
    fun getComposerList(@Field("lastDate") composerName: String): Call<ComposerModel>

    @FormUrlEncoded
    @POST(Constants.CHORAL_WORKS_URL)
    fun getChoralWorks(@Field("composerName") composerName: String,
                       @Field("lastDate") lastDate: String): Call<ChoralWorksModel>

    @FormUrlEncoded
    @POST(Constants.SONG_DETAIL_UEL)
    fun getSongDetail(@Field("albumId") songId: String): Call<SongDetailModel>

    @FormUrlEncoded
    @POST(Constants.ADD_TO_BASKET_URL)
    fun addToBasket(@Field("songId") songId: String,
                    @Field("currencyId") currencyId: String,
                    @Field("userId") userId: String,
                    @Field("access_token") accessToken: String
                    ): Call<BasketModel>



    @FormUrlEncoded
    @POST(Constants.FREE_GIFT)
    fun freeGift(@Field("songId") songId: String,
                    @Field("currencyId") currencyId: String,
                    @Field("userId") userId: String,
                    @Field("discountCode") discountCode: String,
                    @Field("subTotal") subTotal: String,
                    @Field("access_token") accessToken: String
    ): Call<BaseModel>



    @FormUrlEncoded
    @POST(Constants.CHECK_FREE_GIFT_STATUS)
    fun checkfreeGift(
                 @Field("userId") userId: String,
                 @Field("songId") songId: String,
                 @Field("currencyId") currencyId: String,
                 @Field("access_token") accessToken: String
    ): Call<BaseModel>

    @FormUrlEncoded
    @POST(Constants.DELETE_BASKET_ITEM_URL)
    fun deleteFromBasket(@Field("id") id: String,
                         @Field("currencyId") currencyId: String,
                         @Field("userId") userId: String,
                         @Field("access_token") accessToken: String
                         ): Call<BasketModel>

    @FormUrlEncoded
    @POST(Constants.APPLY_DISCOUNT_CODE_URL)
    fun applyDiscountCode(@Field("discountCode") discountCode: String,
                          @Field("currencyId") currencyId: String,
                          @Field("userId") userId: String,
                          @Field("access_token") accessToken: String
                          ): Call<BasketModel>

    @FormUrlEncoded
    @POST(Constants.REMOVE_DISCOUNT_CODE_URL)
    fun removeDiscountCode(@Field("currencyId") currencyId: String,
                          @Field("userId") userId: String,
                           @Field("access_token") accessToken: String
                           ): Call<BasketModel>



    @FormUrlEncoded
    @POST(Constants.GET_BASKET_URL)
    fun getBasket(@Field("userId") userId: String,
                  @Field("access_token") accessToken: String
                  ): Call<BasketModel>

    @FormUrlEncoded
    @POST(Constants.PURCHASE_URL)
    fun purchase(@Field("userId") userId: String,
                 @Field("discountCode") discountCode: String,
                 @Field("subTotal") subTotal: String,
                 @Field("currencyId") currencyId: String,
                 @Field("access_token") accessToken: String
                 ): Call<PurchaseDataModel>

    @FormUrlEncoded
    @POST(Constants.GET_TOKEN_URL)
    fun getToken(@Field("access_token") accessToken: String,
                 ): Call<GetTokeModel>

    @FormUrlEncoded
    @POST(Constants.SUBMIT_ORDER_URL)
    fun submitOrder(@Field("userId") userId: String,
                    @Field("orderId") orderId: String,
                    @Field("transId") transId: String,
                    @Field("access_token") accessToken: String,
                    @Field("payment_method_nonce") nonce: String
                    ): Call<SubmitOrderModel>

    @FormUrlEncoded
    @POST(Constants.ORDER_HISTORY_URL)
    fun getOrderHistory(@Field("userId") userId: String,
                        @Field("lastDate") lastDate: String,
                        @Field("access_token") accessToken: String
                        ): Call<OrderHistoryModel>

    @FormUrlEncoded
    @POST(Constants.PURCHASED_MUSIC_URL)
    fun getPurchasedMusic(@Field("userId") userId: String,
                          @Field("lastDate") lastDate: String,
                          @Field("access_token") accessToken: String
                          ): Call<PurchasedMusicModel>

    @FormUrlEncoded
    @POST(Constants.CONTACT_US_URL)
    fun doContactUs(@Field("name") name: String,
                    @Field("email") email: String,
                    @Field("contact_number") contact_number: String,
                    @Field("message") message: String): Call<GenericResponseModel>

    @GET(Constants.GET_COUNTRY_LIST_URL)
    fun getCountryList(): Call<CountryModel>

}