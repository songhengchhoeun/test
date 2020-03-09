package kh.com.mysabay.sdk.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.alimuzaffar.lib.pin.PinEntryEditText;

import org.apache.commons.lang3.StringUtils;

import kh.com.mysabay.sdk.R;
import kh.com.mysabay.sdk.base.BaseFragment;
import kh.com.mysabay.sdk.databinding.FragmentVerifiedBinding;
import kh.com.mysabay.sdk.ui.activity.LoginActivity;
import kh.com.mysabay.sdk.utils.MessageUtil;
import kh.com.mysabay.sdk.viewmodel.UserApiVM;

/**
 * Created by Tan Phirum on 3/7/20
 * Gmail phirumtan@gmail.com
 */
public class VerifiedFragment extends BaseFragment<FragmentVerifiedBinding, UserApiVM> {

    public static final String TAG = VerifiedFragment.class.getSimpleName();

    @Override
    public int getLayoutId() {
        return R.layout.fragment_verified;
    }

    @Override
    public void initializeObjects(View v, Bundle args) {

    }

    @Override
    public void assignValues() {

    }

    @Override
    public void addListeners() {
        mViewBinding.edtVerifyCode.setAnimateText(true);
        mViewBinding.edtVerifyCode.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
            @Override
            public void onPinEntered(CharSequence str) {
                if (StringUtils.equalsIgnoreCase(str, "5555")) {
                    MessageUtil.displayToast(getContext(), "verified success");
                } else {
                    MessageUtil.displayToast(getContext(), "verified failed");
                    mViewBinding.edtVerifyCode.setError(true);
                    mViewBinding.edtVerifyCode.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mViewBinding.edtVerifyCode.setText(null);
                        }
                    }, 1000);
                }
            }
        });


        viewModel.liveNetworkState.observe(this, this::showProgressState);

       /* viewModel.ver.observe(this, new Observer<Object>() {
            @Override
            public void onChanged(Object o) {
                LogUtil.debug(TAG, "success with object " + gson.toJson(o));
                if (getActivity() instanceof LoginActivity)
                    ((LoginActivity) getActivity()).initAddFragment(new VerifiedFragment(), VerifiedFragment.TAG, true);
            }
        });*/

        mViewBinding.tvResendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.postToGetUserProfile();
            }
        });

        mViewBinding.btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.postToVerified("", 0);
            }
        });
    }

    @Override
    public View assignProgressView() {
        return mViewBinding.viewEmpty.progressBar;
    }

    @Override
    public View assignEmptyView() {
        return mViewBinding.viewEmpty.viewRetry;
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
