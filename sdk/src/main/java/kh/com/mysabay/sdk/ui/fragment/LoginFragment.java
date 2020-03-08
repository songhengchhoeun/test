package kh.com.mysabay.sdk.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import kh.com.mysabay.sdk.R;
import kh.com.mysabay.sdk.base.BaseFragment;
import kh.com.mysabay.sdk.databinding.FragmentLoginBinding;
import kh.com.mysabay.sdk.ui.activity.LoginActivity;
import kh.com.mysabay.sdk.viewmodel.UserApiVM;

/**
 * Created by Tan Phirum on 3/7/20
 * Gmail phirumtan@gmail.com
 */
public class LoginFragment extends BaseFragment<FragmentLoginBinding, UserApiVM> {

    public static final String TAG = LoginFragment.class.getSimpleName();

    @NotNull
    @Contract(" -> new")
    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    public void initializeObjects(View v, Bundle args) {

    }

    @Override
    public void assignValues() {

    }

    @Override
    public void addListeners() {

    }

    @Override
    public int getViewProgressId() {
        return R.id.progress_bar;
    }

    @Override
    public int getViewEmptyId() {
        return R.id.view_empty;
    }

    @Override
    protected Class<UserApiVM> getViewModel() {
        return UserApiVM.class;
    }

    @Override
    protected void onOnlineCallback() {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ((LoginActivity) context).userComponent.inject(this);
        // Now you can access loginViewModel here and onCreateView too
        // (shared instance with the Activity and the other Fragment)
    }
}
