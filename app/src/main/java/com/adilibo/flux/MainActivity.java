package com.adilibo.flux;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<LampRVModel> lamps = new ArrayList<>();
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView lampRecyclerView = findViewById(R.id.lampRecyclerView);
        LampRVAdapter lampRVAdapter = new LampRVAdapter(this, lamps);

        Button test = findViewById(R.id.test);

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lamps.add(new LampRVModel("Lamp " + i, ""+i, i, 2*i, 3 * i, i%2==0, i%4==0));
                i++;
                lampRVAdapter.notifyDataSetChanged();
            }
        });

        lampRecyclerView.setAdapter(lampRVAdapter);

        lampRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}