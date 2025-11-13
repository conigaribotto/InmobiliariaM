package com.example.inmobiliaria.ui.inquilinos;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.model.Alquiler;
import com.example.inmobiliaria.model.Inquilino;
import com.example.inmobiliaria.request.ApiClient;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InquilinoDetalleViewModel extends AndroidViewModel {

    private int contratoId = -1;

    private final MutableLiveData<String> nombreCompleto      = new MutableLiveData<>("");
    private final MutableLiveData<String> dni                 = new MutableLiveData<>("");
    private final MutableLiveData<String> telefono            = new MutableLiveData<>("");
    private final MutableLiveData<String> email               = new MutableLiveData<>("");
    private final MutableLiveData<String> direccionInmueble   = new MutableLiveData<>("");
    private final MutableLiveData<String> fechasContrato      = new MutableLiveData<>("");
    private final MutableLiveData<String> montoContrato       = new MutableLiveData<>("");

    public InquilinoDetalleViewModel(@NonNull Application app) {
        super(app);
    }

    /** Se llama desde el Fragment, una sola vez */
    public void init(int contratoId) {
        this.contratoId = contratoId;
    }

    public LiveData<String> getNombreCompleto()    { return nombreCompleto; }
    public LiveData<String> getDni()              { return dni; }
    public LiveData<String> getTelefono()         { return telefono; }
    public LiveData<String> getEmail()            { return email; }
    public LiveData<String> getDireccionInmueble(){ return direccionInmueble; }
    public LiveData<String> getFechasContrato()   { return fechasContrato; }
    public LiveData<String> getMontoContrato()    { return montoContrato; }

    public void cargar() {
        ApiClient.getInmobiliariaService()
                .obtenerContratos()
                .enqueue(new Callback<List<Alquiler>>() {
                    @Override
                    public void onResponse(Call<List<Alquiler>> call,
                                           Response<List<Alquiler>> resp) {
                        List<Alquiler> lista = resp.isSuccessful() && resp.body() != null
                                ? resp.body()
                                : Collections.emptyList();

                        for (Alquiler a : lista) {
                            if (a.getIdContrato() == contratoId) {

                                Inquilino inq = a.getInquilino();

                                String nom = inq != null
                                        ? nz(inq.getNombre()) + " " + nz(inq.getApellido())
                                        : "";

                                String dniTxt  = "DNI: " + (inq != null ? nz(inq.getDni()) : "");
                                String telTxt  = "Tel: " + (inq != null ? nz(inq.getTelefono()) : "");
                                String mailTxt = "Email: " + (inq != null ? nz(inq.getEmail()) : "");

                                String dirTxt = "Inmueble: " +
                                        (a.getInmueble() != null ? nz(a.getInmueble().getDireccion()) : "");

                                String f1 = nz(a.getFechaInicio());
                                String f2 = nz(a.getFechaFinalizacion());
                                String periodo;
                                if (!f1.isEmpty() || !f2.isEmpty()) {
                                    periodo = "Contrato: " + (f1.isEmpty() ? "?" : f1)
                                            + " a " + (f2.isEmpty() ? "?" : f2);
                                } else {
                                    periodo = "Contrato #" + a.getIdContrato();
                                }

                                String montoTxt = "Monto: $ " + a.getMontoAlquiler();

                                nombreCompleto.postValue(nom);
                                dni.postValue(dniTxt);
                                telefono.postValue(telTxt);
                                email.postValue(mailTxt);
                                direccionInmueble.postValue(dirTxt);
                                fechasContrato.postValue(periodo);
                                montoContrato.postValue(montoTxt);
                                return;
                            }
                        }

                        limpiar();
                    }

                    @Override
                    public void onFailure(Call<List<Alquiler>> call, Throwable t) {
                        limpiar();
                    }
                });
    }

    private void limpiar() {
        nombreCompleto.postValue("");
        dni.postValue("");
        telefono.postValue("");
        email.postValue("");
        direccionInmueble.postValue("");
        fechasContrato.postValue("");
        montoContrato.postValue("");
    }

    private String nz(Object o) {
        return o == null ? "" : String.valueOf(o);
    }
}
