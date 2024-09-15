package com.example.attendencemonitering;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.attendencemonitering.comman.Urls;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;

public class VerifyOTPActivity extends AppCompatActivity {

    TextView tvMobileno,tvResendOTP;
    EditText etInputCode1,etInputCode2,etInputCode3,etInputCode4,etInputCode5,etInputCode6;

    AppCompatButton btnVerify;
    ProgressDialog progressDialog;

    private String strVerificationCode,strName,strMobileno,strEmailId,strUsername,strPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otpactivity);

        tvMobileno = findViewById(R.id.tvVerifyOTPMobileno);
        tvResendOTP = findViewById(R.id.tvVerifyOTPResendOTP);
        etInputCode1 = findViewById(R.id.etVerifyOTPInputCode1);
        etInputCode2 = findViewById(R.id.etVerifyOTPInputCode2);
        etInputCode3 = findViewById(R.id.etVerifyOTPInputCode3);
        etInputCode4 = findViewById(R.id.etVerifyOTPInputCode4);
        etInputCode5 = findViewById(R.id.etVerifyOTPInputCode5);
        etInputCode6 = findViewById(R.id.etVerifyOTPInputCode6);
        btnVerify = findViewById(R.id.acbtnVerifyOTPVerify);

        strVerificationCode = getIntent().getStringExtra("verificationCode");// firebase ne takalela OTP number ithe asto
        strName = getIntent().getStringExtra("Name");
        strMobileno = getIntent().getStringExtra("Mobileno");
        strEmailId= getIntent().getStringExtra("emailid");
        strUsername = getIntent().getStringExtra("username");
        strPassword = getIntent().getStringExtra("password");

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etInputCode1.getText().toString().trim().isEmpty() || etInputCode2.getText().toString().trim().isEmpty() ||
                        etInputCode3.getText().toString().trim().isEmpty() || etInputCode4.getText().toString().trim().isEmpty()||
                        etInputCode5.getText().toString().trim().isEmpty() || etInputCode6.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(VerifyOTPActivity.this,"Please Enter Valid OTP",Toast.LENGTH_SHORT).show();

                }
                // alela otp number verification karun otpcose madhe takla
                // user ne taklela otp number otpcode madhe store aahe
                String otpcode= etInputCode1.getText().toString()+etInputCode2.getText().toString()+etInputCode3.getText().toString()+
                        etInputCode4.getText().toString()+etInputCode5.getText().toString()+etInputCode6.getText().toString();

                if (strVerificationCode!=null)
                {
                    progressDialog= new ProgressDialog(VerifyOTPActivity.this);
                    progressDialog.setTitle("Verifying OTP");
                    progressDialog.setMessage("Please Wait...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    // firebase kadun alela otp &user ne taklela otp phoneAutoProvider store karto
                     PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                            strVerificationCode,otpcode);

                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                progressDialog.dismiss();
                                userRegisterDetails();
                            }
                            else
                            {
                                progressDialog.dismiss();
                                Toast.makeText(VerifyOTPActivity.this,"OTP Verification Failed",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }


            }
        });

        tvResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" +strMobileno,
                        60, TimeUnit.SECONDS, VerifyOTPActivity.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                Toast.makeText(VerifyOTPActivity.this,"Verification Completed",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(VerifyOTPActivity.this,"Verification Failed",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String newverificationCode, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                               strVerificationCode=newverificationCode;
                            }
                        });
            }
        });
        setupInputOTP();
    }

        private void userRegisterDetails() {
            AsyncHttpClient client = new AsyncHttpClient();// client and server communication
            RequestParams params = new RequestParams();   //put the data

            params.put("Name", strName);
            params.put("Mobileno", strMobileno);
            params.put("emailid", strEmailId);
            params.put("username", strUsername);
            params.put("password", strPassword);

            client.post(Urls.UserRegisterDetailsWebService, params, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);

                            try {
                                String status = response.getString("sucess");
                                if (status.equals("1")) {
                                    progressDialog.dismiss();
                                    Toast.makeText(VerifyOTPActivity.this, "Registration successfully Done", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(VerifyOTPActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(VerifyOTPActivity.this, "Already Data Present", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            progressDialog.dismiss();
                            Toast.makeText(VerifyOTPActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                        }
                    }
            );
        }

    private void setupInputOTP() {
        etInputCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //trim = perticular word chi pahili space & nantr chi space remove karto
                if(!s.toString().trim().isEmpty())
                {
                    etInputCode2.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        //addTextChangedListener = it is check user change or edit information in Edit Text
        etInputCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty())
                {
                    etInputCode3.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etInputCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override       //CharSequence s =  perticular OTP Number saves
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty())
                {
                    etInputCode4.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etInputCode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override       //CharSequence s =  perticular OTP Number saves
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty())
                {
                    etInputCode5.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etInputCode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override       //CharSequence s =  perticular OTP Number saves
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty())
                {
                    etInputCode6.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



    }
}
