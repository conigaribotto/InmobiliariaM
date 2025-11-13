package com.example.inmobiliaria.ui.inmuebles;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.inmobiliaria.model.Inmueble;
import com.example.inmobiliaria.request.ApiClient;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgregarInmuebleViewModel extends AndroidViewModel {

    public AgregarInmuebleViewModel(@NonNull Application application) {
        super(application);
    }

    public void crear(String tipoS,
                      String usoS,
                      String dirS,
                      String ambS,
                      String supS,
                      String valorS,
                      String latS,
                      String lonS,
                      Uri imageUri,
                      Context ctx) {

        String tipo = safe(tipoS);
        String uso  = safe(usoS);
        String dir  = safe(dirS);
        String amb  = safe(ambS);
        String sup  = safe(supS);
        String val  = safe(valorS);
        String lat  = safe(latS);
        String lon  = safe(lonS);

        if (TextUtils.isEmpty(uso) || TextUtils.isEmpty(dir)) {
            Toast.makeText(getApplication(),
                    "Complete uso y dirección",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(tipo)) {
            tipo = uso; // por si no carga tipo
        }

        int ambientes = 0;
        int superficie = 0;
        double valor = 0.0;
        double latitud = 0.0;
        double longitud = 0.0;

        try {
            if (!TextUtils.isEmpty(amb)) ambientes = Integer.parseInt(amb);
            if (!TextUtils.isEmpty(sup)) superficie = Integer.parseInt(sup);
            if (!TextUtils.isEmpty(val)) valor = Double.parseDouble(val);
            if (!TextUtils.isEmpty(lat)) latitud = Double.parseDouble(lat);
            if (!TextUtils.isEmpty(lon)) longitud = Double.parseDouble(lon);
        } catch (NumberFormatException e) {
            Toast.makeText(getApplication(),
                    "Revisá los campos numéricos",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Inmueble nuevo = new Inmueble();
            nuevo.setTipo(tipo);
            nuevo.setUso(uso);
            nuevo.setDireccion(dir);
            nuevo.setAmbientes(ambientes);
            nuevo.setSuperficie(superficie);   // INT
            nuevo.setValor(valor);             // DOUBLE
            nuevo.setLatitud(latitud);
            nuevo.setLongitud(longitud);
            nuevo.setDisponible(false);

            String inmuebleJson = new Gson().toJson(nuevo);
            RequestBody inmuebleBody = RequestBody.create(
                    MediaType.parse("application/json; charset=utf-8"),
                    inmuebleJson
            );

            MultipartBody.Part imagenPart;
            if (imageUri != null) {
                byte[] bytes = readAll(ctx, imageUri);
                RequestBody reqFile = RequestBody.create(
                        MediaType.parse("image/jpeg"),
                        bytes
                );
                imagenPart = MultipartBody.Part.createFormData(
                        "imagen",
                        "imagen.jpg",
                        reqFile
                );
            } else {
                imagenPart = MultipartBody.Part.createFormData(
                        "imagen",
                        "",
                        RequestBody.create(
                                MediaType.parse("application/octet-stream"),
                                new byte[0]
                        )
                );
            }

            ApiClient.getInmobiliariaService()
                    .cargarInmueble(imagenPart, inmuebleBody)
                    .enqueue(new Callback<Inmueble>() {
                        @Override
                        public void onResponse(Call<Inmueble> call, Response<Inmueble> resp) {
                            if (resp.isSuccessful()) {
                                Toast.makeText(getApplication(),
                                        "Inmueble creado",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                String msg = "Error al crear inmueble";
                                try {
                                    if (resp.errorBody() != null) {
                                        msg += ": " + resp.errorBody().string();
                                    }
                                } catch (Exception ignored) {}
                                Log.e("API_INMUEBLE", msg);
                                Toast.makeText(getApplication(),
                                        msg,
                                        Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Inmueble> call, Throwable t) {
                            Log.e("API_INMUEBLE", "Fallo red", t);
                            Toast.makeText(getApplication(),
                                    "Red: " + t.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

        } catch (Exception e) {
            Toast.makeText(getApplication(),
                    "Error leyendo la imagen: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private String safe(String s) {
        return s == null ? "" : s.trim();
    }










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


