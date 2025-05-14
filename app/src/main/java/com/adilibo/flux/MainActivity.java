package com.adilibo.flux;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {
    FluxApp fluxApp;
    LampRVAdapter lampRVAdapter;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fluxApp = (FluxApp) getApplication();

        RecyclerView lampRecyclerView = findViewById(R.id.lampRecyclerView);
        lampRVAdapter = new LampRVAdapter(this, fluxApp);

        Button test = findViewById(R.id.test);
        Button about = findViewById(R.id.about);

        test.setOnClickListener(v -> {
            fluxApp.registerLamp(new LampRVModel("Lamp " + i, ""+i,"FFFFFFFF",  i%2==0, i%4==0));
            i++;
            lampRVAdapter.notifyDataSetChanged();
        });

        about.setOnClickListener( v -> {
            Intent intent = new Intent(v.getContext(), About.class);
            v.getContext().startActivity(intent);
        });

        lampRecyclerView.setAdapter(lampRVAdapter);

        lampRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        lampRVAdapter.notifyDataSetChanged();
    }
}