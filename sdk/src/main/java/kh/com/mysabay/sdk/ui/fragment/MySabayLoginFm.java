package kh.com.mysabay.sdk.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

import kh.com.mysabay.sdk.Apps;
import kh.com.mysabay.sdk.R;
import kh.com.mysabay.sdk.base.BaseFragment;
import kh.com.mysabay.sdk.databinding.FmMysabayLoginBinding;
import kh.com.mysabay.sdk.pojo.AppItem;
import kh.com.mysabay.sdk.utils.LogUtil;

/**
 * Created by Tan Phirum on 3/10/20
 * Gmail phirumtan@gmail.com
 */
public class MySabayLoginFm extends BaseFragment {

    public static final String TAG = MySabayLoginFm.class.getSimpleName();
    private static final String EXT_KEY_DEEPLINK = "EXT_KEY_DEEPLINK";

    public static MySabayLoginFm newInstance(String url) {
        Bundle args = new Bundle();
        args.putString(EXT_KEY_DEEPLINK, url);
        MySabayLoginFm f = new MySabayLoginFm();
        f.setArguments(args);
        return f;
    }

    private FmMysabayLoginBinding mViewBinding;
    private String mDeepLink;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (getArguments() != null)
            mDeepLink = getArguments().getString(EXT_KEY_DEEPLINK);

        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fm_mysabay_login;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void initializeObjects(View v, Bundle args) {
        mViewBinding = DataBindingUtil.bind(v);
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

                LogUtil.debug(TAG, "url =" + request.getUrl() + " token =" + request.getUrl().getQueryParameter("access_token"));
                String token = request.getUrl().getQueryParameter("access_token");
                if (!StringUtils.isBlank(token)) {
                    AppItem appItem = new AppItem("9c85c50a4362f687cd4507771ba81db5cf50eaa0b3008f4f943f77ba3ac6386b", token);
                    Apps.getInstance().saveAppItem(gson.toJson(appItem));
                    if (getActivity() != null)
                        getActivity().runOnUiThread(() -> getActivity().finish());
                }
                return super.shouldInterceptRequest(view, request);
            }
        });
    }

    @Override
    public void assignValues() {
        if (StringUtils.isBlank(mDeepLink)) {
            Map<String, String> header = new HashMap<>();
            header.put("app_secret", "9c85c50a4362f687cd4507771ba81db5cf50eaa0b3008f4f943f77ba3ac6386b");
            mViewBinding.wv.loadUrl("https://user.master.mysabay.com/api/v1/user/mysabay/login", header);
        } else
            mViewBinding.wv.loadUrl(mDeepLink);
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
