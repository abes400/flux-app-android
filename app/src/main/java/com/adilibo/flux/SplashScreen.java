package com.adilibo.flux;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);

        Handler handler = new Handler();
        ImageView logo = findViewById(R.id.logo_SS);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);


        handler.postDelayed((Runnable) () -> {
            logo.startAnimation(fadeIn);
        }, 100);
        handler.postDelayed((Runnable) () -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }, 1500);

    }
}