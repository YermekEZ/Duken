package com.example.dykenapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

public class ProductListActivity extends AppCompatActivity {

    int numberOfProducts = 0;

    private ImageButton searchImageButton, addImageButton, myProfileImageButton, orderImageButton;
    private TextView numberOfProductsTextView;
    private RecyclerView recyclerView;
    private EditText searchEditText;
    List<ProductData> productDataList;
    ListAdapter listAdapter;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        searchImageButton = findViewById(R.id.searchImageButton);
        addImageButton = findViewById(R.id.addImageButton);
        myProfileImageButton = findViewById(R.id.myProfileImageButton);
        orderImageButton = findViewById(R.id.orderImageButton);
        numberOfProductsTextView = findViewById(R.id.numberOfProductsTextView);
        searchEditText = findViewById(R.id.searchEditText);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration itemDecorator = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.item_layout));
        recyclerView.addItemDecoration(itemDecorator);
        //recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        productDataList = new ArrayList<>();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("products").child(SharedData.getPhoneNumber());

        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    ProductData productData = dataSnapshot.getValue(ProductData.class);
                    productDataList.add(productData);
                }
                listAdapter = new ListAdapter(productDataList);
                recyclerView.setAdapter(listAdapter);
                numberOfProducts = productDataList.size();
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

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });

        searchImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductListActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }
        });

        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductListActivity.this, AddProductActivity.class);
                startActivity(intent);
            }
        });

        myProfileImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductListActivity.this, MyProfileActivity.class);
                startActivity(intent);
            }
        });

        orderImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductListActivity.this, OrderActivity.class);
                startActivity(intent);
            }
        });
    }

    private void filter(String text) {
        List<ProductData> filteredList = new ArrayList<>();
        for(ProductData productData: productDataList){
            if(productData.getmProductName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(productData);
            }
        }
        numberOfProducts = filteredList.size();
        if(numberOfProducts == 1){
            numberOfProductsTextView.setText(numberOfProducts + " product");
        } else {
            numberOfProductsTextView.setText(numberOfProducts + " products");
        }
        listAdapter.filtered(filteredList);
    }

}