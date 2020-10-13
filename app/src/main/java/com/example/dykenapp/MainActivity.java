package com.example.dykenapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity{

    private EditText editTextPhoneNumber;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextPhoneNumber = findViewById(R.id.editTextPhone);

        //mFirebaseAuth = FirebaseAuth.getInstance();

        findViewById(R.id.nextButton).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                //mFirebaseDatabase = FirebaseDatabase.getInstance();
                String phoneNumber = editTextPhoneNumber.getText().toString();

                /*if(phoneNumber.isEmpty() || phoneNumber.length() < 10){
                    editTextPhoneNumber.setError("Invalid phone number");
                    editTextPhoneNumber.requestFocus();
                    return;
                }*/

                Intent intent = new Intent(MainActivity.this, VerifyPhoneActivity.class);
                intent.putExtra("phoneNumber", phoneNumber);
                startActivity(intent);
            }
        });

    }

    /*protected void onStart() {
        super.onStart();

        if (mFirebaseAuth.getCurrentUser() != null) {
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
        }
    }*/

    /*ublic void sendPhoneNumber(View view) {
        String phoneNumber = editTextPhoneNumber.getText().toString();

        if(phoneNumber.isEmpty() || phoneNumber.length() < 10){
            editTextPhoneNumber.setError("Invalid phone number");
            editTextPhoneNumber.requestFocus();
            return;
        }

        Intent intent = new Intent(MainActivity.this, VerifyPhoneActivity.class);
        intent.putExtra("phoneNumber", phoneNumber);
        startActivity(intent);
    }*/
}