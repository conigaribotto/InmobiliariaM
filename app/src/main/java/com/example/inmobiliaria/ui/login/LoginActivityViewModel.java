package com.example.inmobiliaria.ui.login;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivityViewModel extends AndroidViewModel {

    private MutableLiveData<String> mensaje = new MutableLiveData<>();
    private MutableLiveData<Boolean> loginExitoso = new MutableLiveData<>();

    public LoginActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<String> getMensaje() {
        return mensaje;
    }

    public LiveData<Boolean> getLoginExitoso() {
        return loginExitoso;
    }

    public void login(String usuario, String clave) {
        if (usuario.isEmpty() || clave.isEmpty()) {
            mensaje.postValue("Debe ingresar usuario y contraseña");
            return;
        }

        ApiClient.InmobiliariaService api = ApiClient.getInmobiliariaService();
        Call<String> call = api.login(usuario, clave);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body();
                    ApiClient.guardarToken(getApplication(), token);
                    mensaje.postValue("Inicio de sesión exitoso");
                    loginExitoso.postValue(true);
                } else {
                    mensaje.postValue("Usuario o contraseña incorrectos");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                mensaje.postValue("Error de conexión: " + t.getMessage());
                Log.e("LoginViewModel", t.toString());
            }
        });
    }

    public void verificarSesion() {
        String token = ApiClient.obtenerToken(getApplication());
        if (token != null && !token.isEmpty()) {
            loginExitoso.postValue(true);
        }
    }
}
