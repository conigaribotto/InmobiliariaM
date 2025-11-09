package com.example.inmobiliaria.ui.contratos;

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

public class ContratosDetalleFragment extends Fragment {

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inf, @Nullable ViewGroup c, @Nullable Bundle b) {
        View v = inf.inflate(R.layout.fragment_contratos_detalle, c, false);

        ContratosDetalleViewModel vm = new ViewModelProvider(this).get(ContratosDetalleViewModel.class);

        int id = requireArguments().getInt("contratoId");
        vm.init(id);

        TextView tvInq = v.findViewById(R.id.tvInquilino);
        TextView tvInm = v.findViewById(R.id.tvInmueble);
        TextView tvFec = v.findViewById(R.id.tvFechas);
        TextView tvMon = v.findViewById(R.id.tvMonto);

        vm.getInquilino().observe(getViewLifecycleOwner(), tvInq::setText);
        vm.getInmueble().observe(getViewLifecycleOwner(), tvInm::setText);
        vm.getFechas().observe(getViewLifecycleOwner(), tvFec::setText);
        vm.getMonto().observe(getViewLifecycleOwner(), tvMon::setText);

        vm.cargar();
        return v;
    }
}
