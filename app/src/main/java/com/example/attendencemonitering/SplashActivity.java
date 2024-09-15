package com.example.attendencemonitering;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {


    ImageView ivLogo;
    TextView tvTitle;
    Animation fadeInAnim;
     Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ivLogo = findViewById(R.id.ivsplashLogo);
        tvTitle = findViewById(R.id.tvSplashTitle);

        fadeInAnim= AnimationUtils.loadAnimation(SplashActivity.this,R.anim.fadein);
        ivLogo.startAnimation(fadeInAnim);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i= new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(i);
            }
        },3000);

    }
}