package com.example.dykenapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private TextInputLayout mName, mSurname, mStateID;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mName = findViewById(R.id.name);
        mSurname = findViewById(R.id.surname);
        mStateID = findViewById(R.id.stateID);

        phoneNumber = getIntent().getStringExtra("phoneNumber");

        findViewById(R.id.buttonContinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFirebaseDatabase = FirebaseDatabase.getInstance();
                mDatabaseReference = mFirebaseDatabase.getReference("users");

                String name = mName.getEditText().getText().toString();
                String surname = mSurname.getEditText().getText().toString();
                String stateID = mStateID.getEditText().getText().toString();

                AddProfileData addDataOfUser = new AddProfileData(name, surname, stateID, phoneNumber);

                mDatabaseReference.push().setValue(addDataOfUser);

                Intent intent = new Intent(ProfileActivity.this, AddStore.class);
                startActivity(intent);
            }
        });
    }

}