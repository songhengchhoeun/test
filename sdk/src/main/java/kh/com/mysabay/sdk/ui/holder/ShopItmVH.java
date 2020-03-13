package kh.com.mysabay.sdk.ui.holder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import kh.com.mysabay.sdk.databinding.PartialShopItemBinding;

/**
 * Created by Tan Phirum on 3/13/20
 * Gmail phirumtan@gmail.com
 */
public class ShopItmVH extends RecyclerView.ViewHolder {

    public PartialShopItemBinding viewBinding;

    public ShopItmVH(@NonNull View itemView) {
        super(itemView);
        this.viewBinding = DataBindingUtil.bind(itemView);
    }
}
