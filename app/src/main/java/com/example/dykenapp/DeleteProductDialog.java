package com.example.dykenapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DeleteProductDialog extends AppCompatDialogFragment {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    private DeleteProductDialog.DeleteProductDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.delete_product_dialog, null);

        builder.setView(view)
                .setTitle("Delete this product?")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mFirebaseDatabase = FirebaseDatabase.getInstance();
                        mDatabaseReference = mFirebaseDatabase.getReference("products");

                        mDatabaseReference.child(SharedData.getPhoneNumber()).child(SharedData.getBarcode()).removeValue();

                        Toast.makeText(getActivity(), "Data have been deleted successfully!", Toast.LENGTH_SHORT).show();

                        listener.completeDelete();
                    }
                });

        return builder.create();
    }

    public interface DeleteProductDialogListener{
        void completeDelete();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (DeleteProductDialog.DeleteProductDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "implement interface first");
        }
    }

}
