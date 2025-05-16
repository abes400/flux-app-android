package com.adilibo.flux;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class About extends AppCompatActivity {
    int i = 0;
    final int MAX_COUNT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Button backAbt = findViewById(R.id.back_ABT);
        backAbt.setOnClickListener(v -> finish());

        Button erbaa  = findViewById(R.id.erbaa);
        erbaa.setOnLongClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://tr.wikipedia.org/wiki/Erbaa"));
            startActivity(browserIntent);
            erbaa.setVisibility(View.GONE);
            return true;
        });

        Button github = findViewById(R.id.github);
        github.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://github.com/abes400/flux-app-android"));
            startActivity(browserIntent);
        });
        github.setOnLongClickListener(v -> {
            i++;
            if(i == MAX_COUNT) erbaa.setVisibility(View.VISIBLE);
            return true;
        });

    }
}