package com.example.dykenapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter {

    List<OrderHistoryData> orderHistoryDataList;
    private OnGetInfoListener mListener;

    public interface OnGetInfoListener{
        void getDetails(int position);
    }

    public OrderHistoryAdapter(List<OrderHistoryData> orderHistoryDataList, OnGetInfoListener onGetInfoListener) {
        this.orderHistoryDataList = orderHistoryDataList;
        this.mListener = onGetInfoListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history_layout, parent, false);
        ViewHolderClass viewHolderClass = new ViewHolderClass(view, mListener);

        return viewHolderClass;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ViewHolderClass viewHolderClass = (ViewHolderClass) holder;

        OrderHistoryData orderHistoryData = orderHistoryDataList.get(position);
        viewHolderClass.orderDateTextView.setText(orderHistoryData.getmDate());
        viewHolderClass.totalSumTextView.setText("$" + orderHistoryData.getmTotalPrice());
    }

    @Override
    public int getItemCount() {
        return orderHistoryDataList.size();
    }

    public class ViewHolderClass extends RecyclerView.ViewHolder{

        TextView orderDateTextView, totalSumTextView;
        ImageButton detailsImageButton;
        private OnGetInfoListener mListener;

        public ViewHolderClass(@NonNull View itemView, OnGetInfoListener onGetInfoListener) {
            super(itemView);
            orderDateTextView = itemView.findViewById(R.id.orderDateTextView);
            totalSumTextView = itemView.findViewById(R.id.totalSumTextView);
            detailsImageButton = itemView.findViewById(R.id.detailsImageButton);

            this.mListener = onGetInfoListener;

            detailsImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.getDetails(getAdapterPosition());
                }
            });
        }
    }
}
