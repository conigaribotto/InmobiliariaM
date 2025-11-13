package com.example.inmobiliaria.ui.inmuebles;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.inmobiliaria.R;

public class InmuebleDetalleFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_inmueble_detalle, container, false);

        InmuebleDetalleViewModel vm =
                new ViewModelProvider(this).get(InmuebleDetalleViewModel.class);

        int id = requireArguments().getInt("inmuebleId");
        vm.init(id);

        ImageView ivFoto   = v.findViewById(R.id.ivFotoInmueble);
        TextView tvT       = v.findViewById(R.id.tvTitulo);
        TextView tvD       = v.findViewById(R.id.tvDireccion);
        TextView tvUso     = v.findViewById(R.id.tvUso);
        TextView tvTipo    = v.findViewById(R.id.tvTipo);
        TextView tvA       = v.findViewById(R.id.tvAmbientes);
        TextView tvSup     = v.findViewById(R.id.tvSuperficie);
        TextView tvValor   = v.findViewById(R.id.tvValor);
        TextView tvLat     = v.findViewById(R.id.tvLatitud);
        TextView tvLon     = v.findViewById(R.id.tvLongitud);
        TextView tvEstado  = v.findViewById(R.id.tvEstado);

        vm.getTitulo().observe(getViewLifecycleOwner(), tvT::setText);
        vm.getDireccion().observe(getViewLifecycleOwner(), tvD::setText);
        vm.getUso().observe(getViewLifecycleOwner(), tvUso::setText);
        vm.getTipo().observe(getViewLifecycleOwner(), tvTipo::setText);
        vm.getAmbientes().observe(getViewLifecycleOwner(), tvA::setText);
        vm.getSuperficie().observe(getViewLifecycleOwner(), tvSup::setText);
        vm.getValor().observe(getViewLifecycleOwner(), tvValor::setText);
        vm.getLatitud().observe(getViewLifecycleOwner(), tvLat::setText);
        vm.getLongitud().observe(getViewLifecycleOwner(), tvLon::setText);
        vm.getEstado().observe(getViewLifecycleOwner(), tvEstado::setText);

        vm.getFotoUrl().observe(getViewLifecycleOwner(), url ->
                Glide.with(requireContext())
                        .load(url)
                        .placeholder(android.R.drawable.ic_menu_gallery)
                        .error(android.R.drawable.ic_menu_report_image)
                        .into(ivFoto)
        );

        vm.cargar();
        return v;
    }
}
