package com.example.inmobiliaria.ui.inmuebles;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.inmobiliaria.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class InmuebleFragment extends Fragment {

    private InmueblesViewModel vm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inf,
                             @Nullable ViewGroup c,
                             @Nullable Bundle b) {

        View v = inf.inflate(R.layout.fragment_inmueble, c, false);

        vm = new ViewModelProvider(this).get(InmueblesViewModel.class);

        RecyclerView rv = v.findViewById(R.id.recycler);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        InmuebleAdapter adapter = new InmuebleAdapter(i -> {
            Bundle args = new Bundle();
            args.putInt("inmuebleId", i.getIdInmueble());
            Navigation.findNavController(v)
                    .navigate(R.id.action_nav_inmuebles_to_nav_inmueble_detalle, args);
        });
        rv.setAdapter(adapter);

        SwipeRefreshLayout swipe = v.findViewById(R.id.swipeRefresh);
        swipe.setOnRefreshListener(vm::cargar);

        vm.getInmuebles().observe(getViewLifecycleOwner(), adapter::submit);
        vm.getLoading().observe(getViewLifecycleOwner(), swipe::setRefreshing);

        FloatingActionButton fab = v.findViewById(R.id.fabAgregar);
        fab.setOnClickListener(view ->
                Navigation.findNavController(v)
                        .navigate(R.id.action_nav_inmuebles_to_nav_agregar_inmueble)
        );

        vm.cargar();
        return v;
    }
}
