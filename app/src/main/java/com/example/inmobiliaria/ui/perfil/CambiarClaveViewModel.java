package com.example.inmobiliaria.ui.perfil;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.model.Propietario;
import com.example.inmobiliaria.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CambiarClaveViewModel extends AndroidViewModel {

    private MutableLiveData<Boolean> limpiarCampos = new MutableLiveData<>();

    public LiveData<Boolean> getLimpiarCampos() {
        return limpiarCampos;
    }
    public CambiarClaveViewModel(@NonNull Application application) {
        super(application);
    }

    public void cambiarClave(String actual, String nueva, String confirmar){
        if (actual.isEmpty() || nueva.isEmpty() || confirmar.isEmpty()) {
            Toast.makeText(getApplication(), "No pueden haber campos vacíos", Toast.LENGTH_SHORT).show();

            return;
        }
        if (!nueva.equals(confirmar)) {
            Toast.makeText(getApplication(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        String token = ApiClient.obtenerToken(getApplication());
        Call<Void> llamada = ApiClient.getInmobiliariaService().cambiarClave("Bearer " + token,actual,nueva);
        llamada.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplication(), "Contraseña cambiada con éxito", Toast.LENGTH_SHORT).show();
                    limpiarCampos.postValue(true);
                } else {
                    Toast.makeText(getApplication(), "Error al cambiar contraseña", Toast.LENGTH_SHORT).show();
                    Log.e("Error", response.message());
                    limpiarCampos.postValue(true);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getApplication(), "Error de servidor", Toast.LENGTH_SHORT).show();
                Log.e("CambiarClave", t.getMessage());
                limpiarCampos.postValue(true);
            }
        });
    }

    public void resetearLimpiarCampos() {
        limpiarCampos.setValue(false);
    }


}