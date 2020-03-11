package kh.com.mysabay.sdk;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import com.akexorcist.localizationactivity.core.LocalizationApplicationDelegate;
import com.google.gson.Gson;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import kh.com.mysabay.sdk.callback.UserInfoListener;
import kh.com.mysabay.sdk.di.BaseAppComponent;
import kh.com.mysabay.sdk.di.DaggerBaseAppComponent;
import kh.com.mysabay.sdk.pojo.AppItem;
import kh.com.mysabay.sdk.pojo.profile.UserProfileItem;
import kh.com.mysabay.sdk.repository.UserRepo;
import kh.com.mysabay.sdk.ui.activity.LoginActivity;
import kh.com.mysabay.sdk.utils.AppRxSchedulers;
import kh.com.mysabay.sdk.utils.MessageUtil;
import kh.com.mysabay.sdk.webservice.AbstractDisposableObs;

/**
 * Created by Tan Phirum on 3/7/20
 * Gmail phirumtan@gmail.com
 */
public class Apps extends Application {

    LocalizationApplicationDelegate mLocalizationApplicationDelegate = new LocalizationApplicationDelegate(this);

    private static Apps mInstance;
    private SharedPreferences mPreferences;
    public BaseAppComponent mComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        this.mComponent = DaggerBaseAppComponent.create();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(mLocalizationApplicationDelegate.attachBaseContext(base));
    }

    @Override
    public void onConfigurationChanged(@NotNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mLocalizationApplicationDelegate.onConfigurationChanged(this);
    }

    @Override
    public Context getApplicationContext() {
        return mLocalizationApplicationDelegate.getApplicationContext(super.getApplicationContext());
    }

    @Contract(pure = true)
    public static synchronized Apps getInstance() {
        return mInstance;
    }

    public SharedPreferences getPreferences(Activity context) {
        if (mPreferences == null)
            mPreferences = context.getSharedPreferences(Globals.PREF_NAME, MODE_PRIVATE);
        return mPreferences;
    }

    public SharedPreferences getPreferences() {
        if (mPreferences == null)
            mPreferences = mInstance.getSharedPreferences(Globals.PREF_NAME, MODE_PRIVATE);
        return mPreferences;
    }

    public void saveAppItem(String item) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString(Globals.EXT_KEY_APP_ITEM, item);
        editor.apply();
    }

    public String getAppItem() {
        return getPreferences().getString(Globals.EXT_KEY_APP_ITEM, null);
    }

    public void showLoginView() {
        startActivity(new Intent(this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public void logout() {
        saveAppItem("");
    }

    @Inject
    UserRepo userRepo;
    @Inject
    Gson gson;
    @Inject
    AppRxSchedulers appRxSchedulers;

    public void getUserInfo(UserInfoListener listener) {
        AppItem item = gson.fromJson(getAppItem(), AppItem.class);
        userRepo.getUserProfile(item.appSecret, item.token).subscribeOn(appRxSchedulers.io())
                .observeOn(appRxSchedulers.mainThread()).subscribe(new AbstractDisposableObs<UserProfileItem>(this) {
            @Override
            protected void onSuccess(UserProfileItem userProfileItem) {
                if (listener != null)
                    listener.userInfo(gson.toJson(userProfileItem));
                else MessageUtil.displayToast(getInstance(), "don't see listener handle.");
            }

            @Override
            protected void onErrors(Throwable error) {

            }
        });
    }

}
