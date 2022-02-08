package com.example.appdelclima;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterCiudades extends RecyclerView.Adapter implements View.OnClickListener {

    ArrayList<Ciudad> listDatos;
    private View.OnClickListener listener;

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        final TextView textViewCiudad;

        public ViewHolder(View view){
            super(view);
            textViewCiudad = (TextView) view.findViewById(R.id.nombreCiudad);
        }
    }

    public AdapterCiudades(ArrayList<Ciudad> listDatos) {
        this.listDatos = listDatos;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ciudades, parent, false);
        view.setOnClickListener(this::onClick);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Ciudad miCiudad= listDatos.get(position);
        ((ViewHolder)holder).textViewCiudad.setText(miCiudad.getNombre());
    }
    @Override
    public int getItemCount() {
        return listDatos.size();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

}
