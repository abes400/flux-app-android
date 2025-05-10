package com.adilibo.flux;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LampRVAdapter extends RecyclerView.Adapter<LampRVAdapter.Holder> {
    ArrayList<LampRVModel> lampList;
    Context context;

    public LampRVAdapter(Context context, ArrayList<LampRVModel> lampList) {
        this.context = context;
        this.lampList = lampList;
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
        holder.lampName.setText(lampList.get(position).name);
        holder.lampToggle.setChecked(lampList.get(position).isOn);
        holder.lamp = lampList.get(position);
    }

    @Override
    public int getItemCount() {
        return lampList.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {
        TextView lampName;
        Switch lampToggle;
        CardView baseCard;
        LampRVModel lamp;
        public Holder(@NonNull View itemView) {
            super(itemView);
            lampName = itemView.findViewById(R.id.lampName);
            lampToggle = itemView.findViewById(R.id.lampToggle);
            baseCard = itemView.findViewById(R.id.base_card);

            baseCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), LampControl.class);
                    intent.putExtra("Data", lamp);
                    v.getContext().startActivity(intent);
                }
            });

        }
    }
}
