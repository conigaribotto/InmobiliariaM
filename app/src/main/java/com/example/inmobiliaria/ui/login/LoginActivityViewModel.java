package com.example.inmobiliaria.ui.login;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivityViewModel extends AndroidViewModel {

    private final MutableLiveData<String> mensaje = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loginExitoso = new MutableLiveData<>();

    public LoginActivityViewModel(@NonNull Application application) { super(application); }

    public LiveData<String> getMensaje() { return mensaje; }
    public LiveData<Boolean> getLoginExitoso() { return loginExitoso; }

    public void login(String usuario, String clave) {
        if (usuario.isEmpty() || clave.isEmpty()) {
            mensaje.postValue("Debe ingresar usuario y contraseña");
            return;
        }

        ApiClient.getInmobiliariaService()
                .login(usuario, clave)
                .enqueue(new Callback<String>() {
                    @Override public void onResponse(Call<String> call, Response<String> resp) {
                        if (resp.isSuccessful() && resp.body() != null) {
                            String token = resp.body().trim().replace("\"", "");
                            if (token.startsWith("Bearer ")) token = token.substring(7).trim();
                            ApiClient.guardarToken(getApplication(), token);
                            mensaje.postValue("Inicio de sesión exitoso");
                            loginExitoso.postValue(true);
                        } else {
                            mensaje.postValue("Usuario o contraseña incorrectos");
                        }
                    }
                    @Override public void onFailure(Call<String> call, Throwable t) {
                        mensaje.postValue("Error de conexión: " + t.getMessage());
                        Log.e("LoginViewModel", String.valueOf(t));
                    }
                });
    }

    public void verificarSesion() {
        if (ApiClient.isTokenValido(getApplication())) {
            loginExitoso.postValue(true);
        }
    }
}


