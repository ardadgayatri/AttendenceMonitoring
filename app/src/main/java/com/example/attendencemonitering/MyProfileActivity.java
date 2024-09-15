package com.example.attendencemonitering;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.attendencemonitering.comman.Urls;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.jar.Attributes;
import java.util.prefs.Preferences;

import cz.msebera.android.httpclient.Header;

public class MyProfileActivity extends AppCompatActivity {

    ImageView ivProfilePhoto;
    AppCompatButton btnEditProfile,btnSignOut;
    TextView tvName,tvMobileNo,tvEmailId,tvUsername;

    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;
    SharedPreferences Preferences;
    String strUsername;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

       Preferences = PreferenceManager.getDefaultSharedPreferences(MyProfileActivity.this);

       strUsername= Preferences.getString("username","");

        ivProfilePhoto=findViewById(R.id.ivMyProfileProfilePhoto);
        btnEditProfile=findViewById(R.id.acbtnMyProfileEditProfile);
        tvName = findViewById(R.id.tvMyProfileName);
        tvMobileNo= findViewById(R.id.tvMyProfileMobileNo);
        tvEmailId= findViewById(R.id.tvMyProfileEmail);
        tvUsername= findViewById(R.id.tvMyProfileUsername);
        btnSignOut =findViewById(R.id.btnSignOut);

        googleSignInOptions = new  GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(MyProfileActivity.this, googleSignInOptions);

        GoogleSignInAccount  googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);

        if(googleSignInAccount != null)
        {
            String Name = googleSignInAccount.getDisplayName();
            String Email = googleSignInAccount.getEmail();

            tvName.setText(Name);
            tvEmailId.setText(Email);

            btnSignOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent intent = new Intent(MyProfileActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    });
                }
            });
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
         progressDialog= new ProgressDialog(MyProfileActivity.this);
         progressDialog.setTitle("My Profile");
         progressDialog.setMessage("Please Wait...");
         progressDialog.setCanceledOnTouchOutside(true);
         progressDialog.show();

         getMyDetails();
    }

    //  following code getmydetails for take Single data from Multiple data
    private void getMyDetails() {
        AsyncHttpClient client= new AsyncHttpClient();
        RequestParams params= new RequestParams();

        params.put("username",strUsername);

        client.post(Urls.getMyDetailsWebService,params, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                progressDialog.dismiss();
                try {
                    JSONArray jsonArray=response.getJSONArray("getMyDetails");
                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);

                        String strid= jsonObject.getString("id");
                        String strimage = jsonObject.getString("image");
                        String strName=jsonObject.getString("Name");
                        String strMobileno=jsonObject.getString("Mobileno");
                        String stremailid=jsonObject.getString("emailid");
                        String strusername=jsonObject.getString("username");

                        tvName.setText(strName);
                        tvMobileNo.setText(strMobileno);
                        tvEmailId.setText(stremailid);
                        tvUsername.setText(strusername);


                        //image load sathi
                            Glide.with(MyProfileActivity.this)
                                .load("http://192.168.253.107:80/AttendenceAPI/images/"+strimage) // Image URL or resource
                                    .skipMemoryCache(true)
                                .error(R.drawable.image_not_found) // Image to show if there's an error
                                .into(ivProfilePhoto); // Target ImageView
                    }


                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                progressDialog.dismiss();
                Toast.makeText(MyProfileActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
