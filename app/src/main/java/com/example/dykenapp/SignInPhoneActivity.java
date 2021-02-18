package com.example.dykenapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInPhoneActivity extends AppCompatActivity {

    private EditText editTextPhoneNumber;
    private TextView textView, errorMessage;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_phone);

        textView = findViewById(R.id.textView);
        errorMessage = findViewById(R.id.errorMessage);
        editTextPhoneNumber = findViewById(R.id.editTextPhone);

        editTextPhoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        //mFirebaseAuth = FirebaseAuth.getInstance();

        findViewById(R.id.nextButton).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                //mFirebaseDatabase = FirebaseDatabase.getInstance();
                final String phoneNumber = editTextPhoneNumber.getText().toString();

                /*if(phoneNumber.isEmpty() || phoneNumber.length() < 10){
                    editTextPhoneNumber.setError("Invalid phone number");
                    editTextPhoneNumber.requestFocus();
                    return;
                }*/

                mFirebaseDatabase = FirebaseDatabase.getInstance();
                mDatabaseReference = mFirebaseDatabase.getReference().child("users").child(phoneNumber);
                mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Intent intent = new Intent(SignInPhoneActivity.this, VerifyPhoneSignInActivity.class);
                            intent.putExtra("phoneNumber", phoneNumber);
                            startActivity(intent);
                        } else {
                            errorMessage.setText("Invalid phone number");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }
}