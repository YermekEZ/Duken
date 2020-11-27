package com.example.dykenapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddStoreActivity extends AppCompatActivity{

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private Spinner spinnerCities;
    private String selectedCity;
    private Button buttonContinue;
    private String phoneNumber;
    private TextInputLayout mIIN, mLegalName, mAddress;
    private TextInputEditText IIN, legalName, address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_store);

        spinnerCities = findViewById(R.id.spinnerCities);

        phoneNumber = getIntent().getStringExtra("phoneNumber");

        mIIN = findViewById(R.id.IIN);
        mLegalName = findViewById(R.id.legalName);
        mAddress = findViewById(R.id.address);

        IIN = findViewById(R.id.IINEditText);
        legalName = findViewById(R.id.legalNameEditText);
        address = findViewById(R.id.addressEditText);

        buttonContinue = findViewById(R.id.buttonContinue);
        buttonContinue.setTextColor(Color.parseColor("#A2AAC5"));
        buttonContinue.setBackgroundResource(R.color.inactiveButton);
        buttonContinue.setEnabled(false);

        IIN.addTextChangedListener(addStore);
        legalName.addTextChangedListener(addStore);
        address.addTextChangedListener(addStore);

        ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(this, R.array.cities, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerCities.setAdapter(adapter);

        spinnerCities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String[] cities = getResources().getStringArray(R.array.cities);
                selectedCity = cities[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFirebaseDatabase = FirebaseDatabase.getInstance();
                mDatabaseReference = mFirebaseDatabase.getReference("stores");

                String IIN = mIIN.getEditText().getText().toString();
                String legalName = mLegalName.getEditText().getText().toString();
                String address = mAddress.getEditText().getText().toString();
                String city = selectedCity;

                AddStoreData addData = new AddStoreData(phoneNumber, IIN, legalName, address, city);

                mDatabaseReference.push().setValue(addData);

                Intent intent = new Intent(AddStoreActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }
        });


    }

    private TextWatcher addStore = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            
            String IINInput = IIN.getText().toString().trim();
            String legalNameInput = legalName.getText().toString().trim();
            String addressInput = address.getText().toString().trim();

            if(IINInput.isEmpty() == false && legalNameInput.isEmpty() == false && addressInput.isEmpty() == false) {

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