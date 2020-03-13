package kh.com.mysabay.sdk.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import kh.com.mysabay.sdk.R;
import kh.com.mysabay.sdk.adapter.ShopAdapter;
import kh.com.mysabay.sdk.base.BaseFragment;
import kh.com.mysabay.sdk.databinding.FmShopBinding;
import kh.com.mysabay.sdk.ui.activity.StoreActivity;
import kh.com.mysabay.sdk.viewmodel.StoreApiVM;

/**
 * Created by Tan Phirum on 3/13/20
 * Gmail phirumtan@gmail.com
 */
public class ShopsFragment extends BaseFragment<FmShopBinding, StoreApiVM> {

    public static final String TAG = ShopsFragment.class.getSimpleName();

    private ShopAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @NotNull
    @Contract(" -> new")
    public static ShopsFragment newInstance() {
        return new ShopsFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fm_shop;
    }

    @Override
    public void initializeObjects(@NotNull View v, Bundle args) {
        mAdapter = new ShopAdapter(v.getContext());
        mAdapter.setHasStableIds(true);
        mLayoutManager = new GridLayoutManager(v.getContext(), 2);
        mViewBinding.rcv.setLayoutManager(mLayoutManager);
        mViewBinding.rcv.setAdapter(mAdapter);

        viewModel.getNetworkState().observe(this, this::showProgressState);
        viewModel.getShopItem().observe(this, item -> mAdapter.insert(item.data));
    }

    @Override
    public void assignValues() {
        if (getContext() != null)
            viewModel.getShopFromServer(getContext());
    }

    @Override
    public void addListeners() {

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
}
