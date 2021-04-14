package com.example.dykenapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

public class MainMenuActivity extends AppCompatActivity {

    //private BottomNavigationView bottomNavigationView;
    private TextView scanResult, productNameResult, priceResult, inStockResult;
    private Button scanButton;
    private ImageButton addImageButton, searchImageButton, myProfileImageButton, listImageButton, editButton;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private String barcodeValue, productValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        //bottomNavigationView = findViewById(R.id.bottomNavigationView);
        scanResult = findViewById(R.id.scanResult);
        productNameResult = findViewById(R.id.productNameResult);
        priceResult = findViewById(R.id.priceResult);
        inStockResult = findViewById(R.id.inStockResult);
        scanButton = findViewById(R.id.scanButton);
        addImageButton = findViewById(R.id.addImageButton);
        searchImageButton = findViewById(R.id.searchImageButton);
        myProfileImageButton = findViewById(R.id.myProfileImageButton);
        listImageButton = findViewById(R.id.listImageButton);
        editButton = findViewById(R.id.editButton);

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, ScanActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, AddProductActivity.class);
                startActivity(intent);
            }
        });

        myProfileImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, MyProfileActivity.class);
                startActivity(intent);
            }
        });

        listImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, ProductListActivity.class);
                startActivity(intent);
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

    }

    public void readName(final String barcodeNumber) {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("products");

        mDatabaseReference.child(SharedData.getPhoneNumber()).child(barcodeNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ProductData productData = snapshot.getValue(ProductData.class);
                String mProductName = productData.getmProductName();
                productNameResult.setText(mProductName);
                String mPrice = productData.getmPrice();
                priceResult.setText(mPrice);
                String mInStock = productData.getmPieces();
                inStockResult.setText(mInStock);
                SharedData.setProductName(mProductName);
                SharedData.setBarcode(barcodeNumber);
                SharedData.setPrice(mPrice);
                SharedData.setPieces(mInStock);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        editButton.setVisibility(View.VISIBLE);
    }

    private void openDialog() {
        EditProductDialog editProductDialog = new EditProductDialog();
        editProductDialog.show(getSupportFragmentManager(), "Edit product details");
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 0) {
            if(resultCode == CommonStatusCodes.SUCCESS) {
                if(data != null) {
                    Barcode barcode = data.getParcelableExtra("scannedCode");
                    String details = barcode.displayValue;
                    scanResult.setText(details);
                    readName(details);
                } else {
                    scanResult.setText("No code found");
                }
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}