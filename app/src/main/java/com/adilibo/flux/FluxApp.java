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
            btMgr.closeDevice(deviceInterface);
            deviceInterface = null;
            Log.d("INFO", "Disconnected");
        }
    }

    private void onConnected(BluetoothSerialDevice device) {
        deviceInterface = device.toSimpleDeviceInterface();
        deviceInterface.setErrorListener(this::onConnectionError);
        deviceInterface.setMessageSentListener(this::onSent);

        // HERE COMES THE STUPIDEST IMPLEMENTATION EVER KNOWN TO MANKIND
        boolean isOn = false;
        for(LampRVModel lamp : _lamps) {
            if(lamp.getAddress().equals(device.getMac())) {
                isOn = lamp.isOn;
                break;
            }
        }

        deviceInterface.sendMessage(isOn ? "\\on/" : "\\off/");
    }

    private void onSent(String message) {
        if(currentAct == 0)
            disconnect();
    }

    public void onConnectionError(Throwable error) {
        // TODO: Error handling
    }

    public synchronized void sendColor(@NonNull String  color) {
        if(deviceInterface != null) {
            deviceInterface.sendMessage(color);
        }
    }

    public synchronized void sendCommand(int cmd) {

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

