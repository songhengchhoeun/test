package kh.com.mysabay.sdk;

import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Singleton;

import kh.com.mysabay.sdk.callback.UserInfoListener;
import kh.com.mysabay.sdk.pojo.AppItem;
import kh.com.mysabay.sdk.pojo.profile.UserProfileItem;
import kh.com.mysabay.sdk.repository.UserRepo;
import kh.com.mysabay.sdk.utils.AppRxSchedulers;
import kh.com.mysabay.sdk.utils.MessageUtil;
import kh.com.mysabay.sdk.utils.RSAEncryptUtils;
import kh.com.mysabay.sdk.webservice.AbstractDisposableObs;

/**
 * Created by Tan Phirum on 3/11/20
 * Gmail phirumtan@gmail.com
 */
@Singleton
public class MySabaySDK {

    private static Apps apps;
    private static MySabaySDK mySabaySDK;

    @Inject
    public MySabaySDK() {
        Apps.getInstance().mComponent.inject(this);
    }

    public static MySabaySDK getInstance() {
        if (mySabaySDK == null) {
            mySabaySDK = new MySabaySDK();
            apps = Apps.getInstance();
        }
        return mySabaySDK;
    }

    public void showLoginView() {
        //startActivity(new Intent(this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    @Inject
    UserRepo userRepo;
    @Inject
    Gson gson;
    @Inject
    AppRxSchedulers appRxSchedulers;
    @Inject
    RSAEncryptUtils rsaEncryptUtils;

    public void getUserInfo(UserInfoListener listener) {
        AppItem item = gson.fromJson(apps.getAppItem(), AppItem.class);
        userRepo.getUserProfile(item.appSecret, item.token).subscribeOn(appRxSchedulers.io())
                .observeOn(appRxSchedulers.mainThread()).subscribe(new AbstractDisposableObs<UserProfileItem>(apps) {
            @Override
            protected void onSuccess(UserProfileItem userProfileItem) {
                if (listener != null)
                    listener.userInfo(gson.toJson(userProfileItem));
                else MessageUtil.displayToast(apps, "don't see listener handle.");
            }

            @Override
            protected void onErrors(Throwable error) {

            }
        });
    }
}
