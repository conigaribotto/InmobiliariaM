package com.example.inmobiliaria.ui.inmuebles;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.inmobiliaria.R;

public class InmuebleFragment extends Fragment {

    private InmueblesViewModel vm;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inf, @Nullable ViewGroup c, @Nullable Bundle b) {
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

        EditText etTit  = v.findViewById(R.id.etTitulo);
        EditText etDesc = v.findViewById(R.id.etDescripcion);
        EditText etDir  = v.findViewById(R.id.etDireccion);
        Button btnCrear = v.findViewById(R.id.btnCrear);
        btnCrear.setOnClickListener(view ->
                vm.crear(etTit.getText().toString(), etDesc.getText().toString(), etDir.getText().toString())
        );

        vm.getInmuebles().observe(getViewLifecycleOwner(), adapter::submit);
        vm.getLoading().observe(getViewLifecycleOwner(), swipe::setRefreshing);

        vm.cargar(); // siempre carga, la VM decide si refresca o reutiliza cache
        return v;
    }
}

