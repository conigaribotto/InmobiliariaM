package com.example.inmobiliaria.ui.logout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.inmobiliaria.databinding.FragmentLogoutBinding;
import com.example.inmobiliaria.ui.login.LoginActivity;

public class LogoutFragment extends Fragment {

    private FragmentLogoutBinding binding;
    private LogoutViewModel vm;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLogoutBinding.inflate(inflater, container, false);
        vm = new ViewModelProvider(this).get(LogoutViewModel.class);

        binding.btnConfirmarLogout.setOnClickListener(v -> vm.confirmarLogout());


        binding.btnCancelarLogout.setOnClickListener(v ->
                requireActivity().onBackPressed()
        );

        vm.getNavigateToLogin().observe(getViewLifecycleOwner(), event -> {
            Intent intent = new Intent(requireActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            requireActivity().finish();
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}


