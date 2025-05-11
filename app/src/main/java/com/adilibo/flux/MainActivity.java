package com.adilibo.flux;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FluxApp fluxApp;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fluxApp = (FluxApp) getApplication();

        RecyclerView lampRecyclerView = findViewById(R.id.lampRecyclerView);
        LampRVAdapter lampRVAdapter = new LampRVAdapter(this, fluxApp);

        Button test = findViewById(R.id.test);

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fluxApp.registerLamp(new LampRVModel("Lamp " + i, ""+i,"#000000",  i%2==0, i%4==0));
                i++;
                lampRVAdapter.notifyDataSetChanged();
            }
        });

        lampRecyclerView.setAdapter(lampRVAdapter);

        lampRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}