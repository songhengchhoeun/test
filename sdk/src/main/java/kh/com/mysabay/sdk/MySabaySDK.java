package kh.com.mysabay.sdk;

import android.content.Intent;

import com.google.gson.Gson;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;
import javax.inject.Singleton;

import kh.com.mysabay.sdk.callback.LoginListener;
import kh.com.mysabay.sdk.callback.PaymentListener;
import kh.com.mysabay.sdk.callback.UserInfoListener;
import kh.com.mysabay.sdk.pojo.AppItem;
import kh.com.mysabay.sdk.pojo.login.SubscribeLogin;
import kh.com.mysabay.sdk.pojo.payment.SubscribePayment;
import kh.com.mysabay.sdk.pojo.profile.UserProfileItem;
import kh.com.mysabay.sdk.repository.UserRepo;
import kh.com.mysabay.sdk.ui.activity.LoginActivity;
import kh.com.mysabay.sdk.ui.activity.StoreActivity;
import kh.com.mysabay.sdk.utils.AppRxSchedulers;
import kh.com.mysabay.sdk.utils.LogUtil;
import kh.com.mysabay.sdk.utils.MessageUtil;
import kh.com.mysabay.sdk.webservice.AbstractDisposableObs;

/**
 * Created by Tan Phirum on 3/11/20
 * Gmail phirumtan@gmail.com
 */
@Singleton
public class MySabaySDK {

    private static final String TAG = MySabaySDK.class.getSimpleName();

    @Inject
    UserRepo userRepo;
    @Inject
    Gson gson;
    @Inject
    AppRxSchedulers appRxSchedulers;

    private static Apps apps;
    private static MySabaySDK mySabaySDK;
    private LoginListener loginListner;
    private PaymentListener mPaymentListener;

    @Inject
    public MySabaySDK() {
        LogUtil.debug(TAG, "init MySabaySDK");
        EventBus.getDefault().register(this);
        apps = Apps.getInstance();
        apps.mComponent.inject(this);
    }

    public static MySabaySDK getInstance() {
        if (mySabaySDK == null)
            mySabaySDK = new MySabaySDK();
        return mySabaySDK;
    }

    public void showLoginView(LoginListener listener) {
        if (listener != null)
            this.loginListner = listener;
        apps.startActivity(new Intent(apps, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public void logout() {
        apps.saveAppItem("");
    }

    public void getUserInfo(UserInfoListener listener) {
        AppItem item = gson.fromJson(apps.getAppItem(), AppItem.class);
        userRepo.getUserProfile(item.appSecret, item.token).subscribeOn(appRxSchedulers.io())
                .observeOn(appRxSchedulers.mainThread()).subscribe(new AbstractDisposableObs<UserProfileItem>(apps) {
            @Override
            protected void onSuccess(UserProfileItem userProfileItem) {
                if (listener != null)
                    listener.userInfo(gson.toJson(userProfileItem));
                else onErrors(new NullPointerException("UserInfoListener required!!!"));
            }

            @Override
            protected void onErrors(Throwable error) {
                LogUtil.error(TAG, error.getLocalizedMessage());
            }
        });
    }

    public void showShopView(PaymentListener listener) {
        if (listener == null) return;

        this.mPaymentListener = listener;
        AppItem appItem = gson.fromJson(Apps.getInstance().getAppItem(), AppItem.class);
        if (appItem == null || StringUtils.isBlank(appItem.token)) {
            MessageUtil.displayToast(apps, "You need to login first");
            return;
        }
        apps.startActivity(new Intent(apps, StoreActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    @Subscribe
    public void onLoginEvent(SubscribeLogin event) {
        if (loginListner != null) {
            if (!StringUtils.isBlank(event.accessToken)) {
                loginListner.loginSuccess(event.accessToken);
            } else
                loginListner.loginFailed(event.error);
        } else {
            LogUtil.debug(TAG, "loginListerner null " + gson.toJson(event));
        }
    }

    @Subscribe
    public void onPaymentEvent(SubscribePayment event) {
        if (mPaymentListener != null) {
            if (event.dataAIP != null)
                mPaymentListener.purchaseAIPSuccess(event.dataAIP);
            else if (event.dataMySabay != null)
                mPaymentListener.purchaseMySabaySuccess(event.dataMySabay);
            else
                mPaymentListener.purchaseFailed(event.dataError);
        } else
            LogUtil.debug(TAG, "loginListerner null " + gson.toJson(event));
    }

    public void destroy() {
        EventBus.getDefault().unregister(this);
        loginListner = null;
        mPaymentListener = null;
        mySabaySDK = null;
        apps = null;
    }
}
