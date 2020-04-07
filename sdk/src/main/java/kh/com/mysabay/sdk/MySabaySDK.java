package kh.com.mysabay.sdk;

import android.content.Intent;

import com.google.gson.Gson;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import kh.com.mysabay.sdk.callback.LoginListener;
import kh.com.mysabay.sdk.callback.PaymentListener;
import kh.com.mysabay.sdk.callback.RefreshTokenListener;
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
    private static SdkConfiguration mSdkConfiguration;

    private LoginListener loginListner;
    private PaymentListener mPaymentListener;

    @Inject
    public MySabaySDK(SdkConfiguration configuration) {
        LogUtil.debug(TAG, "init MySabaySDK");
        EventBus.getDefault().register(this);
        mSdkConfiguration = configuration;
        apps = Apps.getInstance();
        apps.setSdkConfiguration(configuration);
        apps.mComponent.inject(this);
        mySabaySDK = this;
    }

    public static class Impl {
        public static synchronized void setDefaultInstanceConfiguration(SdkConfiguration configuration) {
            new MySabaySDK(configuration);
        }
    }

    @Contract(pure = true)
    public static MySabaySDK getInstance() {
        if (mSdkConfiguration == null)
            throw new RuntimeException("This sdk is need SdkConfiguration");
        return mySabaySDK;
    }

    /**
     * Show the login screen
     *
     * @param listener return token when login success, failed message if login failed
     */
    public void showLoginView(LoginListener listener) {
        if (listener != null)
            this.loginListner = listener;
        apps.startActivity(new Intent(apps, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    /**
     * validate if user login
     *
     * @return true has logged , false otherwise
     */
    public boolean isLogIn() {
        return !StringUtils.isBlank(Apps.getInstance().getAppItem());
    }

    public void logout() {
        apps.saveAppItem("");
    }

    /**
     * Get user profile
     *
     * @param listener
     */
    public void getUserProfile(UserInfoListener listener) {
        AppItem item = gson.fromJson(apps.getAppItem(), AppItem.class);
        userRepo.getUserProfile(item.appSecret, item.token).subscribeOn(appRxSchedulers.io())
                .observeOn(appRxSchedulers.mainThread()).subscribe(new AbstractDisposableObs<UserProfileItem>(apps) {
            @Override
            protected void onSuccess(UserProfileItem userProfileItem) {
                if (listener != null) {
                    if (userProfileItem.status == 200) {
                        item.withToken(userProfileItem.data.refreshToken);
                        item.withExpired(userProfileItem.data.expire);
                        Apps.getInstance().saveAppItem(gson.toJson(item));
                        listener.userInfo(gson.toJson(userProfileItem));
                    } else
                        onErrors(new Error(gson.toJson(userProfileItem)));
                } else {
                    onErrors(new NullPointerException("UserInfoListener required!!!"));
                }
            }

            @Override
            protected void onErrors(@NotNull Throwable error) {
                LogUtil.error(TAG, error.getLocalizedMessage());
            }
        });
    }

    /**
     * Show the shop item
     *
     * @param listener return with item purchase transaction or failed message
     */
    public void showStoreView(PaymentListener listener) {
        if (listener == null) return;

        this.mPaymentListener = listener;
        AppItem appItem = gson.fromJson(Apps.getInstance().getAppItem(), AppItem.class);
        if (appItem == null || StringUtils.isBlank(appItem.token)) {
            MessageUtil.displayToast(apps, "You need to login first");
            return;
        }
        apps.startActivity(new Intent(apps, StoreActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    /**
     * @param listener
     */
    public void refreshToken(RefreshTokenListener listener) {
        AppItem item = gson.fromJson(apps.getAppItem(), AppItem.class);
        userRepo.getUserProfile(item.appSecret, item.token).subscribeOn(appRxSchedulers.io())
                .observeOn(appRxSchedulers.mainThread()).subscribe(new AbstractDisposableObs<UserProfileItem>(apps) {
            @Override
            protected void onSuccess(UserProfileItem userProfileItem) {
                if (listener != null) {
                    if (userProfileItem.status == 200) {
                        item.withToken(userProfileItem.data.refreshToken);
                        item.withExpired(userProfileItem.data.expire);
                        Apps.getInstance().saveAppItem(gson.toJson(item));
                        listener.refreshSuccess(userProfileItem.data.refreshToken);
                    } else
                        onErrors(new Error(gson.toJson(userProfileItem)));
                } else {
                    onErrors(new NullPointerException("RefreshTokenListener required!!!"));
                }
            }

            @Override
            protected void onErrors(@NotNull Throwable error) {
                LogUtil.error(TAG, error.getLocalizedMessage());
                if (listener != null) listener.refreshFailed(error);
            }
        });
    }

    /**
     * @return with token that valid
     */
    public String currentToken() {
        AppItem item = gson.fromJson(apps.getAppItem(), AppItem.class);
        return item.token;
    }

    /**
     * @return true if token is valid, false otherwise
     */
    public boolean isTokenValid() {
        AppItem item = gson.fromJson(apps.getAppItem(), AppItem.class);
        if (System.currentTimeMillis() == item.expire)
            return false;
        else return true;
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
            if (event.dataIAP != null)
                mPaymentListener.purchaseIAPSuccess(event.dataIAP);
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
