package kh.com.mysabay.sdk.viewmodel;

import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import kh.com.mysabay.sdk.repository.UserRepo;
import kh.com.mysabay.sdk.utils.AppRxSchedulers;

/**
 * Created by Tan Phirum on 3/8/20
 * Gmail phirumtan@gmail.com
 */
public class UserApiVM extends ViewModel {

    private final UserRepo userRepo;
    private final AppRxSchedulers appRxSchedulers;

    @Inject
    public UserApiVM(UserRepo userRepo, AppRxSchedulers appRxSchedulers) {
        this.userRepo = userRepo;
        this.appRxSchedulers = appRxSchedulers;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
