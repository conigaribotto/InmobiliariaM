package com.example.inmobiliaria.ui.pagos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.inmobiliaria.databinding.FragmentPagoBinding;

public class PagoFragment extends Fragment {

    private FragmentPagoBinding binding;
    private PagoViewModel vm;
    private PagoAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPagoBinding.inflate(inflater, container, false);
        vm = new ViewModelProvider(this).get(PagoViewModel.class);

        // Recycler + adapter
        adapter = new PagoAdapter();
        binding.rvPagos.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvPagos.setAdapter(adapter);

        // Observers
        vm.getPagos().observe(getViewLifecycleOwner(), adapter::submit);

        vm.getLoading().observe(getViewLifecycleOwner(), loading -> {
            binding.progressPagos.setVisibility(Boolean.TRUE.equals(loading) ? View.VISIBLE : View.GONE);
        });

        vm.getEmpty().observe(getViewLifecycleOwner(), empty -> {
            binding.tvVacioPagos.setVisibility(Boolean.TRUE.equals(empty) ? View.VISIBLE : View.GONE);
        });

        // Tomamos el contratoId de los argumentos y dispararmos la carga
        final int contratoId = requireArguments().getInt("contratoId", 0);
        vm.cargarPagos(contratoId);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
