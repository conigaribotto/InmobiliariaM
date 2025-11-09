package com.example.inmobiliaria.ui.inquilinos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inmobiliaria.R;
import com.example.inmobiliaria.model.Alquiler;

import java.util.ArrayList;
import java.util.List;

public class InquilinosAdapter extends RecyclerView.Adapter<InquilinosAdapter.VH> {

    public interface OnItemClick { void onClick(Alquiler a); }

    private final List<Alquiler> data = new ArrayList<>();
    private final OnItemClick listener;

    public InquilinosAdapter(OnItemClick l) { listener = l; }

    public void submit(List<Alquiler> nuevos) {
        data.clear(); if (nuevos!=null) data.addAll(nuevos); notifyDataSetChanged();
    }

    @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup p, int vtype) {
        View v = LayoutInflater.from(p.getContext()).inflate(R.layout.item_inquilino, p, false);
        return new VH(v);
    }

    @Override public void onBindViewHolder(@NonNull VH h, int pos) {
        Alquiler a = data.get(pos);
        String nombre = a.getInquilino()!=null ? a.getInquilino().getNombre()+" "+a.getInquilino().getApellido() : "Inquilino";
        String dir = a.getInmueble()!=null ? a.getInmueble().getDireccion() : "";
        h.tvNombre.setText(nombre);
        h.tvInmueble.setText(dir);
        h.itemView.setOnClickListener(v -> listener.onClick(a));
    }

    @Override public int getItemCount() { return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvNombre, tvInmueble;
        VH(@NonNull View v) {
            super(v);
            tvNombre = v.findViewById(R.id.tvNombre);
            tvInmueble = v.findViewById(R.id.tvInmueble);
        }
    }
}
