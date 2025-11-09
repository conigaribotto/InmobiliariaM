package com.example.inmobiliaria.ui.inquilinos;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.model.Alquiler;
import com.example.inmobiliaria.request.ApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InquilinosViewModel extends AndroidViewModel {
    private final MutableLiveData<List<Alquiler>> contratos = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);

    public InquilinosViewModel(@NonNull Application app) { super(app); }

    public LiveData<List<Alquiler>> getContratos() { return contratos; }
    public LiveData<Boolean> getLoading() { return loading; }

    public void cargar() {
        loading.postValue(true);
        ApiClient.getInmobiliariaService().obtenerContratos()
                .enqueue(new Callback<List<Alquiler>>() {
                    @Override public void onResponse(Call<List<Alquiler>> call, Response<List<Alquiler>> resp) {
                        loading.postValue(false);
                        contratos.postValue(resp.isSuccessful() && resp.body()!=null ? resp.body() : new ArrayList<>());
                    }
                    @Override public void onFailure(Call<List<Alquiler>> call, Throwable t) {
                        loading.postValue(false);
                        contratos.postValue(new ArrayList<>());
                    }
                });
    }
}


