package com.example.inmobiliaria.ui.login;

import androidx.lifecycle.ViewModel;

package com.example.lab3tp1.ui.login;

import android.app.Application;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.request.ApiClient;
import com.example.inmobiliaria.ui.menu.MenuActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends AndroidViewModel {

    public MutableLiveData<Boolean> loginExitoso = new MutableLiveData<>();

    public LoginViewModel(@NonNull Application app) {
        super(app);
    }

    public void login(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getApplication(), "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiClient.InmobiliariaService api = ApiClient.getApiInmobiliaria(getApplication());
        Call<String> call = api.login(email, password);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body();
                    ApiClient.saveToken(getApplication(), token);
                    loginExitoso.postValue(true);
                } else {
                    Toast.makeText(getApplication(), "Usuario o clave incorrectos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplication(), "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
