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

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inf, @Nullable ViewGroup c, @Nullable Bundle b) {
        View v = inf.inflate(R.layout.fragment_inquilino_detalle, c, false);

        InquilinoDetalleViewModel vm =
                new ViewModelProvider(this).get(InquilinoDetalleViewModel.class);

        TextView tvNom = v.findViewById(R.id.tvNombre);
        TextView tvDni = v.findViewById(R.id.tvDni);
        TextView tvTel = v.findViewById(R.id.tvTelefono);
        TextView tvMail = v.findViewById(R.id.tvEmail);
        TextView tvInm = v.findViewById(R.id.tvInmueble);

        vm.getNombreCompleto().observe(getViewLifecycleOwner(), tvNom::setText);
        vm.getDni().observe(getViewLifecycleOwner(), tvDni::setText);
        vm.getTelefono().observe(getViewLifecycleOwner(), tvTel::setText);
        vm.getEmail().observe(getViewLifecycleOwner(), tvMail::setText);
        vm.getDireccionInmueble().observe(getViewLifecycleOwner(), tvInm::setText);

        vm.cargar();
        return v;
    }
}
