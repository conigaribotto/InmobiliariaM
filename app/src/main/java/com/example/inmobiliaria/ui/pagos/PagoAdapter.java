package com.example.inmobiliaria.ui.pagos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inmobiliaria.R;
import com.example.inmobiliaria.model.Pagos;

import java.util.ArrayList;
import java.util.List;

public class PagoAdapter extends RecyclerView.Adapter<PagoAdapter.VH> {

    private final List<Pagos> data = new ArrayList<>();

    public void submit(List<Pagos> list) {
        data.clear();
        if (list != null) data.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pago, parent, false);
        return new VH(v);
    }

    @Override public void onBindViewHolder(@NonNull VH h, int pos) {
        Pagos p = data.get(pos);
        h.tvNumeroPago.setText(h.itemView.getContext().getString(R.string.pago_numero, p.getNumero()));
        h.tvImporte.setText(h.itemView.getContext().getString(R.string.pago_importe, String.valueOf(p.getImporte())));
        h.tvFecha.setText(h.itemView.getContext().getString(R.string.pago_fecha, p.getFecha() != null ? p.getFecha().toString() : "-"));
    }

    @Override public int getItemCount() { return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvNumeroPago, tvImporte, tvFecha;
        VH(@NonNull View v) {
            super(v);
            tvNumeroPago = v.findViewById(R.id.tvNumeroPago);
            tvImporte = v.findViewById(R.id.tvImporte);
            tvFecha = v.findViewById(R.id.tvFecha);
        }
    }
}

