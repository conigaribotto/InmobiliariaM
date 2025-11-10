package com.example.inmobiliaria.ui.inmuebles;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.model.Inmueble;
import com.example.inmobiliaria.request.ApiClient;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InmueblesViewModel extends AndroidViewModel {

    private final MutableLiveData<List<Inmueble>> inmuebles = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);

    public InmueblesViewModel(@NonNull Application application) { super(application); }

    public LiveData<List<Inmueble>> getInmuebles() { return inmuebles; }
    public LiveData<Boolean> getLoading() { return loading; }

    /** Lista de inmuebles del propietario autenticado (token por interceptor). */
    public void cargar() {
        loading.postValue(true);
        ApiClient.getInmobiliariaService()
                .obtenerInmuebles()
                .enqueue(new Callback<List<Inmueble>>() {
                    @Override public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> resp) {
                        loading.postValue(false);
                        inmuebles.postValue(resp.isSuccessful() && resp.body()!=null
                                ? resp.body() : new ArrayList<>());
                    }
                    @Override public void onFailure(Call<List<Inmueble>> call, Throwable t) {
                        loading.postValue(false);
                        inmuebles.postValue(new ArrayList<>());
                        Toast.makeText(getApplication(), "Red: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    /**
     * Crea un inmueble enviando título, descripción, dirección y una foto (opcional) seleccionada
     * desde galería (Uri). Si no hay foto, se envía una parte vacía para cumplir el @Multipart.
     */
    public void crear(String tituloS, String descS, String dirS, Uri imageUri, Context ctx) {
        try {
            RequestBody titulo = RequestBody.create(MediaType.parse("text/plain"), nz(tituloS));
            RequestBody desc   = RequestBody.create(MediaType.parse("text/plain"), nz(descS));
            RequestBody direc  = RequestBody.create(MediaType.parse("text/plain"), nz(dirS));

            MultipartBody.Part fotoPart;
            if (imageUri != null) {
                byte[] bytes = readAll(ctx, imageUri);
                RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), bytes);
                fotoPart = MultipartBody.Part.createFormData("foto", "foto.jpg", reqFile);
            } else {
                // Parte vacía para cumplir la firma del endpoint
                fotoPart = MultipartBody.Part.createFormData(
                        "foto", "",
                        RequestBody.create(MediaType.parse("application/octet-stream"), new byte[0])
                );
            }

            loading.postValue(true);
            ApiClient.getInmobiliariaService()
                    .crearInmueble(titulo, desc, direc, fotoPart)
                    .enqueue(new Callback<Inmueble>() {
                        @Override public void onResponse(Call<Inmueble> call, Response<Inmueble> resp) {
                            loading.postValue(false);
                            if (resp.isSuccessful()) {
                                Toast.makeText(getApplication(), "Inmueble creado", Toast.LENGTH_SHORT).show();
                                cargar(); // refrescar lista
                            } else {
                                Toast.makeText(getApplication(), "Error al crear inmueble", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override public void onFailure(Call<Inmueble> call, Throwable t) {
                            loading.postValue(false);
                            Toast.makeText(getApplication(), "Red: " + t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

        } catch (Exception e) {
            Toast.makeText(ctx, "Error leyendo la imagen: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // ===== Helpers =====
    private String nz(String s) { return s == null ? "" : s; }

    private byte[] readAll(Context ctx, Uri uri) throws Exception {
        InputStream is = null;
        ByteArrayOutputStream buffer = null;
        try {
            is = ctx.getContentResolver().openInputStream(uri);
            buffer = new ByteArrayOutputStream();
            byte[] data = new byte[8192];
            int nRead;
            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
            return buffer.toByteArray();
        } finally {
            try { if (is != null) is.close(); } catch (Exception ignored) {}
            try { if (buffer != null) buffer.close(); } catch (Exception ignored) {}
        }
    }
}

