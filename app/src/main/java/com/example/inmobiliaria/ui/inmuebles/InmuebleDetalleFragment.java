package com.example.inmobiliaria.ui.inmuebles;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.inmobiliaria.R;

public class InmuebleDetalleFragment extends Fragment {

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inf, @Nullable ViewGroup c, @Nullable Bundle b) {
        View v = inf.inflate(R.layout.fragment_inmueble_detalle, c, false);

        InmuebleDetalleViewModel vm = new ViewModelProvider(this).get(InmuebleDetalleViewModel.class);

        // Recibe el argumento y se lo pasa a la VM
        int id = requireArguments().getInt("inmuebleId");
        vm.init(id);

        TextView tvT = v.findViewById(R.id.tvTitulo);
        TextView tvD = v.findViewById(R.id.tvDireccion);
        TextView tvA = v.findViewById(R.id.tvAmbientes);
        TextView tvP = v.findViewById(R.id.tvPrecio);
        TextView tvE = v.findViewById(R.id.tvEstado);

        vm.getTitulo().observe(getViewLifecycleOwner(), tvT::setText);
        vm.getDireccion().observe(getViewLifecycleOwner(), tvD::setText);
        vm.getAmbientes().observe(getViewLifecycleOwner(), tvA::setText);
        vm.getPrecio().observe(getViewLifecycleOwner(), tvP::setText);
        vm.getEstado().observe(getViewLifecycleOwner(), tvE::setText);

        vm.cargar();
        return v;
    }
}
