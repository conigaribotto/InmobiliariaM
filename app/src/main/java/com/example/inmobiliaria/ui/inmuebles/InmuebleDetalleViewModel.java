package com.example.inmobiliaria.ui.inmuebles;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.model.Inmueble;
import com.example.inmobiliaria.request.ApiClient;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InmuebleDetalleViewModel extends AndroidViewModel {

    private int inmuebleId = -1;

    private final MutableLiveData<String> titulo    = new MutableLiveData<>("");
    private final MutableLiveData<String> direccion = new MutableLiveData<>("");
    private final MutableLiveData<String> ambientes = new MutableLiveData<>("");
    private final MutableLiveData<String> precio    = new MutableLiveData<>("");
    private final MutableLiveData<String> estado    = new MutableLiveData<>("");

    public InmuebleDetalleViewModel(@NonNull Application app) { super(app); }

    public void init(int id) { this.inmuebleId = id; }

    public LiveData<String> getTitulo()    { return titulo; }
    public LiveData<String> getDireccion() { return direccion; }
    public LiveData<String> getAmbientes() { return ambientes; }
    public LiveData<String> getPrecio()    { return precio; }
    public LiveData<String> getEstado()    { return estado; }

    public void cargar() {
        ApiClient.getInmobiliariaService().obtenerInmuebles()
                .enqueue(new Callback<List<Inmueble>>() {
                    @Override public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> resp) {
                        List<Inmueble> lista = resp.isSuccessful() && resp.body()!=null ? resp.body() : Collections.emptyList();
                        for (Inmueble i : lista) {
                            if (i.getIdInmueble() == inmuebleId) {
                                titulo.postValue(nz(i.getTitulo()));
                                direccion.postValue(nz(i.getDireccion()));
                                ambientes.postValue("Ambientes: " + nz(i.getAmbientes()));

                                precio.postValue("");
                                estado.postValue("");
                                return;
                            }
                        }

                        titulo.postValue(""); direccion.postValue(""); ambientes.postValue("");
                        precio.postValue(""); estado.postValue("");
                    }
                    @Override public void onFailure(Call<List<Inmueble>> call, Throwable t) {
                        titulo.postValue(""); direccion.postValue(""); ambientes.postValue("");
                        precio.postValue(""); estado.postValue("");
                    }
                });
    }

    private String nz(Object o){ return o==null? "" : String.valueOf(o); }
}
