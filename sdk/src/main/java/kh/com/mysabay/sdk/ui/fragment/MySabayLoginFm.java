package kh.com.mysabay.sdk.ui.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import kh.com.mysabay.sdk.R;
import kh.com.mysabay.sdk.base.BaseFragment;
import kh.com.mysabay.sdk.databinding.FmMysabayLoginBinding;
import kh.com.mysabay.sdk.ui.activity.LoginActivity;

/**
 * Created by Tan Phirum on 3/10/20
 * Gmail phirumtan@gmail.com
 */
public class MySabayLoginFm extends BaseFragment {

    public static final String TAG = MySabayLoginFm.class.getSimpleName();

    FmMysabayLoginBinding mViewBinding;

    @Override
    public int getLayoutId() {
        return R.layout.fm_mysabay_login;
    }

    @Override
    public void initializeObjects(View v, Bundle args) {
        mViewBinding = DataBindingUtil.bind(v);
    }

    @Override
    public void assignValues() {
        LoginActivity.loginActivity.viewModel.loginMySabay.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                mViewBinding.wv.loadData(s, "text/html", "utf-8");
            }
        });

    }

    @Override
    public void addListeners() {

    }

    @Override
    public View assignProgressView() {
        return null;
    }

    @Override
    public View assignEmptyView() {
        return null;
    }

    @Override
    protected Class getViewModel() {
        return null;
    }

    @Override
    protected void onOnlineCallback() {

    }
}
