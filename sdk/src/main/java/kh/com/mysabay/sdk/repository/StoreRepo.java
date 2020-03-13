package kh.com.mysabay.sdk.repository;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import kh.com.mysabay.sdk.pojo.googleVerify.GoogleVerifyBody;
import kh.com.mysabay.sdk.pojo.mysabay.MySabayItem;
import kh.com.mysabay.sdk.pojo.payment.PaymentBody;
import kh.com.mysabay.sdk.pojo.shop.ShopItem;
import kh.com.mysabay.sdk.pojo.thirdParty.ThirdPartyItem;
import kh.com.mysabay.sdk.webservice.api.StoreApi;

/**
 * Created by Tan Phirum on 3/13/20
 * Gmail phirumtan@gmail.com
 */
@Singleton
public class StoreRepo implements StoreApi {

    private final StoreApi storeApi;

    @Inject
    public StoreRepo(StoreApi storeApi) {
        this.storeApi = storeApi;
    }

    @Override
    public Observable<ShopItem> getShopItem(String appSecret, String token) {
        return storeApi.getShopItem(appSecret, "Bearer " + token);
    }

    @Override
    public Observable<MySabayItem> getMySabayCheckout(String appSecret, String token, String uuid) {
        return null;
    }

    @Override
    public Observable<ThirdPartyItem> get3PartyCheckout(String appSecret, String token, String uuid) {
        return null;
    }

    @Override
    public Observable<Object> postToVerifyGoogle(String appSecret, String token, GoogleVerifyBody body) {
        return null;
    }

    @Override
    public Observable<Object> postToPaid(String appSecret, String token, PaymentBody body) {
        return null;
    }
}
