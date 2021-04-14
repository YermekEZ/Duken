package com.example.dykenapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProductDialog extends AppCompatDialogFragment {

    private TextInputLayout productName, barcode, price, pieces;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.edit_product_layout, null);

        builder.setView(view)
                .setTitle("Edit product details")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Save changes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        editData();

                    }
                });

        productName = view.findViewById(R.id.productName);
        barcode = view.findViewById(R.id.barcodeNumber);
        price = view.findViewById(R.id.price);
        pieces = view.findViewById(R.id.pieces);

        productName.getEditText().setText(SharedData.getProductName());
        barcode.getEditText().setText(SharedData.getBarcode());
        price.getEditText().setText(SharedData.getPrice());
        pieces.getEditText().setText(SharedData.getPieces());

        return builder.create();
    }

    private void editData() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("products");

        String mProductName = productName.getEditText().getText().toString();
        String mBarcode = barcode.getEditText().getText().toString();
        String mPrice = price.getEditText().getText().toString();
        String mPieces = pieces.getEditText().getText().toString();

        ProductData addData = new ProductData(mProductName, mBarcode, mPrice, mPieces);

        mDatabaseReference.child(SharedData.getPhoneNumber()).child(SharedData.getBarcode()).setValue(addData);

        Toast.makeText(getActivity(), "Data have been saved successfully!", Toast.LENGTH_SHORT).show();
    }
}
