package kh.com.mysabay.sdk.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import kh.com.mysabay.sdk.pojo.NetworkState;
import kh.com.mysabay.sdk.repository.UserRepo;
import kh.com.mysabay.sdk.ui.activity.LoginActivity;
import kh.com.mysabay.sdk.ui.fragment.VerifiedFragment;
import kh.com.mysabay.sdk.utils.AppRxSchedulers;
import kh.com.mysabay.sdk.utils.LogUtil;

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
    public LiveData<NetworkState> liveNetworkState;
    private final MediatorLiveData<Object> _login;
    public LiveData<Object> login;
    public CompositeDisposable mCompositeDisposable;

    @Inject
    public UserApiVM(UserRepo userRepo, AppRxSchedulers appRxSchedulers) {
        this.userRepo = userRepo;
        this.appRxSchedulers = appRxSchedulers;
        this._networkState = new MediatorLiveData<>();
        this.liveNetworkState = _networkState;
        this._login = new MediatorLiveData<>();
        this.login = _login;
        this.mCompositeDisposable = new CompositeDisposable();
    }

    public void postToLogin(Context context, String appSecret, String phone) {
        _networkState.setValue(new NetworkState(NetworkState.Status.LOADING));
        mCompositeDisposable.add(this.userRepo.getUserLogin(appSecret, phone).subscribeOn(appRxSchedulers.io())
                .observeOn(appRxSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        _networkState.setValue(new NetworkState(NetworkState.Status.SUCCESS));
                        _login.setValue(o);
                        if (context instanceof LoginActivity)
                            ((LoginActivity) context).initAddFragment(new VerifiedFragment(), VerifiedFragment.TAG, true);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        _networkState.setValue(new NetworkState(NetworkState.Status.ERROR, throwable.getLocalizedMessage()));
                        if (context instanceof LoginActivity)
                            ((LoginActivity) context).initAddFragment(new VerifiedFragment(), VerifiedFragment.TAG, true);
                    }
                }));
    }

    public void postToVerified(String phone, int code) {
        _networkState.setValue(new NetworkState(NetworkState.Status.LOADING));
        mCompositeDisposable.add(this.userRepo.postVerifyCode("", "", phone, code).subscribeOn(appRxSchedulers.io())
                .observeOn(appRxSchedulers.mainThread()).subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

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
