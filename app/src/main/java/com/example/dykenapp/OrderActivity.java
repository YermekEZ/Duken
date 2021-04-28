package com.example.dykenapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class OrderActivity extends AppCompatActivity implements EnterCountDialog.EnterCountDialogListener, OrderListAdapter.OnItemClick {

    int numberOfProducts = 0;
    int totalPrice = 0;
    int priceForOne = 0;
    String scannedCode;
    boolean exist = false;

    List<ProductData> productDataList;
    OrderListAdapter listAdapter;

    private TextView numberOfProductsTextView;
    private RecyclerView recyclerView;
    private FloatingActionButton addProductFloatingButton;
    private Button makeOrderButton;
    private ImageButton listImageButton, searchImageButton, addImageButton, myProfileImageButton;
    private ProgressBar progressBar;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        numberOfProductsTextView = findViewById(R.id.numberOfProductsTextView);
        recyclerView = findViewById(R.id.recyclerView);
        addProductFloatingButton = findViewById(R.id.addProductFloatingButton);
        makeOrderButton = findViewById(R.id.makeOrder);
        listImageButton = findViewById(R.id.listImageButton);
        searchImageButton = findViewById(R.id.searchImageButton);
        addImageButton = findViewById(R.id.addImageButton);
        myProfileImageButton = findViewById(R.id.myProfileImageButton);
        progressBar = findViewById(R.id.progress_circular);

        makeOrderButton.setText("Make order for $" + totalPrice);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration itemDecorator = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.item_layout));
        recyclerView.addItemDecoration(itemDecorator);
        productDataList = new ArrayList<>();

        listAdapter = new OrderListAdapter(productDataList, this);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();

        listImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderActivity.this, ProductListActivity.class);
                startActivity(intent);
            }
        });

        searchImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }
        });

        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderActivity.this, AddProductActivity.class);
                startActivity(intent);
            }
        });

        myProfileImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderActivity.this, MyProfileActivity.class);
                startActivity(intent);
            }
        });

        addProductFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderActivity.this, ScanActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        makeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                Date date = new Date();
                String currentDate = formatter.format(date);

                OrderHistoryData orderHistoryData = new OrderHistoryData(currentDate, Integer.toString(totalPrice));
                mDatabaseReference.child("orders").child(SharedData.getPhoneNumber()).child(currentDate).setValue(orderHistoryData);

                for(int i = 0; i < productDataList.size(); i++) {
                    final String barcode = productDataList.get(i).getmBarcodeNumber();
                    String name = productDataList.get(i).getmProductName();
                    String price = productDataList.get(i).getmPrice();
                    final String pieces = productDataList.get(i).getmPieces();

                    OrderData orderData = new OrderData(name, price, pieces);
                    mDatabaseReference.child("orderDetails").child(SharedData.getPhoneNumber()).child(currentDate).child("products")
                            .child(barcode).setValue(orderData);

                    mDatabaseReference.child("products").child(SharedData.getPhoneNumber()).child(barcode)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    ProductData productData = snapshot.getValue(ProductData.class);
                                    int maxPieces = Integer.parseInt(productData.getmPieces());
                                    maxPieces = maxPieces - Integer.parseInt(pieces);
                                    mDatabaseReference.child("products").child(SharedData.getPhoneNumber())
                                            .child(barcode).child("mPieces").setValue(Integer.toString(maxPieces));
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                }
                productDataList.clear();
                recyclerView.setAdapter(listAdapter);
                numberOfProducts = 0;
                numberOfProductsTextView.setText(numberOfProducts + " products");
                totalPrice = 0;
                makeOrderButton.setText("Make order for $" + totalPrice);
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Order has been saved successfully!", Toast.LENGTH_SHORT).show();
            }

        });
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 0) {
            if(resultCode == CommonStatusCodes.SUCCESS) {
                if(data != null) {
                    Barcode barcode = data.getParcelableExtra("scannedCode");
                    final String barcodeNumber = barcode.displayValue;
                    SharedData.setBarcode(barcodeNumber);
                    scannedCode = barcodeNumber;
                    doesExist(barcodeNumber);
                } else {
                    Toast.makeText(getApplicationContext(),"Product with this barcode does not exist", Toast.LENGTH_LONG).show();
                }
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void doesExist(final String barcodeNumber) {

        mDatabaseReference.child("products").child(SharedData.getPhoneNumber()).child(barcodeNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ProductData productData = snapshot.getValue(ProductData.class);
                priceForOne = Integer.parseInt(productData.getmPrice());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if(productDataList.isEmpty()){
            mDatabaseReference.child("products").child(SharedData.getPhoneNumber()).child(barcodeNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ProductData productData = snapshot.getValue(ProductData.class);
                    SharedData.setMaxCount(Integer.parseInt(productData.getmPieces()));
                    openDialog();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            for(final ProductData productData: productDataList){
                if(productData.getmBarcodeNumber().equals(barcodeNumber)) {
                    exist = true;
                    countSetter(productData);
                    return;
                }
            }
            newProduct();
        }

    }

    private void newProduct() {
        mDatabaseReference.child("products").child(SharedData.getPhoneNumber()).child(scannedCode).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ProductData productData = snapshot.getValue(ProductData.class);
                SharedData.setMaxCount(Integer.parseInt(productData.getmPieces()));
                openDialog();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void countSetter(final ProductData productData) {
        mDatabaseReference.child("products").child(SharedData.getPhoneNumber()).child(scannedCode).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ProductData productData1 = snapshot.getValue(ProductData.class);
                int totalPieces = Integer.parseInt(productData1.getmPieces());
                int currentPieces = Integer.parseInt(productData.getmPieces());
                SharedData.setMaxCount(totalPieces - currentPieces);
                openDialog();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void openDialog() {
        EnterCountDialog enterCountDialog = new EnterCountDialog();
        enterCountDialog.show(getSupportFragmentManager(), "Enter number of products");
    }

    @Override
    public void setCount(final String pieces) {
        if(exist == true) {
            for(ProductData productData: productDataList) {
                if(scannedCode.equals(productData.getmBarcodeNumber())) {
                    int curPieces = Integer.parseInt(productData.getmPieces());
                    int piecesToAdd = Integer.parseInt(pieces);
                    productData.setmPieces(Integer.toString(curPieces + piecesToAdd));
                    productData.setmPrice(Integer.toString(priceForOne * (curPieces + piecesToAdd)));
                    totalPrice -= priceForOne * curPieces;
                    totalPrice += priceForOne * (curPieces + piecesToAdd);
                    makeOrderButton.setText("Make order for $" + totalPrice);
                    recyclerView.setAdapter(listAdapter);
                    exist = false;
                }
            }
        } else {
            mDatabaseReference.child("products").child(SharedData.getPhoneNumber()).child(scannedCode).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ProductData productData = snapshot.getValue(ProductData.class);
                    productData.setmPieces(pieces);
                    int piecesInt = Integer.parseInt(pieces);
                    String tPrice = Integer.toString(priceForOne * piecesInt);
                    productData.setmPrice(tPrice);
                    totalPrice += priceForOne * Integer.parseInt(pieces);
                    makeOrderButton.setText("Make order for $" + totalPrice);
                    productDataList.add(productData);
                    recyclerView.setAdapter(listAdapter);
                    numberOfProducts = productDataList.size();
                    if(numberOfProducts == 1){
                        numberOfProductsTextView.setText(numberOfProducts + " product");
                    } else {
                        numberOfProductsTextView.setText(numberOfProducts + " products");
                    }
                    exist = false;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }


    @Override
    public void onDelete(int position) {
        int price = Integer.parseInt(productDataList.get(position).getmPrice());
        totalPrice = totalPrice - price;
        productDataList.remove(position);
        makeOrderButton.setText("Make order for $" + totalPrice);
        numberOfProducts = productDataList.size();
        numberOfProductsTextView.setText(numberOfProducts + " products");
        listAdapter.notifyItemRemoved(position);
    }
}