package com.example.inmobiliaria.ui.contratos;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.model.Alquiler;
import com.example.inmobiliaria.request.ApiClient;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContratosDetalleViewModel extends AndroidViewModel {

    private int contratoId = -1;

    private final MutableLiveData<String> inquilino = new MutableLiveData<>("");
    private final MutableLiveData<String> inmueble  = new MutableLiveData<>("");
    private final MutableLiveData<String> fechas    = new MutableLiveData<>("");
    private final MutableLiveData<String> monto     = new MutableLiveData<>("");

    public ContratosDetalleViewModel(@NonNull Application app) { super(app); }

    /** Llamado desde el Fragment */
    public void init(int id) { this.contratoId = id; }

    public LiveData<String> getInquilino() { return inquilino; }
    public LiveData<String> getInmueble()  { return inmueble; }
    public LiveData<String> getFechas()    { return fechas; }
    public LiveData<String> getMonto()     { return monto; }

    public void cargar() {
        ApiClient.getInmobiliariaService().obtenerContratos()
                .enqueue(new Callback<List<Alquiler>>() {
                    @Override public void onResponse(Call<List<Alquiler>> call, Response<List<Alquiler>> resp) {
                        List<Alquiler> lista = resp.isSuccessful() && resp.body()!=null
                                ? resp.body() : Collections.emptyList();

                        for (Alquiler a : lista) {
                            if (a.getIdContrato() == contratoId) {
                                String nom = a.getInquilino()!=null
                                        ? nz(a.getInquilino().getNombre()) + " " + nz(a.getInquilino().getApellido())
                                        : "";
                                String dir = a.getInmueble()!=null ? nz(a.getInmueble().getDireccion()) : "";

                                // Tus fechas son String en el modelo -> las usamos directo
                                String f1 = nz(a.getFechaInicio());
                                String f2 = nz(a.getFechaFinalizacion());
                                String periodo = (f1.isEmpty() && f2.isEmpty())
                                        ? ("Contrato #" + a.getIdContrato())
                                        : ("Desde " + f1 + " hasta " + f2);

                                // Monto con getMontoAlquiler()
                                String montoTxt = "$ " + a.getMontoAlquiler();

                                inquilino.postValue(nom);
                                inmueble.postValue(dir);
                                fechas.postValue(periodo);
                                monto.postValue(montoTxt);
                                return;
                            }
                        }
                        // No encontrado → publicar vacíos (la UI no necesita if)
                        inquilino.postValue(""); inmueble.postValue(""); fechas.postValue(""); monto.postValue("");
                    }

                    @Override public void onFailure(Call<List<Alquiler>> call, Throwable t) {
                        inquilino.postValue(""); inmueble.postValue(""); fechas.postValue(""); monto.postValue("");
                    }
                });
    }

    private String nz(Object o){ return o==null? "" : String.valueOf(o); }
}
