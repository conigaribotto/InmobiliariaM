package com.example.inmobiliaria.ui.pagos;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.model.Pagos;
import com.example.inmobiliaria.request.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PagoViewModel extends AndroidViewModel {

    private final MutableLiveData<List<Pagos>> pagos = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> empty = new MutableLiveData<>(false);

    public PagoViewModel(@NonNull Application app) {
        super(app);
    }

    public LiveData<List<Pagos>> getPagos() { return pagos; }
    public LiveData<Boolean> getLoading() { return loading; }
    public LiveData<Boolean> getEmpty() { return empty; }

    public void cargarPagos(int contratoId) {
        loading.postValue(true);
        empty.postValue(false);


        Call<List<Pagos>> call = ApiClient.getInmobiliariaService()
                .obtenerPagosPorContrato(contratoId);

        call.enqueue(new Callback<List<Pagos>>() {
            @Override public void onResponse(Call<List<Pagos>> call, Response<List<Pagos>> response) {
                loading.postValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    List<Pagos> list = response.body();
                    pagos.postValue(list);
                    empty.postValue(list.isEmpty());
                } else {
                    pagos.postValue(null);
                    empty.postValue(true);
                }
            }
            @Override public void onFailure(Call<List<Pagos>> call, Throwable t) {
                loading.postValue(false);
                pagos.postValue(null);
                empty.postValue(true);
            }
        });
    }
}

