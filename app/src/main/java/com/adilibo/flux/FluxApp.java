package com.adilibo.flux;

import android.app.Application;
import android.bluetooth.BluetoothDevice;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;
//import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;

import com.harrysoft.androidbluetoothserial.BluetoothManager;
import com.harrysoft.androidbluetoothserial.BluetoothSerialDevice;
import com.harrysoft.androidbluetoothserial.SimpleBluetoothDeviceInterface;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class FluxApp extends Application {
    ArrayList<LampRVModel> _lamps;

    SharedPreferences sPrefs;
    SharedPreferences.Editor sPrefEdit;
    public int currentAct = 0;


    private BluetoothManager btMgr = BluetoothManager.getInstance();
    private SimpleBluetoothDeviceInterface deviceInterface;
    private String currentMAC = "";

    @Override
    public void onCreate() {
        super.onCreate();
        sPrefs = getSharedPreferences("LAMP_METADATA", MODE_PRIVATE);
        sPrefEdit = sPrefs.edit();

        _lamps = new ArrayList<>();

        if (btMgr == null) {
            Toast.makeText(this, "BT not supported", Toast.LENGTH_SHORT).show();
        }

    }

    public synchronized void connectDevice(int index) {
        btMgr.openSerialDevice(_lamps.get(index).getAddress())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onConnected, this::onConnectionError);
    }

    public void disconnect() {
        if(deviceInterface != null) {
            btMgr.closeDevice(currentMAC);
            deviceInterface = null;
        }
    }

    private void onConnected(BluetoothSerialDevice device) {
        String newMAC = device.getMac();

        if(!currentMAC.equals(newMAC)){
            disconnect();
            currentMAC = newMAC;
        }

        deviceInterface = device.toSimpleDeviceInterface();
        deviceInterface.setErrorListener(this::onConnectionError);

        // HERE COMES THE STUPIDEST IMPLEMENTATION EVER KNOWN TO MANKIND
        int index = 0;
        for(LampRVModel lamp : _lamps) {
            if(lamp.getAddress().equals(device.getMac())) {
                sendCommand(index);
                break;
            }
            index++;
        }
    }

    public void onConnectionError(Throwable error) {
        btMgr = BluetoothManager.getInstance();

        Toast.makeText(this, "Check BT connection or whether another device is connecting", Toast.LENGTH_SHORT).show();
        error.printStackTrace();
    }

    public synchronized void sendCommand(@NonNull int  index) {
        if(deviceInterface != null) {
            LampRVModel lamp = _lamps.get(index);
            deviceInterface.sendMessage("\\" + (lamp.isOn ? '1' : '0')
                    + (lamp.auto_brightness ? '1' : '0') + lamp.getHexStr() + "/");
        }
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
        for(BluetoothDevice device : btMgr.getPairedDevicesList()) {
            String address = device.getAddress();
            String name = device.getName();

            _lamps.add(new LampRVModel(
                    sPrefs.getString(address + "_nickname", name),
                    address,
                    sPrefs.getString(address + "_hex", "FFFFFFFF"),
                    sPrefs.getBoolean(address + "_auto", false),
                    sPrefs.getBoolean(address + "_on_off", false)
            ));
        }


        saveLampData();
    }
}

