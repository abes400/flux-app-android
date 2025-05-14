package com.adilibo.flux;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;
import com.skydoves.colorpickerview.sliders.BrightnessSlideBar;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class LampControl extends AppCompatActivity {

    ColorPickerView colorPickerView;
    Toast success;
    Button resetPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lamp_control);

        success = Toast.makeText(this, R.string.image_success, Toast.LENGTH_SHORT);

        FluxApp fluxApp = (FluxApp) getApplication();
        LampRVModel lamp = fluxApp.getLampAt(getIntent().getIntExtra("Index", 0));

        Button backLC = findViewById(R.id.back_LC);
        Button uploadPhoto = findViewById(R.id.uploadPhoto);
        resetPhoto = findViewById(R.id.resetPhoto);
        resetPhoto.setEnabled(false);
        TextView title = findViewById(R.id.title_LC);
        SwitchCompat toggleLC = findViewById(R.id.toggle_LC);
        SwitchCompat autoBrightness = findViewById(R.id.autoBrightness);
        colorPickerView = findViewById(R.id.colorPickerView);
        BrightnessSlideBar brightnessSlideBar = findViewById(R.id.brightnessSlide);
        colorPickerView.attachBrightnessSlider(brightnessSlideBar);
        MaterialCardView pickerCardView = findViewById(R.id.picker_card);

        colorPickerView.setColorListener((ColorEnvelopeListener) (envelope, fromUser) -> {
            lamp.setHexStr(envelope.getHexCode());
            pickerCardView.setStrokeColor(envelope.getColor());
        });


        backLC.setOnClickListener(v -> finish());
        toggleLC.setOnClickListener(v -> lamp.isOn = toggleLC.isChecked());
        autoBrightness.setOnClickListener(v -> lamp.auto_brightness = autoBrightness.isChecked());
        uploadPhoto.setOnClickListener(v -> uploadPicture());
        resetPhoto.setOnClickListener( v -> {
            colorPickerView.setHsvPaletteDrawable();
            resetPhoto.setEnabled(false);
        });

        title.setText(lamp.name);
        toggleLC.setChecked(lamp.isOn);
        autoBrightness.setChecked(lamp.auto_brightness);
        colorPickerView.setInitialColor(Color.parseColor("#" + lamp.getHexStr()));

    }
    protected void uploadPicture() {
        Intent imageIntent = new Intent(Intent.ACTION_PICK);
        imageIntent.setType("image/*");
        startActivityForResult(imageIntent, 1);
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            // Just copied and pasted it directly from documentation, please don't ask me how it works
            final Uri imageURi = data.getData();
            final InputStream imageStream = getContentResolver().openInputStream(imageURi);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            Drawable drawable = new BitmapDrawable(getResources(), selectedImage);
            colorPickerView.setPaletteDrawable(drawable);
            resetPhoto.setEnabled(true);
            success.show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (RuntimeException re) {
            re.printStackTrace();
        }
    }
}