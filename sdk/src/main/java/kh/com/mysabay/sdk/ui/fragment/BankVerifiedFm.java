package kh.com.mysabay.sdk.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import kh.com.mysabay.sdk.R;
import kh.com.mysabay.sdk.base.BaseFragment;
import kh.com.mysabay.sdk.databinding.PartialBankProviderVerifiedBinding;
import kh.com.mysabay.sdk.pojo.thirdParty.payment.Data;
import kh.com.mysabay.sdk.ui.activity.StoreActivity;
import kh.com.mysabay.sdk.utils.LogUtil;
import kh.com.mysabay.sdk.viewmodel.StoreApiVM;

/**
 * Created by Tan Phirum on 4/1/20
 * Gmail phirumtan@gmail.com
 */
public class BankVerifiedFm extends BaseFragment<PartialBankProviderVerifiedBinding, StoreApiVM> {

    public static final String TAG = BankVerifiedFm.class.getSimpleName();
    private static final String EXT_KEY_PaymentResponseItem = "PaymentResponseItem";
    private String mDeepLink;

    private Data mPaymentResponseItem;

    public static BankVerifiedFm newInstance(Data item) {
        Bundle args = new Bundle();
        args.putParcelable(EXT_KEY_PaymentResponseItem, item);
        BankVerifiedFm f = new BankVerifiedFm();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (getArguments() != null)
            mPaymentResponseItem = getArguments().getParcelable(EXT_KEY_PaymentResponseItem);

        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.partial_bank_provider_verified;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void initializeObjects(View v, Bundle args) {
        WebView.setWebContentsDebuggingEnabled(true);
        mViewBinding.wv.getSettings().setJavaScriptEnabled(true);
        mViewBinding.wv.getSettings().setLoadsImagesAutomatically(true);
        mViewBinding.wv.getSettings().setLoadWithOverviewMode(true);
        mViewBinding.wv.getSettings().setUseWideViewPort(true);
        mViewBinding.wv.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        mViewBinding.wv.getSettings().setSupportZoom(true);
        mViewBinding.wv.getSettings().setBuiltInZoomControls(true);
        mViewBinding.wv.getSettings().setDisplayZoomControls(false);
        mViewBinding.wv.clearHistory();
        mViewBinding.wv.clearCache(true);
        mViewBinding.wv.setWebViewClient(new WebViewClient() {
            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
/*
                LogUtil.debug(TAG, "url =" + request.getUrl() + " token =" + request.getUrl().getQueryParameter("access_token"));
                String token = request.getUrl().getQueryParameter("access_token");
                if (!StringUtils.isBlank(token) && getActivity() != null)
                    getActivity().runOnUiThread(() -> {
                        viewModel.postToGetUserProfile(getActivity(), token);
                    });*/

                return super.shouldInterceptRequest(view, request);
            }

            @Override
            public void onFormResubmission(WebView view, Message dontResend, Message resend) {
                LogUtil.debug(TAG, "dontResent " + dontResend);
                LogUtil.debug(TAG, "Message " + resend.toString());
                super.onFormResubmission(view, dontResend, resend);
            }
        });
    }

    @Override
    public void assignValues() {
        /*if (StringUtils.isBlank(mDeepLink)) {
            Map<String, String> header = new HashMap<>();
            header.put("app_secret", "9c85c50a4362f687cd4507771ba81db5cf50eaa0b3008f4f943f77ba3ac6386b");
            mViewBinding.wv.loadUrl("https://user.master.mysabay.com/api/v1/user/mysabay/login", header);
        } else
            mViewBinding.wv.loadUrl(mDeepLink);*/
        String html = showFormVerified(mPaymentResponseItem);
        LogUtil.debug(TAG, html);
        mViewBinding.wv.loadDataWithBaseURL("https://store.testing.mysabay.com/", html, "text/html", "utf-8", "https://store.testing.mysabay.com/");
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
        return StoreApiVM.class;
    }

    @Override
    protected void onOnlineCallback() {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ((StoreActivity) context).userComponent.inject(this);
        // Now you can access loginViewModel here and onCreateView too
        // (shared instance with the Activity and the other Fragment)
    }

    private String showFormVerified(Data item) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>Demo Payment Provider</title>\n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "    <link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/bulma@0.8.0/css/bulma.min.css\">\n" +
                "</head>\n" +
                "<body>\n" +
                "    <section class=\"section\">\n" +
                "        <div class=\"container\">\n" +
                "            <h1 class=\"title\">Demo Payment Provider</h1>\n" +
                "        </div>\n" +
                "    </section>\n" +
                "    <section class=\"section\">\n" +
                "        <div class=\"container\">\n" +
                "            <h1 class=\"title\">Acleda-Migs</h1>\n" +
                "            <p>Onetime payment flow</p>\n" +
                "            <form action=\"" + item.requestUrl + "\" method=\"post\">\n" +
                "                <input type=\"hidden\" name=\"hash\" value=\"" + item.hash + "\">\n" +
                "                <input type=\"hidden\" name=\"signature\" value=\"" + item.signature + "\">\n" +
                "                <input type=\"hidden\" name=\"public_key\" value=\"" + item.publicKey + "\">\n" +
                "                \n" +
                "                <div class=\"field\">\n" +
                "                    <div class=\"control\">\n" +
                "                        <button class=\"button is-link\" type=\"submit\">Submit</button>\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "            </form>\n" +
                "        </div>\n" +
                "    </section>\n" +
                "\u200B\n" +
                "</body>\n" +
                "</html>";
    }

}