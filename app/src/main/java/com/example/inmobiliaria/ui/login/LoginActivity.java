package com.example.inmobiliaria.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.inmobiliaria.databinding.ActivityLoginBinding;
import com.example.inmobiliaria.ui.menu.MenuActivity;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private LoginActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(LoginActivityViewModel.class);

        viewModel.getMensaje().observe(this,
                msg -> Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show());

        viewModel.getLoginExitoso().observe(this, ok -> {
            if (ok) {
                startActivity(new Intent(LoginActivity.this, MenuActivity.class));
                finish();
            }
        });

        binding.btnLogin.setOnClickListener((View v) -> {
            String usuario = binding.etUsuario.getText().toString();
            String clave = binding.etClave.getText().toString();
            viewModel.login(usuario, clave);
        });

        // Si ya hay token válido, ir al Menú
        viewModel.verificarSesion();
    }
}
