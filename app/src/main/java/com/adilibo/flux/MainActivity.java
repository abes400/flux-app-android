package com.adilibo.flux;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.widget.Toolbar;


public class MainActivity extends AppCompatActivity {
    FluxApp fluxApp;
    LampRVAdapter lampRVAdapter;
    CardView noLamp;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fluxApp = (FluxApp) getApplication();
        lampRVAdapter = new LampRVAdapter(this, fluxApp);

        Toolbar toolbarHome = findViewById(R.id.toolbar_home);
        toolbarHome.setOnLongClickListener(v -> {
            regLampTemp();
            return true;
        });

        Button about = findViewById(R.id.about);
        about.setOnClickListener( v -> {
            Intent intent = new Intent(v.getContext(), About.class);
            v.getContext().startActivity(intent);
        });

        noLamp = findViewById(R.id.no_lamp);
        noLamp.setOnClickListener(v -> regLampTemp());

        RecyclerView lampRecyclerView = findViewById(R.id.lampRecyclerView);
        lampRecyclerView.setAdapter(lampRVAdapter);
        lampRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        showHideNoLamp();
    }

    @Override
    protected void onResume() {
        super.onResume();
        lampRVAdapter.notifyDataSetChanged();
        showHideNoLamp();
    }

    void showHideNoLamp() {
        if(fluxApp.getLampCount() == 0)
            noLamp.setVisibility(CardView.VISIBLE);
        else noLamp.setVisibility(CardView.GONE);
    }

    void regLampTemp() {
        if(fluxApp.getLampCount() == 0)
            noLamp.setVisibility(CardView.GONE);
        fluxApp.registerLamp(new LampRVModel("Lamp " + i, ""+i,"FFFFFFFF",  i%2==0, i%4==0));
        i = fluxApp.getLampCount();
        lampRVAdapter.notifyDataSetChanged();
    }
}