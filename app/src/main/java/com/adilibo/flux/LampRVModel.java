package com.adilibo.flux;
//import android.util.Log;

import java.io.Serializable;

public class LampRVModel implements Serializable {

    String hexStr;
    String address;
    public String name;
    public boolean auto_brightness, isOn;

    public LampRVModel (String name,
                        String address,
                        String hexStr,
                        boolean auto_brightness,
                        boolean isOn) {

        this.name = name;
        this.address = address;
        this.hexStr = hexStr;
        this.auto_brightness = auto_brightness;
        this.isOn = isOn;
    }

    public String getAddress() {
        return address;
    }

    public String getHexStr() {
        return hexStr;
    }
    public void setHexStr(String hexStr) { this.hexStr = hexStr; }

    public String getName() { return name; }

    public synchronized boolean rename(String newName) {
        if(!newName.isEmpty() && !newName.equals(name)) {
            name = newName;
            return true;
        }
        return false;
    }

    // hsb to rgb
    // https://stackoverflow.com/questions/7896280/converting-from-hsv-hsb-in-java-to-rgb-without-using-java-awt-color-disallowe
}