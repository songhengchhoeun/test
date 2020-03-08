package kh.com.mysabay.sdk.repository;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import kh.com.mysabay.sdk.webservice.api.UserApi;

/**
 * Created by Tan Phirum on 3/8/20
 * Gmail phirumtan@gmail.com
 */
@Singleton
public class UserRepo implements UserApi {

    private final UserApi userApi;

    @Inject
    public UserRepo(UserApi userApi) {
        this.userApi = userApi;
    }

    @Override
    public Observable<Object> getUserLogin(String appSecret, String phone) {
        return userApi.getUserLogin(appSecret, phone);
    }

    @Override
    public Observable<Object> postVerifyCode(String appSecret, String token, String phone, int code) {
        return userApi.postVerifyCode(appSecret, "Bearer " + token, phone, code);
    }

    @Override
    public Observable<Object> postLoginWithMySabay(String appSecret) {
        return userApi.postLoginWithMySabay(appSecret);
    }

    @Override
    public Observable<Object> getUserProfile(String appSecret, String token) {
        return this.userApi.getUserProfile(appSecret, token);
    }


}
