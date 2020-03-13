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
}
