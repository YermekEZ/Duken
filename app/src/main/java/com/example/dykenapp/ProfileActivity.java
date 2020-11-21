package com.example.dykenapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private TextInputLayout mName, mSurname, mStateID;
    private TextInputEditText name, surname, stateID;
    private Button buttonContinue;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mName = findViewById(R.id.name);
        mSurname = findViewById(R.id.surname);
        mStateID = findViewById(R.id.stateID);
        buttonContinue = findViewById(R.id.buttonContinue);

        name = findViewById(R.id.nameEditText);
        surname = findViewById(R.id.surnameEditText);
        stateID = findViewById(R.id.stateIDEditText);

        phoneNumber = getIntent().getStringExtra("phoneNumber");

        name.addTextChangedListener(profileData);
        surname.addTextChangedListener(profileData);
        stateID.addTextChangedListener(profileData);

        buttonContinue.setTextColor(Color.parseColor("#A2AAC5"));
        buttonContinue.setBackgroundResource(R.color.inactiveButton);
        buttonContinue.setEnabled(false);

        buttonContinue.setOnClickListener(new View.OnClickListener() {
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

    private TextWatcher profileData = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String nameInput = name.getText().toString().trim();
            String surnameInput = surname.getText().toString().trim();
            String stateIDInput = stateID.getText().toString().trim();

            if(nameInput.isEmpty() == false && surnameInput.isEmpty() == false && stateIDInput.isEmpty() == false) {

                buttonContinue.setBackgroundResource(R.color.activeButton);
                buttonContinue.setTextColor(Color.parseColor("#FFFFFF"));
                buttonContinue.setEnabled(true);

            }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

}