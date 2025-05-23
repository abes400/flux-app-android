package com.adilibo.flux;

import android.app.Application;
import android.bluetooth.BluetoothDevice;
import android.content.SharedPreferences;
import android.widget.Toast;
//import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;

import com.harrysoft.androidbluetoothserial.BluetoothManager;


public class FluxApp extends Application {
    ArrayList<LampRVModel> _lamps;

    SharedPreferences sPrefs;
    SharedPreferences.Editor sPrefEdit;

    @Override
    public void onCreate() {
        super.onCreate();
        sPrefs = getSharedPreferences("LAMP_METADATA", MODE_PRIVATE);
        sPrefEdit = sPrefs.edit();

        _lamps = new ArrayList<>();

    }

    public synchronized void registerLamp(LampRVModel newLamp) {_lamps.add(newLamp);}
    public synchronized LampRVModel getLampAt(int index) {return _lamps.get(index);}
    public synchronized int getLampCount() {return _lamps.size();}
    public synchronized boolean removeLampAt(int index) {
        if(index >= 0 && index < _lamps.size()){
            _lamps.remove(index);
            return true;
        }
        return false;
    }

    public synchronized  void saveOneLamp(int index) {
        LampRVModel lamp = _lamps.get(index);
        String addr = lamp.getAddress();
        sPrefEdit.putString(addr + "_nickname", lamp.getName());
        sPrefEdit.putString(addr + "_hex", lamp.hexStr);
        sPrefEdit.putBoolean(addr + "_auto", lamp.auto_brightness);
        sPrefEdit.putBoolean(addr + "_on_off", lamp.isOn);
        sPrefEdit.apply();
    }

    public synchronized void saveLampData() {
        sPrefEdit.clear();

        for(LampRVModel lamp : _lamps) {
            String addr = lamp.getAddress();
            sPrefEdit.putString(addr + "_nickname", lamp.getName());
            sPrefEdit.putString(addr + "_hex", lamp.hexStr);
            sPrefEdit.putBoolean(addr + "_auto", lamp.auto_brightness);
            sPrefEdit.putBoolean(addr + "_on_off", lamp.isOn);
        }

        sPrefEdit.apply();
    }

    public synchronized void loadLampData() {
        BluetoothManager btMgr = BluetoothManager.getInstance();
        if (btMgr == null) {
            Toast.makeText(this, "BT not supported", Toast.LENGTH_SHORT).show();
            return;
        }

        Collection <BluetoothDevice> pairedDevices = btMgr.getPairedDevicesList();

        for(BluetoothDevice device : pairedDevices) {
            String address = device.getAddress();
            String name = device.getName();

            _lamps.add(new LampRVModel(
                    sPrefs.getString(address + "_nickname", "NEWLAMP"),
                    address,
                    sPrefs.getString(address + "_hex", "FFFFFFFF"),
                    sPrefs.getBoolean(address + "_auto", false),
                    sPrefs.getBoolean(address + "_on_off", false)
            ));
        }


        saveLampData();
    }
}

