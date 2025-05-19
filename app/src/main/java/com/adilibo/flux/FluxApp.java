package com.adilibo.flux;

import android.app.Application;
import android.content.Context;
//import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


public class FluxApp extends Application {
    final String FILENAME = "LAMP_DATA";
    ArrayList<LampRVModel> _lamps;

    @Override
    public void onCreate() {
        super.onCreate();
        loadLampData();
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

    private synchronized void saveLampData() {
        try {
            FileOutputStream FOStream = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            ObjectOutputStream OOStream = new ObjectOutputStream(FOStream);
            OOStream.writeObject(_lamps);
            OOStream.close();
        } catch(Exception e) {e.printStackTrace();}
    }

    private synchronized void loadLampData() {
        try {
            FileInputStream FIStream = openFileInput(FILENAME);
            ObjectInputStream OIStream = new ObjectInputStream(FIStream);
            _lamps = (ArrayList<LampRVModel>) OIStream.readObject();
            OIStream.close();
        } catch(Exception e) {
            e.printStackTrace();
            _lamps = new ArrayList<>();
        }
    }
}

