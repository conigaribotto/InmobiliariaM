package com.example.inmobiliaria.ui.inmuebles;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.inmobiliaria.R;
import com.example.inmobiliaria.model.Inmueble;

public class InmuebleAdapter extends ListAdapter<Inmueble, InmuebleAdapter.VH> {

    public interface InmuebleListener {
        void onToggleClick(Inmueble item);
        void onItemClick(Inmueble item);
    }

    private final InmuebleListener listener;

    public InmuebleAdapter(InmuebleListener l) {
        super(DIFF);
        this.listener = l;
    }

    private static final DiffUtil.ItemCallback<Inmueble> DIFF = new DiffUtil.ItemCallback<Inmueble>() {
        @Override
        public boolean areItemsTheSame(@NonNull Inmueble oldItem, @NonNull Inmueble newItem) {
            return oldItem.getIdInmueble() == newItem.getIdInmueble();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Inmueble oldItem, @NonNull Inmueble newItem) {
            return
                    safe(oldItem.getTitulo()).equals(safe(newItem.getTitulo())) &&
                            safe(oldItem.getDireccion()).equals(safe(newItem.getDireccion())) &&
                            oldItem.isHabilitado() == newItem.isHabilitado() &&
                            safe(oldItem.getFotoUrl()).equals(safe(newItem.getFotoUrl()));
        }

        private String safe(String s) { return s == null ? "" : s; }
    };

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inmueble, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Inmueble item = getItem(position);

        h.tvTitulo.setText(item.getTitulo());
        h.tvDireccion.setText(item.getDireccion());
        h.tvEstado.setText(item.isHabilitado() ? "Habilitado" : "Deshabilitado");
        h.btnToggle.setText(item.isHabilitado() ? "Deshabilitar" : "Habilitar");

        String url = item.getFotoUrl();
        Glide.with(h.img.getContext())
                .load(url)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_report_image)
                .into(h.img);

        h.itemView.setOnClickListener(v -> listener.onItemClick(item));
        h.btnToggle.setOnClickListener(v -> listener.onToggleClick(item));
    }

    static class VH extends RecyclerView.ViewHolder {
        ImageView img;
        TextView tvTitulo, tvDireccion, tvEstado;
        Button btnToggle;

        VH(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgFoto);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
            tvDireccion = itemView.findViewById(R.id.tvDireccion);
            tvEstado = itemView.findViewById(R.id.tvEstado);
            btnToggle = itemView.findViewById(R.id.btnToggle);
        }
    }
}

