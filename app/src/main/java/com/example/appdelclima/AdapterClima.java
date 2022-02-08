package com.example.appdelclima;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterClima extends RecyclerView.Adapter{

    ArrayList<Clima> listDatos;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        final TextView textViewDia, textViewTemp;

        public ViewHolder(View view){
            super(view);
            textViewDia = (TextView) view.findViewById(R.id.diaSemana);
            textViewTemp = (TextView) view.findViewById(R.id.tempDia);
        }
    }

    public AdapterClima(ArrayList<Clima> listDatos) {
        this.listDatos = listDatos;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Clima miClima= listDatos.get(position);
        ((ViewHolder)holder).textViewDia.setText(miClima.getDia());
        ((ViewHolder)holder).textViewTemp.setText(miClima.getTemp());
    }
    @Override
    public int getItemCount() {
        return listDatos.size();
    }
}

