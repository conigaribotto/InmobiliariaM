package com.example.inmobiliaria.ui.perfil;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.model.Propietario;
import com.example.inmobiliaria.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilViewModel extends AndroidViewModel {

    private final MutableLiveData<String> nombre   = new MutableLiveData<>("");
    private final MutableLiveData<String> apellido = new MutableLiveData<>("");
    private final MutableLiveData<String> dni      = new MutableLiveData<>("");
    private final MutableLiveData<String> email    = new MutableLiveData<>("");
    private final MutableLiveData<String> telefono = new MutableLiveData<>("");

    private final MutableLiveData<Boolean> editable    = new MutableLiveData<>(false);
    private final MutableLiveData<String>  botonTexto  = new MutableLiveData<>("Editar");

    private Propietario propietarioCache;

    public PerfilViewModel(@NonNull Application app) { super(app); }

    public LiveData<String> getNombre()   { return nombre; }
    public LiveData<String> getApellido() { return apellido; }
    public LiveData<String> getDni()      { return dni; }
    public LiveData<String> getEmail()    { return email; }
    public LiveData<String> getTelefono() { return telefono; }
    public LiveData<Boolean> getEditable(){ return editable; }
    public LiveData<String> getBotonTexto(){ return botonTexto; }

    public void cargarPropietario() {
        ApiClient.getInmobiliariaService()
                .obtenerPropietario()
                .enqueue(new Callback<Propietario>() {
                    @Override public void onResponse(Call<Propietario> call, Response<Propietario> resp) {
                        if (resp.isSuccessful() && resp.body() != null) {
                            propietarioCache = resp.body();
                            nombre.postValue(safe(propietarioCache.getNombre()));
                            apellido.postValue(safe(propietarioCache.getApellido()));
                            dni.postValue(safe(propietarioCache.getDni()));
                            email.postValue(safe(propietarioCache.getEmail()));
                            telefono.postValue(safe(propietarioCache.getTelefono()));

                            editable.postValue(false);
                            botonTexto.postValue("Editar");
                        } else {
                            Toast.makeText(getApplication(), "No se pudo cargar el perfil", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override public void onFailure(Call<Propietario> call, Throwable t) {
                        Toast.makeText(getApplication(), "Error de conexión", Toast.LENGTH_SHORT).show();
                        Log.e("PerfilVM", "leerPropietario: " + t.getMessage());
                    }
                });
    }

    public void onClickBotonPrincipal(String textoBotonActual,
                                      String nombreIn,
                                      String apellidoIn,
                                      String dniIn,
                                      String telefonoIn,
                                      String emailIn) {

        if ("Editar".equalsIgnoreCase(textoBotonActual)) {
            editable.setValue(true);
            botonTexto.setValue("Guardar");
            return;
        }

        if (!validar(nombreIn, apellidoIn, dniIn, telefonoIn, emailIn)) return;
        if (propietarioCache == null) {
            Toast.makeText(getApplication(), "Perfil no cargado", Toast.LENGTH_SHORT).show();
            return;
        }

        propietarioCache.setNombre(nombreIn);
        propietarioCache.setApellido(apellidoIn);
        propietarioCache.setDni(dniIn);
        propietarioCache.setTelefono(telefonoIn);
        propietarioCache.setEmail(emailIn);

        ApiClient.getInmobiliariaService()
                .actualizarPropietario(propietarioCache)
                .enqueue(new Callback<Propietario>() {
                    @Override public void onResponse(Call<Propietario> call, Response<Propietario> resp) {
                        if (resp.isSuccessful() && resp.body() != null) {
                            propietarioCache = resp.body();
                            nombre.setValue(safe(propietarioCache.getNombre()));
                            apellido.setValue(safe(propietarioCache.getApellido()));
                            dni.setValue(safe(propietarioCache.getDni()));
                            email.setValue(safe(propietarioCache.getEmail()));
                            telefono.setValue(safe(propietarioCache.getTelefono()));

                            editable.setValue(false);
                            botonTexto.setValue("Editar");
                            Toast.makeText(getApplication(), "Perfil actualizado", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplication(), "Error al actualizar", Toast.LENGTH_SHORT).show();
                            editable.setValue(false);
                            botonTexto.setValue("Editar");
                        }
                    }
                    @Override public void onFailure(Call<Propietario> call, Throwable t) {
                        Toast.makeText(getApplication(), "Error de servidor", Toast.LENGTH_SHORT).show();
                        editable.setValue(false);
                        botonTexto.setValue("Editar");
                    }
                });
    }

    private boolean validar(String n, String a, String d, String t, String e) {
        if (TextUtils.isEmpty(n) || TextUtils.isEmpty(a) || TextUtils.isEmpty(d) ||
                TextUtils.isEmpty(t) || TextUtils.isEmpty(e)) {
            Toast.makeText(getApplication(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(e).matches()) {
            Toast.makeText(getApplication(), "Ingrese un email válido", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private static String safe(String s) { return s == null ? "" : s; }
}

