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

    private final MutableLiveData<String> titulo     = new MutableLiveData<>("");
    private final MutableLiveData<String> direccion  = new MutableLiveData<>("");
    private final MutableLiveData<String> uso        = new MutableLiveData<>("");
    private final MutableLiveData<String> tipo       = new MutableLiveData<>("");
    private final MutableLiveData<String> ambientes  = new MutableLiveData<>("");
    private final MutableLiveData<String> superficie = new MutableLiveData<>("");
    private final MutableLiveData<String> valor      = new MutableLiveData<>("");
    private final MutableLiveData<String> latitud    = new MutableLiveData<>("");
    private final MutableLiveData<String> longitud   = new MutableLiveData<>("");
    private final MutableLiveData<String> estado     = new MutableLiveData<>("");
    private final MutableLiveData<String> fotoUrl    = new MutableLiveData<>("");

    public InmuebleDetalleViewModel(@NonNull Application app) {
        super(app);
    }

    public void init(int id) {
        this.inmuebleId = id;
    }

    public LiveData<String> getTitulo()     { return titulo; }
    public LiveData<String> getDireccion()  { return direccion; }
    public LiveData<String> getUso()        { return uso; }
    public LiveData<String> getTipo()       { return tipo; }
    public LiveData<String> getAmbientes()  { return ambientes; }
    public LiveData<String> getSuperficie() { return superficie; }
    public LiveData<String> getValor()      { return valor; }
    public LiveData<String> getLatitud()    { return latitud; }
    public LiveData<String> getLongitud()   { return longitud; }
    public LiveData<String> getEstado()     { return estado; }
    public LiveData<String> getFotoUrl()    { return fotoUrl; }

    public void cargar() {
        ApiClient.getInmobiliariaService()
                .obtenerInmuebles()
                .enqueue(new Callback<List<Inmueble>>() {
                    @Override
                    public void onResponse(Call<List<Inmueble>> call,
                                           Response<List<Inmueble>> resp) {

                        List<Inmueble> lista = resp.isSuccessful() && resp.body() != null
                                ? resp.body()
                                : Collections.emptyList();

                        for (Inmueble i : lista) {
                            if (i.getIdInmueble() == inmuebleId) {

                                titulo.postValue(nz(i.getTitulo()));
                                direccion.postValue(nz(i.getDireccion()));
                                uso.postValue("Uso: " + nz(i.getUso()));
                                tipo.postValue("Tipo: " + nz(i.getTipo()));
                                ambientes.postValue("Ambientes: " + i.getAmbientes());
                                superficie.postValue("Superficie: " + i.getSuperficie() + " m²");
                                valor.postValue("Valor: $ " + nz(i.getValor()));
                                latitud.postValue("Latitud: " + nz(i.getLatitud()));
                                longitud.postValue("Longitud: " + nz(i.getLongitud()));
                                estado.postValue(i.isDisponible()
                                        ? "Estado: Disponible"
                                        : "Estado: No disponible");
                                fotoUrl.postValue(i.getFotoUrl());

                                return;
                            }
                        }

                        // Si no lo encontró limpio todo
                        limpiar();
                    }

                    @Override
                    public void onFailure(Call<List<Inmueble>> call, Throwable t) {
                        limpiar();
                    }
                });
    }

    private void limpiar() {
        titulo.postValue("");
        direccion.postValue("");
        uso.postValue("");
        tipo.postValue("");
        ambientes.postValue("");
        superficie.postValue("");
        valor.postValue("");
        latitud.postValue("");
        longitud.postValue("");
        estado.postValue("");
        fotoUrl.postValue("");
    }

    private String nz(Object o) {
        return o == null ? "" : String.valueOf(o);
    }
}
