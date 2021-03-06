package com.example.dykenapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class VerifyPhoneSignInActivity extends AppCompatActivity {

    private String mVerificationID;

    private EditText editTextCode;
    private TextView textViewEnterCode, resendCodeTextView;
    private Button nextButton, resendCodeButton;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone_sign_in);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        editTextCode = findViewById(R.id.editTextCode);
        textViewEnterCode = findViewById(R.id.textViewEnterCode);
        resendCodeTextView = findViewById(R.id.resendCodeTextView);
        resendCodeButton = findViewById(R.id.resendCodeButton);

        startTimer();


        //getting mobile number from the previous activity
        //and sending the verification code to the number
        phoneNumber = getIntent().getStringExtra("phoneNumber");

        //appending phoneNumber to textViewEnterCode
        textViewEnterCode.append(phoneNumber);
        sendVerificationCode(phoneNumber);


        //if the automatic sms detection did not work, user can also enter the code manually
        //so adding a click listener to the button
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = editTextCode.getText().toString().trim();
                if (code.isEmpty() || code.length() < 6) {
                    editTextCode.setError("Invalid code");
                    editTextCode.requestFocus();
                    return;
                }

                //verifying the code entered manually
                verifyVerificationCode(code);
            }
        });

        resendCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendVerificationCode(phoneNumber);
                startTimer();
                resendCodeButton.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void startTimer() {
        new CountDownTimer(60000, 1000){

            @Override
            public void onTick(long l) {
                resendCodeTextView.setText("You can resend code in " + l / 1000 + " seconds");
            }

            @Override
            public void onFinish() {
                resendCodeTextView.setText("");
                resendCodeButton.setVisibility(View.VISIBLE);

            }
        }.start();
    }

    //the method is sending verification code
    //the country id is concatenated
    //you can take the country id as user input as well
    private void sendVerificationCode(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                TaskExecutors.MAIN_THREAD,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }


    //the callback to detect the verification status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            mVerificationID = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String smsCode = phoneAuthCredential.getSmsCode();
            if(smsCode != null){
                verifyVerificationCode(smsCode);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(VerifyPhoneSignInActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };


    private void verifyVerificationCode(String codeByUser) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationID, codeByUser);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(VerifyPhoneSignInActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //verification successful we will start the profile activity
                            SharedData.phoneNumber = phoneNumber;
                            Intent intent = new Intent(VerifyPhoneSignInActivity.this, AddProductActivity.class);
                            intent.putExtra("phoneNumber", phoneNumber);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        } else {

                            //verification unsuccessful.. display an error message
                            Toast.makeText(VerifyPhoneSignInActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
}