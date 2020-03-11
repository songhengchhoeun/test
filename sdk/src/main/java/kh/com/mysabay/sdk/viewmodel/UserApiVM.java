package kh.com.mysabay.sdk.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import kh.com.mysabay.sdk.Apps;
import kh.com.mysabay.sdk.pojo.AppItem;
import kh.com.mysabay.sdk.pojo.NetworkState;
import kh.com.mysabay.sdk.pojo.login.LoginItem;
import kh.com.mysabay.sdk.pojo.profile.UserProfileItem;
import kh.com.mysabay.sdk.repository.UserRepo;
import kh.com.mysabay.sdk.ui.activity.LoginActivity;
import kh.com.mysabay.sdk.ui.fragment.MySabayLoginFm;
import kh.com.mysabay.sdk.ui.fragment.VerifiedFragment;
import kh.com.mysabay.sdk.utils.AppRxSchedulers;
import kh.com.mysabay.sdk.utils.LogUtil;
import kh.com.mysabay.sdk.utils.MessageUtil;
import kh.com.mysabay.sdk.webservice.AbstractDisposableObs;

/**
 * Created by Tan Phirum on 3/8/20
 * Gmail phirumtan@gmail.com
 */
public class UserApiVM extends ViewModel {

    private static final String TAG = UserApiVM.class.getSimpleName();

    private final UserRepo userRepo;
    private final AppRxSchedulers appRxSchedulers;
    @Inject
    Gson gson;

    private final MediatorLiveData<String> _msgError = new MediatorLiveData<>();
    private final MediatorLiveData<NetworkState> _networkState;
    private final MediatorLiveData<String> _loginMySabay;
    private MediatorLiveData<LoginItem> _responseLogin;
    public LiveData<NetworkState> liveNetworkState;
    private final MediatorLiveData<String> _login;
    public LiveData<String> login;
    public LiveData<String> loginMySabay;

    public CompositeDisposable mCompositeDisposable;

    @Inject
    public UserApiVM(UserRepo userRepo, AppRxSchedulers appRxSchedulers) {
        this.userRepo = userRepo;
        this.appRxSchedulers = appRxSchedulers;
        this._networkState = new MediatorLiveData<>();
        this._responseLogin = new MediatorLiveData<>();
        this._loginMySabay = new MediatorLiveData<>();
        this.liveNetworkState = _networkState;
        this._login = new MediatorLiveData<>();
        this.login = _login;
        this.loginMySabay = _loginMySabay;
        this.mCompositeDisposable = new CompositeDisposable();
    }

    public void setLoginItemData(LoginItem item) {
        _responseLogin.setValue(item);
    }

    public LiveData<LoginItem> getResponseLogin() {
        return _responseLogin;
    }

    public void postToLogin(Context context, String appSecret, String phone) {
        _login.setValue(phone);
        this.userRepo.getUserLogin(appSecret, phone).subscribeOn(appRxSchedulers.io())
                .observeOn(appRxSchedulers.mainThread())
                .subscribe(new AbstractDisposableObs<LoginItem>(context, _networkState, null) {
                    @Override
                    protected void onSuccess(LoginItem item) {
                        if (item.status == 200) {
                            item.data.withPhone(phone);
                            item.data.withAppSecret(appSecret);
                            _responseLogin.setValue(item);

                            MessageUtil.displayToast(context, item.data.message);
                            if (context instanceof LoginActivity)
                                ((LoginActivity) context).initAddFragment(new VerifiedFragment(), VerifiedFragment.TAG, true);
                        }
                    }

                    @Override
                    protected void onErrors(Throwable error) {
                        LogUtil.error(TAG, error.getLocalizedMessage());
                    }
                });
    }

    public void resendOTP(Context context) {
        _networkState.setValue(new NetworkState(NetworkState.Status.LOADING));
        LoginItem item = getResponseLogin().getValue();
        if (item == null) {
            _networkState.setValue(new NetworkState(NetworkState.Status.ERROR, "Something went wrong, please login again"));
            return;
        }
        this.userRepo.getUserLogin(item.data.appSecret, item.data.phone).subscribeOn(appRxSchedulers.io())
                .observeOn(appRxSchedulers.mainThread())
                .subscribe(new AbstractDisposableObs<LoginItem>(context, _networkState) {
                    @Override
                    protected void onSuccess(LoginItem item1) {
                        if (item1.status == 200) {
                            item1.data.withPhone(item.data.phone);
                            item1.data.withAppSecret(item.data.appSecret);
                            _responseLogin.setValue(item1);
                            MessageUtil.displayToast(context, item1.data.message);
                        }
                    }

                    @Override
                    protected void onErrors(Throwable error) {
                        LogUtil.error(TAG, error.getLocalizedMessage());
                    }
                });
    }

    public void postToVerified(Context context, int code) {
        _networkState.setValue(new NetworkState(NetworkState.Status.LOADING));
        LoginItem item = getResponseLogin().getValue();
        if (item == null) {
            _networkState.setValue(new NetworkState(NetworkState.Status.ERROR, "Something went wrong, please login again"));
            return;
        }
        mCompositeDisposable.add(this.userRepo.postVerifyCode(item.data.appSecret, item.data.accessToken, item.data.phone, code).subscribeOn(appRxSchedulers.io())
                .observeOn(appRxSchedulers.mainThread()).subscribe(response -> {
                    if (response.status == 200) {
                        if (response.data != null) {
                            AppItem appItem = new AppItem(item.data.appSecret, item.data.accessToken, response.data.uuid);
                            String encrypted = gson.toJson(appItem);
                            Apps.getInstance().saveAppItem(encrypted);
                            MessageUtil.displayToast(context, "verified code success");
                            LogUtil.debug(TAG, "write appItem success");
                            LoginActivity.loginActivity.finish();
                        } else
                            LogUtil.error(TAG, "verified data is null");
                    } else
                        LogUtil.error(TAG, "verify code response with status :" + response.status);

                }, throwable -> LogUtil.error(TAG, throwable.getLocalizedMessage())));
    }

    public void postToLoginWithMySabay(Context context, String appSecret) {
        _networkState.setValue(new NetworkState(NetworkState.Status.LOADING));
        mCompositeDisposable.add(this.userRepo.postLoginWithMySabay(appSecret).subscribeOn(appRxSchedulers.io())
                .observeOn(appRxSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String o) throws Exception {
                        _networkState.setValue(new NetworkState(NetworkState.Status.SUCCESS));
                        _loginMySabay.setValue(o);
                        if (context instanceof LoginActivity)
                            ((LoginActivity) context).initAddFragment(new MySabayLoginFm(), MySabayLoginFm.TAG, true);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        _networkState.setValue(new NetworkState(NetworkState.Status.ERROR, throwable.getLocalizedMessage()));
                    }
                }));
    }

    public void postToGetUserProfile() {
        _networkState.setValue(new NetworkState(NetworkState.Status.LOADING));
        mCompositeDisposable.add(this.userRepo.getUserProfile("appSecret", "").subscribeOn(appRxSchedulers.io())
                .observeOn(appRxSchedulers.mainThread())
                .subscribe(new Consumer<UserProfileItem>() {
                    @Override
                    public void accept(UserProfileItem o) throws Exception {
                        _networkState.setValue(new NetworkState(NetworkState.Status.SUCCESS));

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        _networkState.setValue(new NetworkState(NetworkState.Status.ERROR, throwable.getLocalizedMessage()));
                        String item = Apps.getInstance().getAppItem();
                        LogUtil.debug(TAG, "item " + item);
                        /*if (!StringUtils.isBlank(item)) {
                            LogUtil.debug(TAG, "item decrypted " + rsaEncryptUtils.decrypt(item));
                        } else {
                            LogUtil.debug(TAG, "app item from preference null");
                        }*/
                    }
                }));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        LogUtil.debug(TAG, "onClearerd call");
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
            mCompositeDisposable = null;
        }
    }
}
