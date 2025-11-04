package com.example.inmobiliaria.ui.perfil;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.inmobiliaria.R;
import com.example.inmobiliaria.databinding.FragmentCambiarClaveBinding;

public class CambiarClaveFragment extends Fragment {

    private CambiarClaveViewModel viewModel;
    private FragmentCambiarClaveBinding binding;

    public static CambiarClaveFragment newInstance() {
        return new CambiarClaveFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        viewModel = new ViewModelProvider(this).get(CambiarClaveViewModel.class);
        binding = FragmentCambiarClaveBinding.inflate(inflater, container, false);

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String claveActual = binding.etActual.getText().toString();
                String claveNueva = binding.etNueva.getText().toString();
                String claveConfirmar = binding.etConfirmar.getText().toString();

                viewModel.cambiarClave(claveActual, claveNueva, claveConfirmar);
            }
        });

        viewModel.getLimpiarCampos().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    binding.etActual.setText("");
                    binding.etNueva.setText("");
                    binding.etConfirmar.setText("");
                    viewModel.resetearLimpiarCampos();
                }
            }
        });

        return binding.getRoot();
    }



}