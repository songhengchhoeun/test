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
import kh.com.mysabay.sdk.repository.UserRepo;
import kh.com.mysabay.sdk.ui.activity.LoginActivity;
import kh.com.mysabay.sdk.ui.fragment.VerifiedFragment;
import kh.com.mysabay.sdk.utils.AppRxSchedulers;
import kh.com.mysabay.sdk.utils.LogUtil;
import kh.com.mysabay.sdk.utils.RSAEncryptUtils;
import kh.com.mysabay.sdk.webservice.AbstractDisposableObs;

/**
 * Created by Tan Phirum on 3/8/20
 * Gmail phirumtan@gmail.com
 */
public class UserApiVM extends ViewModel {

    private static final String TAG = UserApiVM.class.getSimpleName();

    private final UserRepo userRepo;
    private final AppRxSchedulers appRxSchedulers;
    private final RSAEncryptUtils rsaEncryptUtils;
    @Inject
    Gson gson;

    private final MediatorLiveData<String> _msgError = new MediatorLiveData<>();
    private final MediatorLiveData<NetworkState> _networkState;
    public LiveData<NetworkState> liveNetworkState;
    private final MediatorLiveData<String> _login;
    public LiveData<String> login;
    public LiveData<Object> responseLogin;

    public CompositeDisposable mCompositeDisposable;

    @Inject
    public UserApiVM(UserRepo userRepo, AppRxSchedulers appRxSchedulers, RSAEncryptUtils rsaEncryptUtils) {
        this.userRepo = userRepo;
        this.appRxSchedulers = appRxSchedulers;
        this.rsaEncryptUtils = rsaEncryptUtils;
        this._networkState = new MediatorLiveData<>();
        this.liveNetworkState = _networkState;
        this._login = new MediatorLiveData<>();
        this.login = _login;
        this.mCompositeDisposable = new CompositeDisposable();
    }

    public void postToLogin(Context context, String appSecret, String phone) {
        this.userRepo.getUserLogin(appSecret, phone).subscribeOn(appRxSchedulers.io())
                .observeOn(appRxSchedulers.mainThread())
                .subscribe(new AbstractDisposableObs<Object>(context, _networkState, null) {
                    @Override
                    protected void onSuccess(Object o) {
                        _login.setValue(phone);
                        if (context instanceof LoginActivity)
                            ((LoginActivity) context).initAddFragment(new VerifiedFragment(), VerifiedFragment.TAG, true);
                    }

                    @Override
                    protected void onErrors(Throwable error) {
                        if (context instanceof LoginActivity)
                            ((LoginActivity) context).initAddFragment(new VerifiedFragment(), VerifiedFragment.TAG, true);
                    }
                });
    }

    public void postToVerified(String phone, int code) {
        _networkState.setValue(new NetworkState(NetworkState.Status.LOADING));
        mCompositeDisposable.add(this.userRepo.postVerifyCode("", "", phone, code).subscribeOn(appRxSchedulers.io())
                .observeOn(appRxSchedulers.mainThread()).subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        AppItem item = new AppItem("secret", "qwertyui1234567", "888");

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        AppItem item = new AppItem("secret", "qwertyui1234567", "888");
                        String encrypted = gson.toJson(item);
                        Apps.getInstance().saveAppItem(encrypted);
                        LogUtil.debug(TAG, "encrypted " + encrypted);
                    }
                }));
    }

    public void postToLoginWithMySabay(Context context, String appSecret) {
        _networkState.setValue(new NetworkState(NetworkState.Status.LOADING));
        mCompositeDisposable.add(this.userRepo.postLoginWithMySabay(appSecret).subscribeOn(appRxSchedulers.io())
                .observeOn(appRxSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        _networkState.setValue(new NetworkState(NetworkState.Status.SUCCESS));

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
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
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
