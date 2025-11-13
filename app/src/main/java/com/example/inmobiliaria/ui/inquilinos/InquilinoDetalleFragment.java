package com.example.inmobiliaria.ui.inquilinos;

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

public class InquilinoDetalleFragment extends Fragment {

    private InquilinoDetalleViewModel vm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_inquilino_detalle, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        vm = new ViewModelProvider(this).get(InquilinoDetalleViewModel.class);

        // argumento que viene desde InquilinosFragment
        int contratoId = requireArguments().getInt("contratoId", -1);
        vm.init(contratoId);

        TextView tvInquilino = v.findViewById(R.id.tvInquilino);
        TextView tvDni       = v.findViewById(R.id.tvDni);
        TextView tvTel       = v.findViewById(R.id.tvTelefono);
        TextView tvMail      = v.findViewById(R.id.tvEmail);
        TextView tvInm       = v.findViewById(R.id.tvInmueble);
        TextView tvFechas    = v.findViewById(R.id.tvFechas);
        TextView tvMonto     = v.findViewById(R.id.tvMonto);

        vm.getNombreCompleto().observe(getViewLifecycleOwner(), tvInquilino::setText);
        vm.getDni().observe(getViewLifecycleOwner(), tvDni::setText);
        vm.getTelefono().observe(getViewLifecycleOwner(), tvTel::setText);
        vm.getEmail().observe(getViewLifecycleOwner(), tvMail::setText);
        vm.getDireccionInmueble().observe(getViewLifecycleOwner(), tvInm::setText);
        vm.getFechasContrato().observe(getViewLifecycleOwner(), tvFechas::setText);
        vm.getMontoContrato().observe(getViewLifecycleOwner(), tvMonto::setText);

        vm.cargar();
    }
}


