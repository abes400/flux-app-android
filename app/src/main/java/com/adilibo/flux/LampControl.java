package com.adilibo.flux;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
//import android.util.Log;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.skydoves.colorpickerview.ActionMode;
import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;
import com.skydoves.colorpickerview.sliders.BrightnessSlideBar;

import java.io.InputStream;

public class LampControl extends AppCompatActivity {

    int index;
    Toast success;
    TextView title;
    FluxApp fluxApp;
    LampRVModel lamp;
    Button resetPhoto;
    ColorPickerView colorPickerView;

    final int MAX_DELAY_COEFF = 7;
    int delay = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lamp_control);

        fluxApp = (FluxApp) getApplication();
        success = Toast.makeText(this, R.string.image_success, Toast.LENGTH_SHORT);
        index = getIntent().getIntExtra("Index", 0);
        lamp = fluxApp.getLampAt(index);

        BrightnessSlideBar brightnessSlideBar = findViewById(R.id.brightnessSlide);
        MaterialCardView pickerCardView = findViewById(R.id.picker_card);

        colorPickerView = findViewById(R.id.colorPickerView);
        colorPickerView.attachBrightnessSlider(brightnessSlideBar);
        colorPickerView.setInitialColor(Color.parseColor("#" + lamp.getHexStr()));
        colorPickerView.setColorListener((ColorEnvelopeListener) (envelope, fromUser) -> {
            delay++;
            if(delay >= MAX_DELAY_COEFF) {
                delay = 0;
                lamp.setHexStr(envelope.getHexCode());
                pickerCardView.setStrokeColor(envelope.getColor());
                fluxApp.sendCommand(index);

            }
        });
        //colorPickerView.setActionMode(ActionMode.LAST);

        SwitchCompat autoBrightness = findViewById(R.id.autoBrightness);
        autoBrightness.setChecked(lamp.auto_brightness);
        autoBrightness.setOnClickListener(v -> lamp.auto_brightness = autoBrightness.isChecked());

        Button backLC = findViewById(R.id.back_LC);
        backLC.setOnClickListener(v -> finish());

        title = findViewById(R.id.title_LC);
        title.setText(lamp.getName());
        title.setOnClickListener(v -> renameLampDialog());

        SwitchCompat toggleLC = findViewById(R.id.toggle_LC);
        toggleLC.setChecked(lamp.isOn);
        toggleLC.setOnClickListener(v -> lamp.isOn = toggleLC.isChecked());

        Button uploadPhoto = findViewById(R.id.uploadPhoto);
        uploadPhoto.setOnClickListener(v -> uploadPicture());

        resetPhoto = findViewById(R.id.resetPhoto);
        resetPhoto.setEnabled(false);
        resetPhoto.setOnClickListener( v -> {
            colorPickerView.setHsvPaletteDrawable();
            resetPhoto.setEnabled(false);
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        fluxApp.connectDevice(index);
        fluxApp.currentAct = 1;
    }

    @Override
    protected void onStop() {
        super.onStop();
        fluxApp.saveOneLamp(index);
        fluxApp.disconnect();
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            // Just copied and pasted it directly from documentation, please don't ask me how it works
            final Uri imageURi = data.getData();
            assert imageURi != null;
            final InputStream imageStream = getContentResolver().openInputStream(imageURi);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            Drawable drawable = new BitmapDrawable(getResources(), selectedImage);
            colorPickerView.setPaletteDrawable(drawable);
            resetPhoto.setEnabled(true);
            success.show();
        } catch (Exception e) { e.printStackTrace(); }
    }

    protected void renameLampDialog() {
        final EditText input = new EditText(this);
        input.setSingleLine(true);
        input.setText(title.getText().toString());
        input.setPadding(50, 50, 50, 50);
        AlertDialog dialog = new AlertDialog.Builder(this)
            .setTitle(R.string.rename)
            .setView(input)
            .setPositiveButton(R.string.ok, (dialogInterface, i) -> {
                String newName = input.getText().toString();
                if(lamp.rename(input.getText().toString())) title.setText(newName);
            })
            .setNegativeButton(R.string.cancel, ((dialogInterface, i) ->{}))
            .create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getColor(R.color.secondary));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getColor(R.color.secondary));
    }
    protected void uploadPicture() {
        Intent imageIntent = new Intent(Intent.ACTION_PICK);
        imageIntent.setType("image/*");
        //noinspection deprecation
        startActivityForResult(imageIntent, 1);
    }
}