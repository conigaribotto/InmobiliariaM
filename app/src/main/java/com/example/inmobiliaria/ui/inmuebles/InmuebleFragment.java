package com.example.inmobiliaria.ui.inmuebles;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inmobiliaria.R;

public class InmuebleFragment extends Fragment implements InmuebleAdapter.OnToggleListener {

    private InmueblesViewModel viewModel;
    private InmuebleAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_inmueble, container, false);

        RecyclerView recycler = root.findViewById(R.id.recyclerInmuebles);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new InmuebleAdapter();
        adapter.setOnToggleListener(this);
        recycler.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(InmueblesViewModel.class);

        viewModel.getInmuebles().observe(getViewLifecycleOwner(), adapter::setItems);

        viewModel.getError().observe(getViewLifecycleOwner(), msg ->
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show()
        );

        viewModel.getLoading().observe(getViewLifecycleOwner(), loading -> {
        });

        viewModel.cargarInmuebles();

        return root;
    }

    @Override
    public void onToggle(int inmuebleId, boolean habilitar) {
        viewModel.toggleHabilitado(inmuebleId, habilitar);
    }
}
