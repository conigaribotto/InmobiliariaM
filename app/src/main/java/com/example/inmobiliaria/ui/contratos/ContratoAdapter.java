package com.example.inmobiliaria.ui.contratos;

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

public class ContratoAdapter extends RecyclerView.Adapter<ContratoAdapter.VH> {

    public interface OnItemClick { void onClick(Alquiler a); }

    private final List<Alquiler> data = new ArrayList<>();
    private final OnItemClick listener;

    public ContratoAdapter(OnItemClick l) { listener = l; }

    public void submit(List<Alquiler> nuevos) {
        data.clear();
        if (nuevos != null) data.addAll(nuevos);
        notifyDataSetChanged();
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup p, int vtype) {
        View v = LayoutInflater.from(p.getContext()).inflate(R.layout.item_contrato, p, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        Alquiler a = data.get(pos);

        String inq = (a.getInquilino()!=null)
                ? (nz(a.getInquilino().getNombre()) + " " + nz(a.getInquilino().getApellido()))
                : "Inquilino";

        String dir = (a.getInmueble()!=null) ? nz(a.getInmueble().getDireccion()) : "Inmueble";

        String f1 = nz(a.getFechaInicio());
        String f2 = nz(a.getFechaFinalizacion());

        h.tvInquilino.setText(inq);
        h.tvInmueble.setText(dir);
        h.tvPeriodo.setText("Periodo: " + (f1.isEmpty() ? "?" : f1) + " a " + (f2.isEmpty() ? "?" : f2));

        h.itemView.setOnClickListener(v -> listener.onClick(a));
    }

    @Override public int getItemCount() { return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvInquilino, tvInmueble, tvPeriodo;
        VH(@NonNull View v) {
            super(v);
            tvInquilino = v.findViewById(R.id.tvInquilino);
            tvInmueble  = v.findViewById(R.id.tvInmueble);
            tvPeriodo   = v.findViewById(R.id.tvPeriodo);
        }
    }

    private static String nz(Object o){ return o==null? "" : String.valueOf(o); }
}

