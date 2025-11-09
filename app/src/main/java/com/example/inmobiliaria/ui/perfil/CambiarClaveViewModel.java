package com.example.inmobiliaria.ui.perfil;

import android.app.Application;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CambiarClaveViewModel extends AndroidViewModel {

    private final MutableLiveData<Boolean> limpiarCampos = new MutableLiveData<>(false);
    public LiveData<Boolean> getLimpiarCampos() { return limpiarCampos; }

    public CambiarClaveViewModel(@NonNull Application application) { super(application); }

    public void cambiarClave(String actual, String nueva, String confirmar) {
        if (TextUtils.isEmpty(actual) || TextUtils.isEmpty(nueva) || TextUtils.isEmpty(confirmar)) {
            Toast.makeText(getApplication(), "No pueden haber campos vacíos", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!nueva.equals(confirmar)) {
            Toast.makeText(getApplication(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiClient.getInmobiliariaService()
                .cambiarClave(actual, nueva)  // Interceptor agrega el Bearer
                .enqueue(new Callback<Void>() {
                    @Override public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getApplication(), "Contraseña cambiada con éxito", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplication(), "Error al cambiar contraseña", Toast.LENGTH_SHORT).show();
                        }
                        limpiarCampos.postValue(true);
                    }
                    @Override public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getApplication(), "Error de servidor", Toast.LENGTH_SHORT).show();
                        limpiarCampos.postValue(true);
                    }
                });
    }

    public void resetearLimpiarCampos() { limpiarCampos.setValue(false); }
}
