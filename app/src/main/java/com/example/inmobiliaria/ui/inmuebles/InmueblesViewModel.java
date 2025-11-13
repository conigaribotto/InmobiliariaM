package com.example.inmobiliaria.ui.inmuebles;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.model.Inmueble;
import com.example.inmobiliaria.request.ApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InmueblesViewModel extends AndroidViewModel {

    private final MutableLiveData<List<Inmueble>> inmuebles = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);

    public InmueblesViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Inmueble>> getInmuebles() { return inmuebles; }
    public LiveData<Boolean> getLoading() { return loading; }

    public void cargar() {
        loading.postValue(true);
        ApiClient.getInmobiliariaService()
                .obtenerInmuebles()
                .enqueue(new Callback<List<Inmueble>>() {
                    @Override
                    public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> resp) {
                        loading.postValue(false);
                        if (resp.isSuccessful() && resp.body() != null) {
                            inmuebles.postValue(resp.body());
                        } else {
                            inmuebles.postValue(new ArrayList<>());
                            Toast.makeText(getApplication(), "No se pudieron cargar los inmuebles", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Inmueble>> call, Throwable t) {
                        loading.postValue(false);
                        inmuebles.postValue(new ArrayList<>());
                        Toast.makeText(getApplication(), "Red: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}

