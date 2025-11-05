package com.example.inmobiliaria.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
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

        // Observa mensajes
        viewModel.getMensaje().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String msg) {
                Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

        // Observa éxito de login
        viewModel.getLoginExitoso().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean ok) {
                if (ok) {
                    Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        // Listener del botón
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuario = binding.etUsuario.getText().toString();
                String clave = binding.etClave.getText().toString();
                viewModel.login(usuario, clave);
            }
        });

        // Si ya tiene token, ir directo al menú
        viewModel.verificarSesion();
    }
}
