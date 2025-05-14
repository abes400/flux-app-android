package com.adilibo.flux;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Button backAbt = findViewById(R.id.back_ABT);
        backAbt.setOnClickListener(v -> finish());

        Button github = findViewById(R.id.github);
        Button erbaa  = findViewById(R.id.erbaa);

        github.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/abes400/flux-app-android"));
            startActivity(browserIntent);
        });

        erbaa.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://tr.wikipedia.org/wiki/Erbaa"));
            startActivity(browserIntent);
        });
    }
}