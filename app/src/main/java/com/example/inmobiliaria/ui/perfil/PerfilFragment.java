package com.example.inmobiliaria.ui.perfil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.inmobiliaria.R;
import com.example.inmobiliaria.databinding.FragmentPerfilBinding;
import com.example.inmobiliaria.model.Propietario;

public class PerfilFragment extends Fragment {

    private FragmentPerfilBinding binding;
    private PerfilViewModel viewModel;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(PerfilViewModel.class);
        binding = FragmentPerfilBinding.inflate(inflater, container, false);

        viewModel.getmPropietario().observe(getViewLifecycleOwner(), new Observer<Propietario>() {
            @Override
            public void onChanged(Propietario propietario) {
                binding.etApellido.setText(propietario.getApellido());
                binding.etDni.setText(String.valueOf(propietario.getDni()));
                binding.etEmail.setText(propietario.getEmail());
                binding.etNombre.setText(propietario.getNombre());
                binding.etTelefono.setText(String.valueOf(propietario.getTelefono()));
            }
        });
        viewModel.getmEstado().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                binding.etApellido.setEnabled(aBoolean);
                binding.etDni.setEnabled(aBoolean);
                binding.etEmail.setEnabled(aBoolean);
                binding.etNombre.setEnabled(aBoolean);
                binding.etTelefono.setEnabled(aBoolean);
            }
        });
        viewModel.getBtnTexto().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.btnPerfil.setText(s);

            }
        });

        viewModel.leerPropietario();

        binding.btnPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = binding.etNombre.getText().toString();
                String apellido = binding.etApellido.getText().toString();
                String dni = binding.etDni.getText().toString();
                String telefono = binding.etTelefono.getText().toString();
                String email = binding.etEmail.getText().toString();
                viewModel.guardar(binding.btnPerfil.getText().toString(), nombre, apellido, dni, telefono, email);
            }
        });

        binding.btnClave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_nav_perfil_to_cambiarClaveFragment);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}