package com.example.dykenapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DetailsAdapter extends RecyclerView.Adapter {

    List<OrderData> orderDataList;

    public DetailsAdapter(List<OrderData> orderDataList) {
        this.orderDataList = orderDataList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.details_layout, parent, false);
        ViewHolderClass viewHolderClass = new ViewHolderClass(view);

        return viewHolderClass;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ViewHolderClass viewHolderClass = (ViewHolderClass) holder;

        OrderData orderData = orderDataList.get(position);
        viewHolderClass.productName.setText(orderData.getmName());
        viewHolderClass.pieces.setText(orderData.getmPieces() + "pcs.");
        viewHolderClass.price.setText("$" + orderData.getmPrice());
    }

    @Override
    public int getItemCount() {
        return orderDataList.size();
    }

    public class ViewHolderClass extends RecyclerView.ViewHolder{

        TextView productName, pieces, price;

        public ViewHolderClass(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            pieces = itemView.findViewById(R.id.pieces);
            price = itemView.findViewById(R.id.price);
        }
    }
}
