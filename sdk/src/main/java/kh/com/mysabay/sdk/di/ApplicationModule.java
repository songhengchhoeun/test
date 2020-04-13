package kh.com.mysabay.sdk.di;

import android.content.Context;

import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import kh.com.mysabay.sdk.MySabaySDK;
import kh.com.mysabay.sdk.di.module.ViewModelModule;
import kh.com.mysabay.sdk.utils.RSAEncryptUtils;

/**
 * Created by Tan Phirum on 2020-01-08
 * Gmail phirumtan@gmail.com
 */

@Module(includes = {ViewModelModule.class})
public class ApplicationModule {

    @Singleton
    @Provides
    RSAEncryptUtils provideRsaEncryptUtils(Context context) {
        return RSAEncryptUtils.getInstance(context);
    }

    @Singleton
    @Provides
    Gson provideGson() {
        return new Gson();
    }

    @Singleton
    @Provides
    Context provideContext() {
        return MySabaySDK.getInstance().mAppContext;
    }

}
