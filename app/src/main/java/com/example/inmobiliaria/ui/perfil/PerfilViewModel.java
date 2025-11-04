package com.example.inmobiliaria.ui.perfil;

import android.app.Application;
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
    private MutableLiveData<Propietario> mPropietario = new MutableLiveData<>();
    private MutableLiveData<Boolean> mEstado;
    private MutableLiveData<String> btnTexto;

    public PerfilViewModel(@NonNull Application application) {
        super(application);
        // valores iniciales
        if (mEstado == null) mEstado = new MutableLiveData<>(false);
        if (btnTexto == null) btnTexto = new MutableLiveData<>("Editar");
    }

    public LiveData<Propietario> getmPropietario() {
        return mPropietario;
    }

    public LiveData<Boolean> getmEstado() {
        if (mEstado == null) {
            mEstado = new MutableLiveData<>();
        }
        return mEstado;
    }

    public LiveData<String> getBtnTexto() {
        if (btnTexto == null) {
            btnTexto = new MutableLiveData<>();
        }
        return btnTexto;
    }

    public void leerPropietario() {
        String token = ApiClient.obtenerToken(getApplication());
        if (token == null) {
            Toast.makeText(getApplication(), "No autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        Call<Propietario> call = ApiClient.getInmobiliariaService().obtenerPropietario("Bearer " + token);
        call.enqueue(new Callback<Propietario>() {
            @Override
            public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mPropietario.postValue(response.body());
                } else {
                    Toast.makeText(getApplication(), "No se pudo cargar el perfil", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Propietario> call, Throwable t) {
                Toast.makeText(getApplication(), "Error de conexión", Toast.LENGTH_SHORT).show();
                Log.d("PerfilViewModel", "leerPropietario failure: " + t.getMessage());
            }
        });
    }

    public void guardar(String btn, String nombre, String apellido, String dniStr, String telefonoStr, String email) {
        if (btn.equalsIgnoreCase("editar")) {
            mEstado.setValue(true);
            btnTexto.setValue("Guardar");
        } else {
            // VALIDACIONES
            if (nombre.isEmpty() || apellido.isEmpty() || dniStr.isEmpty() || telefonoStr.isEmpty() || email.isEmpty()) {
                Toast.makeText(getApplication(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(getApplication(), "Ingrese un email válido", Toast.LENGTH_SHORT).show();
                return;
            }

            Propietario current = mPropietario.getValue();
            if (current == null) {
                Toast.makeText(getApplication(), "Perfil no cargado", Toast.LENGTH_SHORT).show();
                return;
            }

            Propietario propietario = new Propietario(
                    current.getIdPropietario(),
                    nombre,
                    apellido,
                    dniStr,        // DNI como String
                    telefonoStr,   // teléfono como String
                    email,
                    null
            );

            btnTexto.setValue("Editar");
            mEstado.setValue(false);

            String token = ApiClient.obtenerToken(getApplication());
            Call<Propietario> llamada = ApiClient.getInmobiliariaService().actualizarPropietario("Bearer " + token, propietario);
            llamada.enqueue(new Callback<Propietario>() {
                @Override
                public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        mPropietario.postValue(response.body());
                        Toast.makeText(getApplication(), "Perfil actualizado", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplication(), "Error al actualizar", Toast.LENGTH_SHORT).show();
                        Log.d("PerfilViewModel", "update error: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<Propietario> call, Throwable throwable) {
                    Toast.makeText(getApplication(), "Error de servidor", Toast.LENGTH_SHORT).show();
                    Log.d("PerfilViewModel", "update failure: " + throwable.getMessage());
                }
            });
        }
    }
}
