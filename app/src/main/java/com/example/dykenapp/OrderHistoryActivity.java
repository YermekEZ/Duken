package com.example.dykenapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity implements OrderHistoryAdapter.OnGetInfoListener{

    private TextView numberOfOrdersTextView;
    private ImageButton backImageButton;
    private RecyclerView recyclerView;

    private int numberOfOrders = 0;
    List<OrderHistoryData> orderHistoryDataList;
    OrderHistoryAdapter orderHistoryAdapter;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        backImageButton = findViewById(R.id.backImageButton);
        recyclerView = findViewById(R.id.recyclerView);
        numberOfOrdersTextView = findViewById(R.id.numberOfOrdersTextView);

        numberOfOrdersTextView.setText(numberOfOrders + " orders");

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("orders").child(SharedData.getPhoneNumber());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration itemDecorator = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.item_layout));
        recyclerView.addItemDecoration(itemDecorator);

        orderHistoryDataList = new ArrayList<>();
        orderHistoryAdapter = new OrderHistoryAdapter(orderHistoryDataList, this);

        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    OrderHistoryData orderHistoryData = dataSnapshot.getValue(OrderHistoryData.class);
                    orderHistoryDataList.add(orderHistoryData);
                }
                recyclerView.setAdapter(orderHistoryAdapter);
                numberOfOrders = orderHistoryDataList.size();
                if(numberOfOrders == 1){
                    numberOfOrdersTextView.setText(numberOfOrders + " order");
                } else {
                    numberOfOrdersTextView.setText(numberOfOrders + " orders");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        backImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderHistoryActivity.this, MyProfileActivity.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public void getDetails(int position) {
        SharedData.setDate(orderHistoryDataList.get(position).getmDate());
        /*Intent intent = new Intent(OrderHistoryActivity.this, OrderDetailsActivity.class);
        startActivity(intent);*/
        OrderDetailsDialog orderDetailsDialog = new OrderDetailsDialog();
        orderDetailsDialog.show(getSupportFragmentManager(), "Order details");
    }

}