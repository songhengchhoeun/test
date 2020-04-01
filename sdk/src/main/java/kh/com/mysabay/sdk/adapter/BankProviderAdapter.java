package kh.com.mysabay.sdk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import kh.com.mysabay.sdk.R;
import kh.com.mysabay.sdk.callback.OnRcvItemClick;
import kh.com.mysabay.sdk.pojo.thirdParty.Data;
import kh.com.mysabay.sdk.ui.holder.BankProviderVH;

/**
 * Created by Tan Phirum on 4/1/20
 * Gmail phirumtan@gmail.com
 */
public class BankProviderAdapter extends RecyclerView.Adapter<BankProviderVH> {

    private List<Data> dataList;
    private LayoutInflater mInflater;
    private OnRcvItemClick mListener;

    public BankProviderAdapter(Context context, List<Data> dataList, OnRcvItemClick listener) {
        this.dataList = dataList;
        this.mInflater = LayoutInflater.from(context);
        this.mListener = listener;
    }

    @NonNull
    @Override
    public BankProviderVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BankProviderVH(mInflater.inflate(R.layout.partial_bank_provider_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BankProviderVH holder, int position) {
        Data item = dataList.get(position);
        holder.setBankName(item.serviceName);
        holder.view.viewBankItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
