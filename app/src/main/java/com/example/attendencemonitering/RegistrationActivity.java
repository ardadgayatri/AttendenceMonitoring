package com.example.attendencemonitering;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.attendencemonitering.LoginActivity;
import com.example.attendencemonitering.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;

public class RegistrationActivity extends AppCompatActivity {


    EditText etName,etMobileNo,etEmailId,etUsername,etPassword;
    Button btnRegister;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setTitle("RegistrationActivity");

        preferences = PreferenceManager.getDefaultSharedPreferences(RegistrationActivity.this);
        editor = preferences.edit();

        etName = findViewById(R.id.etRegisterName);
        etMobileNo = findViewById(R.id.etRegisterMobileNO);
        etEmailId = findViewById(R.id.etRegisterEmailId);
        etUsername = findViewById(R.id.etaRegisterUsername);
        etPassword = findViewById(R.id.etaRegisterPassword);
        btnRegister =findViewById(R.id.btnRegisterRegister);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etName.getText().toString().isEmpty())
                {
                    etName.setError("Please enter Your Name");
                } else if (etMobileNo.getText().toString().isEmpty()){
                    etMobileNo.setError("Please enter your Mobile number");
                }else if (etMobileNo.getText().toString().length()  !=10) {
                    etMobileNo.setError("Invalid Mobile number");
                }
                else if (etEmailId.getText().toString().isEmpty())
                {
                    etEmailId.setError("Please enter your Email ID");
                }else if (!etEmailId.getText().toString().contains("@")||
                        !etEmailId.getText().toString().contains(".com"))
                {
                    etEmailId.setError("Please enter valid Email ID");
                } else if (etUsername.getText().toString().isEmpty()) {
                    etUsername.setError("Please enter your Username");
                }else if (etUsername.getText().toString().length()<8) {
                    etUsername.setError(" Username must be greater 8");
                }
                else if (etUsername.getText().toString().matches(".* [A-Z]*.")) {
                    etUsername.setError("Please enter Atleast 1 Uppercase letter");
                }else if (etUsername.getText().toString().matches(".* [a-z]*.")) {
                    etUsername.setError("Please enter Atleast 1 Lowercase letter");
                }else if (etUsername.getText().toString().matches(".* [0-9]*.")) {
                    etUsername.setError("Please enter Atleast 1 number");
                }else if (etUsername.getText().toString().matches(".* [@,#,$,%,!]*.")) {
                    etUsername.setError("Please enter Atleast 1 special symbol");
                }else if (etPassword.getText().toString().isEmpty()) {
                    etPassword.setError("Please enter YOUR password");
                }else if (etPassword.getText().toString().length()<8) {
                    etPassword.setError("Password must be greater than 8");
                }
                else
                {
                    progressDialog = new ProgressDialog(RegistrationActivity.this);
                    progressDialog.setTitle("Please Wait...");
                    progressDialog.setMessage("Registration is in process");
                    progressDialog.setCanceledOnTouchOutside(true);
                    progressDialog.show();

                    // PhoneAuthProvider class is use to verify mobile number
                    //arg 1= jya mobile number varti otp receive karaycha aahe
                    // arg 2 = 60
                    // arg 3 = time units
                    //arg 4  = current java
                   // arg 5 = callback ( verification fail or complete)

                    PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + etMobileNo.getText().toString(),
                            60, TimeUnit.SECONDS, RegistrationActivity.this,
                            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                @Override
                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                    Toast.makeText(RegistrationActivity.this,"Verification Completed",Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }

                                @Override
                                public void onVerificationFailed(@NonNull FirebaseException e) {
                                    Toast.makeText(RegistrationActivity.this,"Verification Failed",Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }

                                @Override
                                public void onCodeSent(@NonNull String verificationCode, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                    // intent is use to jump one activity to another activity
                                    Intent intent = new Intent(RegistrationActivity.this, VerifyOTPActivity.class);
                                    intent.putExtra("verificationCode",verificationCode); //kay -> string,value
                                    intent.putExtra("Name",etName.getText().toString());
                                    intent.putExtra("Mobileno",etMobileNo.getText().toString());
                                    intent.putExtra("emailid",etEmailId.getText().toString());
                                    intent.putExtra("username",etUsername.getText().toString());
                                    intent.putExtra("password",etPassword.getText().toString());
                                    startActivity(intent);

                                }
                            });
                }

            }

        });
    }
}
