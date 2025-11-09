package com.example.inmobiliaria.ui.menu;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.model.Propietario;
import com.example.inmobiliaria.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuViewModel extends AndroidViewModel {

    private final MutableLiveData<Propietario> propietario = new MutableLiveData<>();

    public MenuViewModel(@NonNull Application app) { super(app); }

    public LiveData<Propietario> getPropietario() { return propietario; }

    public void cargarPropietario() {
        ApiClient.getInmobiliariaService()
                .obtenerPropietario()
                .enqueue(new Callback<Propietario>() {
                    @Override public void onResponse(Call<Propietario> call, Response<Propietario> resp) {
                        if (resp.isSuccessful()) propietario.postValue(resp.body());
                    }
                    @Override public void onFailure(Call<Propietario> call, Throwable t) { }
                });
    }
}

