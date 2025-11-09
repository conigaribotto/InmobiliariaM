package com.example.inmobiliaria.ui.inquilinos;

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

public class InquilinosFragment extends Fragment {

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inf, @Nullable ViewGroup c, @Nullable Bundle b) {
        View v = inf.inflate(R.layout.fragment_inquilinos, c, false);

        InquilinosViewModel vm = new ViewModelProvider(this).get(InquilinosViewModel.class);

        RecyclerView rv = v.findViewById(R.id.rvInquilinos);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        InquilinosAdapter adapter = new InquilinosAdapter(a -> {
            Bundle args = new Bundle();
            args.putInt("contratoId", a.getIdContrato());
            Navigation.findNavController(v)
                    .navigate(R.id.action_nav_inquilinos_to_nav_inquilino_detalle, args);
        });
        rv.setAdapter(adapter);

        SwipeRefreshLayout swipe = v.findViewById(R.id.swipe);
        swipe.setOnRefreshListener(vm::cargar);

        vm.getContratos().observe(getViewLifecycleOwner(), adapter::submit);
        vm.getLoading().observe(getViewLifecycleOwner(), swipe::setRefreshing);

        vm.cargar();
        return v;
    }
}
