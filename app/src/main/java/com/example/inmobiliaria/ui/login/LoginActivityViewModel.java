package com.example.inmobiliaria.ui.login;


import android.app.Application;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.request.ApiClient;
import com.example.inmobiliaria.ui.menu.MenuActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivityViewModel extends AndroidViewModel {

    private final MutableLiveData<Boolean> mLogin = new MutableLiveData<>();

    public LoginActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Boolean> getmLogin() {
        return mLogin;
    }

    public void login(String usuario, String clave) {

        // Validaciones básicas
        if (usuario == null || usuario.isEmpty() || clave == null || clave.isEmpty()) {
            Toast.makeText(getApplication(), "Usuario y clave requeridos", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiClient.InmobiliariaService api = ApiClient.getInmobiliariaService();
        Call<String> llamada = api.login(usuario, clave);

        llamada.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiClient.guardarToken(getApplication(), response.body());
                    Toast.makeText(getApplication(), "Bienvenido", Toast.LENGTH_SHORT).show();
                    navegarAMenu();
                    mLogin.postValue(true);
                } else {
                    Toast.makeText(getApplication(), "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                    mLogin.postValue(false);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplication(), "Error en la conexión", Toast.LENGTH_SHORT).show();
                mLogin.postValue(false);
            }
        });
    }

    private void navegarAMenu() {
        Intent intent = new Intent(getApplication(), MenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplication().startActivity(intent);
    }
}