package kh.com.mysabay.sdk;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import com.akexorcist.localizationactivity.core.LocalizationApplicationDelegate;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import kh.com.mysabay.sdk.di.BaseAppComponent;
import kh.com.mysabay.sdk.di.DaggerBaseAppComponent;
import kh.com.mysabay.sdk.utils.LogUtil;

/**
 * Created by Tan Phirum on 3/7/20
 * Gmail phirumtan@gmail.com
 */
public class Apps extends Application {

    private static final String TAG = "Apps";

    LocalizationApplicationDelegate mLocalizationApplicationDelegate = new LocalizationApplicationDelegate();

    private static Apps mInstance;
    private SharedPreferences mPreferences;
    public BaseAppComponent mComponent;
    private SdkConfiguration mSdkConfiguration;

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.debug(TAG, "init Apps");
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

    public void saveMethodSelected(String item) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString(Globals.EXT_KEY_PAYMENT_METHOD, item);
        editor.apply();
    }

    public String getMethodSelected() {
        return getPreferences().getString(Globals.EXT_KEY_PAYMENT_METHOD, "");
    }

    public SdkConfiguration getSdkConfiguration() {
        return mSdkConfiguration;
    }

    public void setSdkConfiguration(SdkConfiguration mSdkConfiguration) {
        this.mSdkConfiguration = mSdkConfiguration;
    }

    public String userApiUrl() {
        return mSdkConfiguration.isSandBox ? "https://user.testing.mysabay.com/" : "https://user.mysabay.com/";
    }

    public String storeApiUrl() {
        return mSdkConfiguration.isSandBox ? "https://store.testing.mysabay.com/" : "https://store.mysabay.com/";
    }
}
