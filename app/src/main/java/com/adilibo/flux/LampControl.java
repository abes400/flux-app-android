package com.adilibo.flux;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;
import com.skydoves.colorpickerview.sliders.BrightnessSlideBar;

public class LampControl extends AppCompatActivity {

    ColorPickerView colorPickerView;
    BrightnessSlideBar brightnessSlideBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lamp_control);

        FluxApp fluxApp = (FluxApp) getApplication();
        LampRVModel lamp = fluxApp.getLampAt(getIntent().getIntExtra("Index", 0));

        Button backLC = findViewById(R.id.back_LC);
        TextView title = findViewById(R.id.title_LC);
        Switch toggleLC = findViewById(R.id.toggle_LC);
        Switch autoBrightness = findViewById(R.id.autoBrightness);
        colorPickerView = findViewById(R.id.colorPickerView);
        brightnessSlideBar = findViewById(R.id.brightnessSlide);
        colorPickerView.attachBrightnessSlider(brightnessSlideBar);
        MaterialCardView pickerCardView = findViewById(R.id.picker_card);

        colorPickerView.setColorListener((ColorEnvelopeListener) (envelope, fromUser) -> {
            lamp.setHexStr(envelope.getHexCode());
            pickerCardView.setStrokeColor(envelope.getColor());
        });


        backLC.setOnClickListener(v -> finish());
        toggleLC.setOnClickListener(v -> lamp.isOn = toggleLC.isChecked());
        autoBrightness.setOnClickListener(v -> lamp.auto_brightness = autoBrightness.isChecked());

        title.setText(lamp.name);
        toggleLC.setChecked(lamp.isOn);
        autoBrightness.setChecked(lamp.auto_brightness);
        colorPickerView.setInitialColor(Color.parseColor("#" + lamp.getHexStr()));
    }
}