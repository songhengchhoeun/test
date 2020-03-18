package kh.com.mysabay.sdk.webservice.api;

import io.reactivex.Observable;
import kh.com.mysabay.sdk.pojo.googleVerify.GoogleVerifyBody;
import kh.com.mysabay.sdk.pojo.googleVerify.GoogleVerifyResponse;
import kh.com.mysabay.sdk.pojo.mysabay.MySabayItem;
import kh.com.mysabay.sdk.pojo.payment.PaymentBody;
import kh.com.mysabay.sdk.pojo.shop.ShopItem;
import kh.com.mysabay.sdk.pojo.thirdParty.ThirdPartyItem;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Tan Phirum on 3/13/20
 * Gmail phirumtan@gmail.com
 */
public interface StoreApi {

    @GET("api/v1/store")
    Observable<ShopItem> getShopItem(@Header("app_secret") String appSecret, @Header("Authorization") String token);

    @GET("api/v1/checkout/{uuid}")
    Observable<MySabayItem> getMySabayCheckout(@Header("app_secret") String appSecret, @Header("Authorization") String token, @Path("uuid") String uuid);

    @GET("api/v1/cashier/{uuid}")
    Observable<ThirdPartyItem> get3PartyCheckout(@Header("app_secret") String appSecret, @Header("Authorization") String token, @Path("uuid") String uuid);

    @FormUrlEncoded
    @POST("api/v1/verify_receipt/google")
    Observable<GoogleVerifyResponse> postToVerifyGoogle(@Header("app_secret") String appSecret, @Header("Authorization") String token,
                                                        @Body() GoogleVerifyBody body);

    @FormUrlEncoded
    @POST("api/v1/charge/auth")
    Observable<Object> postToPaid(@Header("app_secret") String appSecret, @Header("Authorization") String token,
                                  @Body() PaymentBody body);

}
