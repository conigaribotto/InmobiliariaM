package com.example.inmobiliaria.ui.inmuebles;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.model.Inmueble;
import com.example.inmobiliaria.request.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InmueblesViewModel extends AndroidViewModel {

    private final MutableLiveData<List<Inmueble>> inmuebles = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);

    public InmueblesViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Inmueble>> getInmuebles() { return inmuebles; }
    public LiveData<String> getError() { return error; }
    public LiveData<Boolean> getLoading() { return loading; }

    private void emitirError(String msg) {
        if (msg == null) msg = "";
        error.postValue(msg.trim());
    }

    public void cargarInmuebles() {
        loading.setValue(true);
        String token = ApiClient.obtenerToken(getApplication());
        if (token == null) {
            emitirError("No autenticado");
            inmuebles.setValue(null);
            loading.setValue(false);
            return;
        }

        ApiClient.InmobiliariaService api = ApiClient.getInmobiliariaService();
        api.obtenerInmueblesPorPropietario("Bearer " + token)
                .enqueue(new Callback<List<Inmueble>>() {
                    @Override
                    public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> response) {
                        loading.postValue(false);
                        if (response.isSuccessful() && response.body() != null) {
                            inmuebles.postValue(response.body());
                            emitirError("");
                        } else {
                            inmuebles.postValue(null);
                            emitirError("No se pudieron cargar los inmuebles");
                            Log.d("InmueblesVM", "Response: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Inmueble>> call, Throwable t) {
                        loading.postValue(false);
                        inmuebles.postValue(null);
                        emitirError("Error de conexión");
                        Log.e("InmueblesVM", "Failure: " + t.getMessage());
                    }
                });
    }

    public void toggleHabilitado(int inmuebleId, boolean habilitar) {
        loading.setValue(true);
        String token = ApiClient.obtenerToken(getApplication());
        if (token == null) {
            emitirError("No autenticado");
            loading.setValue(false);
            return;
        }

        ApiClient.InmobiliariaService api = ApiClient.getInmobiliariaService();
        Call<Void> call = habilitar ? api.habilitarInmueble("Bearer " + token, inmuebleId)
                : api.deshabilitarInmueble("Bearer " + token, inmuebleId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                loading.postValue(false);
                if (response.isSuccessful()) {
                    cargarInmuebles();
                    emitirError("");
                } else {
                    emitirError("Error al actualizar estado");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                loading.postValue(false);
                emitirError("Error de conexión");
                Log.e("InmueblesVM", "Toggle failure: " + t.getMessage());
            }
        });
    }
}
