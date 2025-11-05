package com.example.inmobiliaria.ui.contratos;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inmobiliaria.R;
import com.example.inmobiliaria.model.Alquiler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ContratoAdapter extends RecyclerView.Adapter<ContratoAdapter.VH> {

    public interface OnContratoListener {
        void onVerPagos(Alquiler contrato);
    }

    private final List<Alquiler> data = new ArrayList<>();
    private final OnContratoListener listener;
    private final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public ContratoAdapter(OnContratoListener listener) {
        this.listener = listener;
    }

    public void submit(List<Alquiler> list) {
        data.clear();
        if (list != null) data.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contrato, parent, false);
        return new VH(v);
    }

    @Override public void onBindViewHolder(@NonNull VH h, int pos) {
        Alquiler c = data.get(pos);
        h.tvNumero.setText(h.itemView.getContext().getString(R.string.contrato_numero) + c.getIdAlquiler());
        h.tvPrecio.setText(h.itemView.getContext().getString(R.string.contrato_precio, String.valueOf(c.getPrecio())));
        String fi = c.getFechaInicio() != null ? c.getFechaInicio().toLocalDate().toString() : "-";
        String ff = c.getFechaFin() != null ? c.getFechaFin().toLocalDate().toString() : "-";
        h.tvPeriodo.setText(h.itemView.getContext().getString(R.string.contrato_periodo, fi, ff));
        h.btnPagos.setOnClickListener(v -> listener.onVerPagos(c));
    }

    @Override public int getItemCount() { return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvNumero, tvPrecio, tvPeriodo;
        Button btnPagos;
        VH(@NonNull View v) {
            super(v);
            tvNumero = v.findViewById(R.id.tvNumero);
            tvPrecio = v.findViewById(R.id.tvPrecio);
            tvPeriodo = v.findViewById(R.id.tvPeriodo);
            btnPagos = v.findViewById(R.id.btnPagos);
        }
    }
}
