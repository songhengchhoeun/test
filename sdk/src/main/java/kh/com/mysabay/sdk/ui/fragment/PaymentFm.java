package kh.com.mysabay.sdk.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.anjlab.android.iab.v3.BillingCommunicationException;
import com.anjlab.android.iab.v3.BillingHistoryRecord;
import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.Constants;
import com.anjlab.android.iab.v3.PurchaseData;
import com.anjlab.android.iab.v3.PurchaseInfo;
import com.anjlab.android.iab.v3.SkuDetails;
import com.anjlab.android.iab.v3.TransactionDetails;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import kh.com.mysabay.sdk.BuildConfig;
import kh.com.mysabay.sdk.R;
import kh.com.mysabay.sdk.base.BaseFragment;
import kh.com.mysabay.sdk.databinding.FmPaymentBinding;
import kh.com.mysabay.sdk.pojo.googleVerify.DataBody;
import kh.com.mysabay.sdk.pojo.googleVerify.GoogleVerifyBody;
import kh.com.mysabay.sdk.pojo.googleVerify.ReceiptBody;
import kh.com.mysabay.sdk.pojo.mysabay.MySabayItem;
import kh.com.mysabay.sdk.pojo.shop.Data;
import kh.com.mysabay.sdk.pojo.thirdParty.ThirdPartyItem;
import kh.com.mysabay.sdk.ui.activity.StoreActivity;
import kh.com.mysabay.sdk.utils.LogUtil;
import kh.com.mysabay.sdk.utils.MessageUtil;
import kh.com.mysabay.sdk.viewmodel.StoreApiVM;

/**
 * Created by Tan Phirum on 3/15/20
 * Gmail phirumtan@gmail.com
 */
public class PaymentFm extends BaseFragment<FmPaymentBinding, StoreApiVM> implements BillingProcessor.IBillingHandler {

    public static final String TAG = PaymentFm.class.getSimpleName();
    public static final String EXT_KEY_DATA = "EXT_KEY_DATA";

    private BillingProcessor bp;

    private Data mData;
    private static String PURCHASE_ID = "android.test.purchased";

    @NotNull
    @Contract("_ -> new")
    public static PaymentFm newInstance(Data item) {
        Bundle args = new Bundle();
        args.putParcelable(EXT_KEY_DATA, item);
        PaymentFm f = new PaymentFm();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (getArguments() != null)
            mData = getArguments().getParcelable(EXT_KEY_DATA);

        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fm_payment;
    }

    @Override
    public void initializeObjects(@NotNull View v, Bundle args) {
        viewModel.setShopItemSelected(mData);
        viewModel.getMySabayCheckout(v.getContext());
        if (!BillingProcessor.isIabServiceAvailable(v.getContext()))
            MessageUtil.displayDialog(v.getContext(), getString(R.string.upgrade_google_play));

        bp = new BillingProcessor(v.getContext(), null, this);
        bp.initialize();
        // or bp = BillingProcessor.newBillingProcessor(this, "YOUR LICENSE KEY FROM GOOGLE PLAY CONSOLE HERE", this);
        // See below on why this is a useful alternative
    }

    @Override
    public void assignValues() {
        viewModel.getNetworkState().observe(this, this::showProgressState);

        viewModel.getItemSelected().observe(this, data -> {
            if (data != null) {
                mViewBinding.tvPoint.setText(data.name);
                mViewBinding.tvPrice.setText(data.toUSDPrice());
                mViewBinding.tvTotal.setText(String.format(getString(R.string.total_s), data.toUSDPrice()));
            }
        });

        viewModel.getMySabayProvider().observe(this, new Observer<MySabayItem>() {
            @Override
            public void onChanged(MySabayItem mySabayItem) {
                if (mySabayItem.status == 200) {
                    if (mySabayItem.data.size() > 0)
                        mViewBinding.rdbMySabay.setVisibility(View.VISIBLE);
                    else
                        mViewBinding.rdbMySabay.setVisibility(View.GONE);
                } else
                    mViewBinding.rdbMySabay.setVisibility(View.GONE);
            }
        });

        viewModel.getThirdPartyProviders().observe(this, new Observer<ThirdPartyItem>() {
            @Override
            public void onChanged(ThirdPartyItem thirdPartyItem) {
                if (thirdPartyItem.status == 200) {
                    if (thirdPartyItem.data.size() > 0) {
                        mViewBinding.rdbThirdBankProvider.setVisibility(View.VISIBLE);
                    } else
                        mViewBinding.rdbThirdBankProvider.setVisibility(View.GONE);
                } else
                    mViewBinding.rdbThirdBankProvider.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void addListeners() {
        mViewBinding.btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bp.isOneTimePurchaseSupported() && (viewModel.getItemSelected().getValue() != null)) {
                    if (!BuildConfig.DEBUG)
                        PURCHASE_ID = viewModel.getItemSelected().getValue().packageId;
                    boolean isPurchase = bp.purchase(getActivity(), PURCHASE_ID);
                    boolean isConsumePurchase = bp.consumePurchase(PURCHASE_ID);

                    LogUtil.info(TAG, "purchase =" + isPurchase + ", comsumePurcase = " + isConsumePurchase);
                } else
                    MessageUtil.displayDialog(v.getContext(), "sorry your device not support in app purchase");

            }
        });

        mViewBinding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null)
                    getActivity().onBackPressed();
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
    protected Class<StoreApiVM> getViewModel() {
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

    @Override
    public void onProductPurchased(@NonNull String productId, TransactionDetails details) {
        LogUtil.debug(TAG, "product id " + productId + " TransactionDetails :" + details + " : " + bp.getPurchaseTransactionDetails(productId));
        if (details == null) {
            SkuDetails skuDetails = bp.getPurchaseListingDetails(productId);
            LogUtil.debug(TAG, "skuDetails =" + skuDetails);
        } else {
            PurchaseInfo info = details.purchaseInfo;
            PurchaseData purchaseData = info.purchaseData;
            GoogleVerifyBody googleVerifyBody = new GoogleVerifyBody();
            ReceiptBody receiptBody = new ReceiptBody();
            receiptBody.withSignature(info.signature);
            DataBody dataBody = new DataBody(purchaseData.orderId, purchaseData.packageName, purchaseData.productId,
                    purchaseData.purchaseTime == null ? 0 : purchaseData.purchaseTime.getTime(), purchaseData.purchaseState.ordinal(), purchaseData.purchaseToken);
            receiptBody.withData(dataBody);
            googleVerifyBody.withReceipt(receiptBody);
            if (getActivity() != null)
                viewModel.postToVerifyAppInPurchase(getActivity(), googleVerifyBody);
        }
    }

    @Override
    public void onPurchaseHistoryRestored() {
        /*
         * Called when purchase history was restored and the list of all owned PRODUCT ID's
         * was loaded from Google Play
         */
        LogUtil.debug(TAG, "onPurchaseHistoryRestored");
    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
        if (errorCode == Constants.BILLING_RESPONSE_RESULT_USER_CANCELED) {
            LogUtil.info(TAG, "user cancel purchase");
        }
        LogUtil.debug(TAG, "error code " + errorCode + " smg " + (error != null ? error.toString() : ""));
    }

    @Override
    public void onBillingInitialized() {
        LogUtil.debug(TAG, "onBillingInitialized");
        try {
            if (bp.isRequestBillingHistorySupported(Constants.PRODUCT_TYPE_MANAGED)) {
                Bundle extraParams = new Bundle();

                List<BillingHistoryRecord> lsBilling = bp.getPurchaseHistory(Constants.PRODUCT_TYPE_MANAGED, extraParams);
                LogUtil.debug(TAG, "listBilling " + lsBilling.size() + " payload ");

            }

        } catch (BillingCommunicationException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        if (bp != null) bp.release();
        super.onDestroy();
    }
}
