package com.adilibo.flux;
import java.io.Serializable;
import java.util.HexFormat;

public class LampRVModel implements Serializable {
    public String name;
    private String addr, rgb;
    public int hue, sat, brightness;
    public boolean autobrightness, isOn;

    public LampRVModel (String name,
                        String addr,
                        int hue,
                        int sat,
                        int brightness,
                        boolean autobrightness,
                        boolean isOn) {

        this.name = name;
        this.addr = addr;
        this.hue = hue;
        this.sat = sat;
        this.brightness = brightness;
        this.autobrightness = autobrightness;
        this.isOn = isOn;
    }

    public String getAddr() {
        return addr;
    }

    public String getRgb() {
        return rgb;
    }

    // hsb to rgb
    // https://stackoverflow.com/questions/7896280/converting-from-hsv-hsb-in-java-to-rgb-without-using-java-awt-color-disallowe



}