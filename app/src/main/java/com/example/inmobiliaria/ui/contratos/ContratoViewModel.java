package com.example.inmobiliaria.ui.contratos;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.model.Alquiler;
import com.example.inmobiliaria.request.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContratoViewModel extends AndroidViewModel {

    private final MutableLiveData<List<Alquiler>> contratos = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> empty = new MutableLiveData<>(false);
    private final MutableLiveData<Integer> navigateToPagos = new MutableLiveData<>(null);

    public ContratoViewModel(@NonNull Application app) {
        super(app);
    }

    public LiveData<List<Alquiler>> getContratos() { return contratos; }
    public LiveData<Boolean> getLoading() { return loading; }
    public LiveData<Boolean> getEmpty() { return empty; }
    public LiveData<Integer> getNavigateToPagos() { return navigateToPagos; }

    public void cargarContratos(int inmuebleId) {
        loading.postValue(true);
        empty.postValue(false);

        String token = ApiClient.obtenerToken(getApplication());
        Call<List<Alquiler>> call = ApiClient.getInmobiliariaService()
                .obtenerContratosPorInmueble("Bearer " + token, inmuebleId);

        call.enqueue(new Callback<List<Alquiler>>() {
            @Override public void onResponse(Call<List<Alquiler>> call, Response<List<Alquiler>> response) {
                loading.postValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    List<Alquiler> list = response.body();
                    contratos.postValue(list);
                    empty.postValue(list.isEmpty());
                } else {
                    contratos.postValue(null);
                    empty.postValue(true);
                }
            }
            @Override public void onFailure(Call<List<Alquiler>> call, Throwable t) {
                loading.postValue(false);
                contratos.postValue(null);
                empty.postValue(true);
            }
        });
    }

    public void onVerPagosClick(Alquiler contrato) {
        if (contrato != null) {
            navigateToPagos.postValue(contrato.getIdAlquiler());
            // reset para un sólo disparo
            navigateToPagos.postValue(null);
        }
    }
}
