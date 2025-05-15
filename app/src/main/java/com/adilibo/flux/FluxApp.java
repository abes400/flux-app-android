package com.adilibo.flux;

import android.app.Application;
//import android.util.Log;

import java.util.ArrayList;


public class FluxApp extends Application {
    private final ArrayList<LampRVModel> _lamps = new ArrayList<>();

    public FluxApp() {

    }

    public synchronized LampRVModel getLampAt(int index) {return _lamps.get(index);}
    public synchronized boolean registerLamp(LampRVModel newLamp) {return _lamps.add(newLamp);}
    public synchronized int getLampCount() {return _lamps.size();}
    public synchronized boolean removeLampAt(int index) {
        if(index >= 0 && index < _lamps.size()){
            _lamps.remove(index);
            return true;
        }
        return false;
    }
}

