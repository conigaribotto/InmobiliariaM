package com.example.inmobiliaria.ui.inmuebles;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.inmobiliaria.R;
import com.example.inmobiliaria.model.Inmueble;

import java.util.ArrayList;
import java.util.List;

public class InmuebleAdapter extends RecyclerView.Adapter<InmuebleAdapter.VH> {

    private final List<Inmueble> items = new ArrayList<>();
    private OnToggleListener listener;

    public interface OnToggleListener {
        void onToggle(int inmuebleId, boolean habilitar);
    }

    public void setOnToggleListener(OnToggleListener l) { this.listener = l; }

    public void setItems(List<Inmueble> list) {
        items.clear();
        if (list != null) items.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inmueble, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Inmueble i = items.get(position);
        holder.tvTitulo.setText(i.getTitulo());
        holder.tvDireccion.setText(i.getDireccion());
        holder.swHabilitado.setOnCheckedChangeListener(null); // reset listener
        holder.swHabilitado.setChecked(i.isHabilitado());

        if (i.getFotoUrl() != null && !i.getFotoUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext()).load(i.getFotoUrl()).into(holder.ivFoto);
        } else {
            holder.ivFoto.setImageResource(R.drawable.ic_launcher_foreground);
        }

        holder.swHabilitado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (listener != null) listener.onToggle(i.getIdInmueble(), isChecked);
            }
        });
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class VH extends RecyclerView.ViewHolder {
        ImageView ivFoto;
        TextView tvTitulo, tvDireccion;
        Switch swHabilitado;
        VH(@NonNull View itemView) {
            super(itemView);
            ivFoto = itemView.findViewById(R.id.ivFoto);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
            tvDireccion = itemView.findViewById(R.id.tvDireccion);
            swHabilitado = itemView.findViewById(R.id.swHabilitado);
        }
    }
}
