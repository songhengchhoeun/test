package kh.com.mysabay.sample;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import org.apache.commons.lang3.StringUtils;

import kh.com.mysabay.sample.databinding.ActivityMainBinding;
import kh.com.mysabay.sdk.Apps;
import kh.com.mysabay.sdk.MySabaySDK;
import kh.com.mysabay.sdk.callback.LoginListener;
import kh.com.mysabay.sdk.callback.PaymentListener;
import kh.com.mysabay.sdk.utils.MessageUtil;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mViewBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mViewBinding.viewPb.setVisibility(View.GONE);
        findViewById(R.id.show_login_screen).setOnClickListener(v -> {
            mViewBinding.viewPb.setVisibility(View.VISIBLE);
            if (!StringUtils.isBlank(Apps.getInstance().getAppItem())) {
                MessageUtil.displayDialog(v.getContext(), "User already login", "choose option below",
                        "Logout", "Get user information",
                        (dialog, which) -> {
                            MySabaySDK.getInstance().logout();
                            mViewBinding.viewPb.setVisibility(View.GONE);
                        }, (dialog, which) -> MySabaySDK.getInstance().getUserInfo(info -> {
                            MessageUtil.displayDialog(v.getContext(), info);
                            mViewBinding.viewPb.setVisibility(View.GONE);
                        }));
            } else
                MySabaySDK.getInstance().showLoginView(new LoginListener() {
                    @Override
                    public void loginSuccess(String accessToken) {
                        MessageUtil.displayToast(v.getContext(), "accessToken = " + accessToken);
                    }

                    @Override
                    public void loginFailed(Object error) {
                        MessageUtil.displayToast(v.getContext(), "error = " + error);
                    }
                });
        });

        mViewBinding.showPaymentPreAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MySabaySDK.getInstance().showShopView(new PaymentListener() {
                    @Override
                    public void purchaseMySabaySuccess(Object dataMySabay) {
                        MessageUtil.displayToast(v.getContext(), "dataMySabay = " + dataMySabay);
                    }

                    @Override
                    public void purchaseAIPSuccess(Object dataAIP) {
                        MessageUtil.displayToast(v.getContext(), "dataAIP = " + dataAIP);
                    }

                    @Override
                    public void purchaseFailed(Object dataError) {
                        MessageUtil.displayToast(v.getContext(), "error = " + dataError);
                    }
                });
            }
        });
    }

    @Override
    protected void onStop() {
        mViewBinding.viewPb.setVisibility(View.GONE);
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        mViewBinding.viewPb.setVisibility(View.GONE);
        super.onBackPressed();
    }
}
