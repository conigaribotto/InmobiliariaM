package com.example.inmobiliaria.ui.inmuebles;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inmobiliaria.R;
import com.example.inmobiliaria.model.Inmueble;

import java.util.ArrayList;
import java.util.List;

public class InmuebleAdapter extends RecyclerView.Adapter<InmuebleAdapter.VH> {

    public interface OnItemClick { void onClick(Inmueble i); }

    private final List<Inmueble> data = new ArrayList<>();
    private final OnItemClick listener;

    public InmuebleAdapter(OnItemClick l) { this.listener = l; }

    public void submit(List<Inmueble> nuevos) {
        data.clear();
        if (nuevos != null) data.addAll(nuevos);
        notifyDataSetChanged();
    }

    @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup p, int vtype) {
        View v = LayoutInflater.from(p.getContext()).inflate(R.layout.item_inmueble, p, false);
        return new VH(v);
    }

    @Override public void onBindViewHolder(@NonNull VH h, int pos) {
        Inmueble i = data.get(pos);
        String titulo = i.getTitulo() != null ? i.getTitulo() : ("Inmueble #" + i.getIdInmueble());
        h.tvTitulo.setText(titulo);
        h.tvDireccion.setText(i.getDireccion() != null ? i.getDireccion() : "");
        // Quitamos precio porque tu modelo no lo tiene
        h.itemView.setOnClickListener(v -> listener.onClick(i));
    }

    @Override public int getItemCount() { return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvDireccion, tvPrecio; // tvPrecio queda opcional (no se usa)
        VH(@NonNull View v) {
            super(v);
            tvTitulo = v.findViewById(R.id.tvTitulo);
            tvDireccion = v.findViewById(R.id.tvDireccion);
            // Si tu item_inmueble.xml no tiene tvPrecio, podés comentar la línea de abajo
            tvPrecio   = v.findViewById(R.id.tvPrecio);
        }
    }
}


