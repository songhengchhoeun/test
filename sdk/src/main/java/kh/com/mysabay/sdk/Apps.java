package kh.com.mysabay.sdk;

import android.app.Application;
import android.content.Context;
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
}
