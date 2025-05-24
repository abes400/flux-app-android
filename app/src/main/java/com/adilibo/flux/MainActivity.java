package com.adilibo.flux;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainActivity extends AppCompatActivity {
    int i = 0;
    FluxApp fluxApp;
    CardView noLamp;
    LampRVAdapter lampRVAdapter;

    private static final int BT_REQ_CODE = 0;
    private static final int BT_SETUP_REQ_CODE = 2;

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

        FloatingActionButton addDevice = findViewById(R.id.addDevice);
        addDevice.setOnClickListener(v -> regLampTemp());


        fetchLampDataByPermission();

        showHideNoLamp();
        lampRVAdapter.notifyDataSetChanged();

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onResume() {
        super.onResume();
        fluxApp.currentAct = 0;
        lampRVAdapter.notifyDataSetChanged();
        showHideNoLamp();
    }

    // I hate violating DRY, but no other choice for now.
    // TODO: FIX IT ASAP
    @Override
    protected void onStop() {
        super.onStop();
        fluxApp.saveLampData();
    }

    void showHideNoLamp() {
        if(fluxApp.getLampCount() == 0)
            noLamp.setVisibility(CardView.VISIBLE);
        else noLamp.setVisibility(CardView.GONE);
    }

    @SuppressLint("NotifyDataSetChanged")
    void regLampTemp() {
        if(fluxApp.getLampCount() == 0)
            noLamp.setVisibility(CardView.GONE);
        fluxApp.registerLamp(new LampRVModel(getString(R.string.lamp) + " " + i, ""+i,"FFFFFFFF",  i%2==0, i%4==0));
        i = fluxApp.getLampCount();
        lampRVAdapter.notifyDataSetChanged();
    }

    private void fetchLampDataByPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { // for 12+
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN},
                        BT_REQ_CODE);
            } else fluxApp.loadLampData();
        } else fluxApp.loadLampData();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == BT_REQ_CODE) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                fluxApp.loadLampData();
            else {
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle(R.string.bt_popup_title)
                        .setCancelable(false)
                        .setPositiveButton(R.string.bt_settings, (dialogInterface, i) -> {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                            startActivityForResult(intent, BT_SETUP_REQ_CODE);
                        })
                        .setNegativeButton(R.string.exit, (inte, i) -> finish())
                        .create();
                dialog.show();
                dialog.getButton(dialog.BUTTON_NEGATIVE).setTextColor(getColor(R.color.secondary));
                dialog.getButton(dialog.BUTTON_POSITIVE).setTextColor(getColor(R.color.secondary));
            }

        }
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == BT_SETUP_REQ_CODE) {
            fetchLampDataByPermission();
        }

    }

}