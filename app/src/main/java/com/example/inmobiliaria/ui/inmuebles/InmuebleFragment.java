package com.example.inmobiliaria.ui.inmuebles;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.inmobiliaria.R;
import com.example.inmobiliaria.databinding.FragmentInmuebleBinding;
import com.example.inmobiliaria.model.Inmueble;

public class InmuebleFragment extends Fragment {

    private FragmentInmuebleBinding binding;
    private InmueblesViewModel viewModel;
    private InmuebleAdapter adapter;
    private ActivityResultLauncher<String> pickImage;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInmuebleBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(InmueblesViewModel.class);

        adapter = new InmuebleAdapter(new InmuebleAdapter.InmuebleListener() {
            @Override public void onToggleClick(Inmueble item) {
                viewModel.alternarHabilitado(item);
            }
            @Override public void onItemClick(Inmueble item) {
                Bundle args = new Bundle();
                args.putInt("inmuebleId", item.getIdInmueble());
                NavHostFragment.findNavController(InmuebleFragment.this)
                        .navigate(R.id.action_nav_inmuebles_to_nav_contratos, args);
            }
        });
        binding.recycler.setAdapter(adapter);

        // Observers (sin ifs)
        viewModel.getInmuebles().observe(getViewLifecycleOwner(), adapter::submitList);
        viewModel.getLoading().observe(getViewLifecycleOwner(), isLoading -> {
            binding.swipeRefresh.setRefreshing(Boolean.TRUE.equals(isLoading));
        });
        viewModel.getSnackbar().observe(getViewLifecycleOwner(), msg -> {
            android.widget.Toast.makeText(requireContext(), msg, android.widget.Toast.LENGTH_SHORT).show();
        });

        binding.swipeRefresh.setOnRefreshListener(viewModel::cargarInmuebles);

        pickImage = registerForActivityResult(new ActivityResultContracts.GetContent(),
                (Uri uri) -> viewModel.setFotoUriSeleccionada(uri));
        binding.btnFoto.setOnClickListener(v -> pickImage.launch("image/*"));

        binding.btnCrear.setOnClickListener(v -> {
            viewModel.setNuevoTitulo(binding.etTitulo.getText().toString());
            viewModel.setNuevaDescripcion(binding.etDescripcion.getText().toString());
            viewModel.setNuevaDireccion(binding.etDireccion.getText().toString());
            viewModel.crearInmueble();
        });

        viewModel.cargarInmuebles();
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
