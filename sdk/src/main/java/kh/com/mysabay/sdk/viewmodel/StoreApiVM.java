package kh.com.mysabay.sdk.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import kh.com.mysabay.sdk.Apps;
import kh.com.mysabay.sdk.R;
import kh.com.mysabay.sdk.pojo.AppItem;
import kh.com.mysabay.sdk.pojo.NetworkState;
import kh.com.mysabay.sdk.pojo.googleVerify.GoogleVerifyBody;
import kh.com.mysabay.sdk.pojo.googleVerify.GoogleVerifyResponse;
import kh.com.mysabay.sdk.pojo.mysabay.MySabayItem;
import kh.com.mysabay.sdk.pojo.shop.Data;
import kh.com.mysabay.sdk.pojo.shop.ShopItem;
import kh.com.mysabay.sdk.pojo.thirdParty.ThirdPartyItem;
import kh.com.mysabay.sdk.repository.StoreRepo;
import kh.com.mysabay.sdk.utils.AppRxSchedulers;
import kh.com.mysabay.sdk.utils.LogUtil;
import kh.com.mysabay.sdk.utils.MessageUtil;
import kh.com.mysabay.sdk.webservice.AbstractDisposableObs;

/**
 * Created by Tan Phirum on 3/8/20
 * Gmail phirumtan@gmail.com
 */
public class StoreApiVM extends ViewModel {

    private static final String TAG = StoreApiVM.class.getSimpleName();

    private final StoreRepo storeRepo;

    @Inject
    AppRxSchedulers appRxSchedulers;
    @Inject
    Gson gson;

    private final MediatorLiveData<String> _msgError = new MediatorLiveData<>();
    private final MediatorLiveData<NetworkState> _networkState;
    private final MediatorLiveData<ShopItem> _shopItem;
    private final CompositeDisposable mCompos;
    private final MediatorLiveData<Data> mDataSelected;
    private final MediatorLiveData<MySabayItem> mySabayItemMediatorLiveData;
    private final MediatorLiveData<ThirdPartyItem> thirdPartyItemMediatorLiveData;


    @Inject
    public StoreApiVM(StoreRepo storeRepo) {
        this.storeRepo = storeRepo;
        this._networkState = new MediatorLiveData<>();
        this._shopItem = new MediatorLiveData<>();
        this.mCompos = new CompositeDisposable();
        this.mDataSelected = new MediatorLiveData<>();
        this.mySabayItemMediatorLiveData = new MediatorLiveData<>();
        this.thirdPartyItemMediatorLiveData = new MediatorLiveData<>();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (mCompos != null) {
            mCompos.dispose();
            mCompos.clear();
        }
    }

    public void getShopFromServer(@NotNull Context context) {
        AppItem appItem = gson.fromJson(Apps.getInstance().getAppItem(), AppItem.class);
        storeRepo.getShopItem(context.getString(R.string.app_secret), appItem.token).subscribeOn(appRxSchedulers.io())
                .observeOn(appRxSchedulers.mainThread()).subscribe(new AbstractDisposableObs<ShopItem>(context, _networkState) {
            @Override
            protected void onSuccess(ShopItem item) {
                if (item.status == 200)
                    _shopItem.setValue(item);
                else MessageUtil.displayDialog(context, "something went wrong.");
            }

            @Override
            protected void onErrors(Throwable error) {
                LogUtil.error(TAG, error.getLocalizedMessage());
            }
        });
    }

    public LiveData<ShopItem> getShopItem() {
        return _shopItem;
    }

    public LiveData<NetworkState> getNetworkState() {
        return _networkState;
    }

    public LiveData<ThirdPartyItem> getThirdPartyProviders() {
        return thirdPartyItemMediatorLiveData;
    }

    public LiveData<MySabayItem> getMySabayProvider() {
        return mySabayItemMediatorLiveData;
    }

    public void setShopItemSelected(Data data) {
        _networkState.setValue(new NetworkState(NetworkState.Status.SUCCESS));
        this.mDataSelected.setValue(data);
    }

    public LiveData<Data> getItemSelected() {
        return this.mDataSelected;
    }

    public void getMySabayCheckout(@NotNull Context context) {
        AppItem appItem = gson.fromJson(Apps.getInstance().getAppItem(), AppItem.class);
        storeRepo.getMySabayCheckout(context.getString(R.string.app_secret), appItem.token, appItem.uuid).subscribeOn(appRxSchedulers.io())
                .observeOn(appRxSchedulers.mainThread()).subscribe(new Observer<MySabayItem>() {
            @Override
            public void onSubscribe(Disposable d) {
                mCompos.add(d);
            }

            @Override
            public void onNext(MySabayItem mySabayItem) {
                mySabayItemMediatorLiveData.setValue(mySabayItem);
            }

            @Override
            public void onError(Throwable e) {
                LogUtil.debug(TAG, "error " + e.getLocalizedMessage());
                get3PartyCheckout(context);
            }

            @Override
            public void onComplete() {
                get3PartyCheckout(context);
            }
        });

    }

    private void get3PartyCheckout(@NotNull Context context) {
        AppItem appItem = gson.fromJson(Apps.getInstance().getAppItem(), AppItem.class);
        storeRepo.get3PartyCheckout(context.getString(R.string.app_secret), appItem.token, appItem.uuid).subscribeOn(appRxSchedulers.io())
                .observeOn(appRxSchedulers.mainThread()).subscribe(new AbstractDisposableObs<ThirdPartyItem>(context, _networkState) {
            @Override
            protected void onSuccess(ThirdPartyItem thirdPartyItem) {
                LogUtil.debug(TAG, "ThirdPartyItem " + thirdPartyItem);
                thirdPartyItemMediatorLiveData.setValue(thirdPartyItem);
            }

            @Override
            protected void onErrors(Throwable error) {
                LogUtil.error(TAG, "error " + error.getLocalizedMessage());
            }
        });

    }

    public void postToVerifyAppInPurchase(@NotNull Context context, GoogleVerifyBody body) {
        AppItem appItem = gson.fromJson(Apps.getInstance().getAppItem(), AppItem.class);
        mCompos.add(storeRepo.postToVerifyGoogle(context.getString(R.string.app_secret), appItem.token, body).subscribeOn(appRxSchedulers.io())
                .observeOn(appRxSchedulers.mainThread()).subscribe(new Consumer<GoogleVerifyResponse>() {
                    @Override
                    public void accept(GoogleVerifyResponse googleVerifyResponse) throws Exception {
                        MessageUtil.displayDialog(context, googleVerifyResponse.message);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtil.error(TAG, "error " + throwable.getLocalizedMessage());
                    }
                }));
    }
}
