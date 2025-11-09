package com.example.inmobiliaria.ui.inquilinos;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.example.inmobiliaria.model.Alquiler;
import com.example.inmobiliaria.model.Inquilino;
import com.example.inmobiliaria.request.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InquilinoDetalleViewModel extends AndroidViewModel {

    private final SavedStateHandle state;

    private final MutableLiveData<String> nombreCompleto = new MutableLiveData<>("");
    private final MutableLiveData<String> dni = new MutableLiveData<>("");
    private final MutableLiveData<String> telefono = new MutableLiveData<>("");
    private final MutableLiveData<String> email = new MutableLiveData<>("");
    private final MutableLiveData<String> direccionInmueble = new MutableLiveData<>("");

    public InquilinoDetalleViewModel(@NonNull Application app, SavedStateHandle state) {
        super(app);
        this.state = state;
    }

    public LiveData<String> getNombreCompleto()   { return nombreCompleto; }
    public LiveData<String> getDni()              { return dni; }
    public LiveData<String> getTelefono()         { return telefono; }
    public LiveData<String> getEmail()            { return email; }
    public LiveData<String> getDireccionInmueble(){ return direccionInmueble; }

    public void cargar() {
        int contratoId = state.get("contratoId");
        ApiClient.getInmobiliariaService().obtenerContratos()
                .enqueue(new Callback<List<Alquiler>>() {
                    @Override public void onResponse(Call<List<Alquiler>> call, Response<List<Alquiler>> resp) {
                        List<Alquiler> lista = resp.isSuccessful() && resp.body()!=null ? resp.body() : java.util.Collections.emptyList();
                        for (Alquiler a : lista) {
                            if (a.getIdContrato() == contratoId) {
                                Inquilino inq = a.getInquilino();
                                nombreCompleto.postValue(inq!=null ? (nz(inq.getNombre())+" "+nz(inq.getApellido())) : "");
                                dni.postValue("DNI: " + (inq!=null ? nz(inq.getDni()) : ""));
                                telefono.postValue("Tel: " + (inq!=null ? nz(inq.getTelefono()) : ""));
                                email.postValue("Email: " + (inq!=null ? nz(inq.getEmail()) : ""));
                                direccionInmueble.postValue("Inmueble: " + (a.getInmueble()!=null ? nz(a.getInmueble().getDireccion()) : ""));
                                return;
                            }
                        }
                        nombreCompleto.postValue(""); dni.postValue("");
                        telefono.postValue(""); email.postValue(""); direccionInmueble.postValue("");
                    }
                    @Override public void onFailure(Call<List<Alquiler>> call, Throwable t) {
                        nombreCompleto.postValue(""); dni.postValue("");
                        telefono.postValue(""); email.postValue(""); direccionInmueble.postValue("");
                    }
                });
    }

    private String nz(Object o){ return o==null? "" : String.valueOf(o); }
}

