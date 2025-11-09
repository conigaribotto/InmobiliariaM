package com.example.inmobiliaria.ui.perfil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.inmobiliaria.R;
import com.example.inmobiliaria.databinding.FragmentPerfilBinding;

public class PerfilFragment extends Fragment {

    private FragmentPerfilBinding binding;
    private PerfilViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(PerfilViewModel.class);

        // Observa y pinta — sin ifs
        viewModel.getNombre().observe(getViewLifecycleOwner(), v -> binding.etNombre.setText(v));
        viewModel.getApellido().observe(getViewLifecycleOwner(), v -> binding.etApellido.setText(v));
        viewModel.getDni().observe(getViewLifecycleOwner(), v -> binding.etDni.setText(v));
        viewModel.getEmail().observe(getViewLifecycleOwner(), v -> binding.etEmail.setText(v));
        viewModel.getTelefono().observe(getViewLifecycleOwner(), v -> binding.etTelefono.setText(v));

        viewModel.getEditable().observe(getViewLifecycleOwner(), editable -> {
            binding.etNombre.setEnabled(editable);
            binding.etApellido.setEnabled(editable);
            // DNI siempre NO editable (requisito)
            binding.etDni.setEnabled(false);
            binding.etEmail.setEnabled(editable);
            binding.etTelefono.setEnabled(editable);
        });

        viewModel.getBotonTexto().observe(getViewLifecycleOwner(), txt -> binding.btnPerfil.setText(txt));

        binding.btnPerfil.setOnClickListener(v ->
                viewModel.onClickBotonPrincipal(
                        binding.btnPerfil.getText().toString(),
                        binding.etNombre.getText().toString(),
                        binding.etApellido.getText().toString(),
                        binding.etDni.getText().toString(),
                        binding.etTelefono.getText().toString(),
                        binding.etEmail.getText().toString()
                )
        );

        binding.btnClave.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_nav_perfil_to_CambiarClaveFragment)
        );

        // Carga inicial
        viewModel.cargarPropietario();

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
