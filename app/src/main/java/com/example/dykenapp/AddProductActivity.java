package com.example.dykenapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddProductActivity extends AppCompatActivity {

    private ImageButton searchImageButton, myProfileImageButton, listImageButton, scan, orderImageButton;
    private Button saveButton;
    private TextInputLayout mProductName, mScanResult, mPrice, mPieces;
    private TextInputEditText productName, scanResult, price, pieces;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        searchImageButton = findViewById(R.id.scanImageButton);
        myProfileImageButton = findViewById(R.id.myProfileImageButton);
        listImageButton = findViewById(R.id.listImageButton);
        scan = findViewById(R.id.scan);
        orderImageButton = findViewById(R.id.orderImageButton);
        saveButton = findViewById(R.id.saveButton);
        mProductName = findViewById(R.id.productName);
        mScanResult = findViewById(R.id.scanResult);
        mPrice = findViewById(R.id.price);
        mPieces = findViewById(R.id.pieces);
        productName = findViewById(R.id.productNameEditText);
        scanResult = findViewById(R.id.scanResultEditText);
        price = findViewById(R.id.priceEditText);
        pieces = findViewById(R.id.piecesEditText);

        saveButton.setBackgroundResource(R.color.activeButton);
        saveButton.setTextColor(Color.parseColor("#FFFFFF"));

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddProductActivity.this, ScanActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        searchImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddProductActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }
        });

        myProfileImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddProductActivity.this, MyProfileActivity.class);
                startActivity(intent);
            }
        });

        listImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddProductActivity.this, ProductListActivity.class);
                startActivity(intent);
            }
        });

        orderImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddProductActivity.this, OrderActivity.class);
                startActivity(intent);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFirebaseDatabase = FirebaseDatabase.getInstance();
                mDatabaseReference = mFirebaseDatabase.getReference("products");

                String productName = mProductName.getEditText().getText().toString();
                String scannedNumber = mScanResult.getEditText().getText().toString();
                String price = mPrice.getEditText().getText().toString();
                String pieces = mPieces.getEditText().getText().toString();

                ProductData addData = new ProductData(productName, scannedNumber, price, pieces);

                mDatabaseReference.child(SharedData.getPhoneNumber()).child(scannedNumber).setValue(addData);

                Toast.makeText(getApplicationContext(), "Data have been saved successfully!", Toast.LENGTH_SHORT).show();
                mProductName.getEditText().getText().clear();
                mPrice.getEditText().getText().clear();
                mScanResult.getEditText().getText().clear();
                mPieces.getEditText().getText().clear();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 0) {
            if(resultCode == CommonStatusCodes.SUCCESS) {
                if(data != null) {
                    Barcode barcode = data.getParcelableExtra("scannedCode");
                    mScanResult.getEditText().setText(barcode.displayValue);
                } else {
                    mScanResult.getEditText().setText("No code found");
                }
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}