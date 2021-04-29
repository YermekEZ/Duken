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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailsActivity extends AppCompatActivity {

    private TextView numberOfProductsTextView;
    private ImageButton backImageButton;
    private RecyclerView recyclerView;

    private int numberOfProducts = 0;

    List<OrderData> orderDataList;
    DetailsAdapter detailsAdapter;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        numberOfProductsTextView = findViewById(R.id.numberOfProductsTextView);
        backImageButton = findViewById(R.id.backImageButton);
        recyclerView = findViewById(R.id.recyclerView);

        numberOfProductsTextView.setText(numberOfProducts + " products");

        backImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderDetailsActivity.this, OrderHistoryActivity.class);
                startActivity(intent);
            }
        });

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("orderDetails").child(SharedData.getPhoneNumber());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration itemDecorator = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.item_layout));
        recyclerView.addItemDecoration(itemDecorator);

        orderDataList = new ArrayList<>();

        detailsAdapter = new DetailsAdapter(orderDataList);

        mDatabaseReference.child(SharedData.getDate()).child("products").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    OrderData orderData = dataSnapshot.getValue(OrderData.class);
                    orderDataList.add(orderData);
                }
                detailsAdapter = new DetailsAdapter(orderDataList);
                recyclerView.setAdapter(detailsAdapter);
                numberOfProducts = orderDataList.size();
                if(numberOfProducts == 1){
                    numberOfProductsTextView.setText(numberOfProducts + " product");
                } else {
                    numberOfProductsTextView.setText(numberOfProducts + " products");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}