package com.example.inmobiliaria.ui.login;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.inmobiliaria.ui.login.LoginActivityViewModel;
import com.example.inmobiliaria.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private LoginActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(LoginActivityViewModel.class);
        setContentView(binding.getRoot());

        // Observa el resultado del login (el ViewModel maneja la navegación)
        viewModel.getmLogin().observe(this, loginOk -> {
            // La Activity no hace lógica, solo reacciona al evento
        });

        // Botón de login
        binding.btnLogin.setOnClickListener(v -> {
            String usuario = binding.etUsuario.getText().toString();
            String clave = binding.etClave.getText().toString();
            viewModel.login(usuario, clave);
        });
    }
}
