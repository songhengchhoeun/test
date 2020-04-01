package kh.com.mysabay.sdk.ui.holder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import kh.com.mysabay.sdk.databinding.PartialBankProviderItemBinding;

/**
 * Created by Tan Phirum on 4/1/20
 * Gmail phirumtan@gmail.com
 */
public class BankProviderVH extends RecyclerView.ViewHolder {

    public PartialBankProviderItemBinding view;

    public BankProviderVH(@NonNull View itemView) {
        super(itemView);
        view = DataBindingUtil.bind(itemView);
    }

    public void setBankName(String bankName) {
        view.bankName.setText(bankName);
    }
}
