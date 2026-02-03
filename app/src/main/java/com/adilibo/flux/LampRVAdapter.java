package com.adilibo.flux;

import android.os.Handler;
import android.content.Context;
import android.content.Intent;
//import android.util.Log;
import android.graphics.Color;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

public class LampRVAdapter extends RecyclerView.Adapter<LampRVAdapter.Holder> {
    Context context;
    static FluxApp fluxApp;

    public LampRVAdapter(Context context, FluxApp fluxApp) {
        this.context = context;
        LampRVAdapter.fluxApp = fluxApp;
    }

    @NonNull
    @Override
    public LampRVAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_design, parent, false);

        return new LampRVAdapter.Holder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull LampRVAdapter.Holder holder, int position) {
        LampRVModel lamp = fluxApp.getLampAt(position);
        holder.lampName.setText(lamp.name);
        holder.lampToggle.setChecked(lamp.isOn);
        holder.baseCard.setStrokeColor(Color.parseColor("#"+lamp.getHexStr()));
    }

    @Override
    public int getItemCount() {
        return fluxApp.getLampCount();
    }

    public static class Holder extends RecyclerView.ViewHolder {
        TextView lampName;
        SwitchCompat lampToggle;
        MaterialCardView baseCard;

        public Holder(@NonNull View itemView) {
            super(itemView);
            lampName = itemView.findViewById(R.id.lampName);
            lampToggle = itemView.findViewById(R.id.lampToggle);
            baseCard = itemView.findViewById(R.id.base_card);

            baseCard.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), LampControl.class);
                intent.putExtra("Index", getAdapterPosition());
                v.getContext().startActivity(intent);
            });

            lampToggle.setOnClickListener(v ->
            {
                lampToggle.setEnabled(false);
                int index = getAdapterPosition();
                fluxApp.getLampAt(index).isOn = lampToggle.isChecked();
                fluxApp.connectDevice(index);
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    lampToggle.setEnabled(true);
                }, 0);
            });
        }
    }
}
