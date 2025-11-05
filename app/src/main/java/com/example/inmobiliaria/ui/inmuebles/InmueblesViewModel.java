package com.example.inmobiliaria.ui.inmuebles;

import android.app.Application;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.model.Inmueble;
import com.example.inmobiliaria.request.ApiClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InmueblesViewModel extends AndroidViewModel {

    private final MutableLiveData<List<Inmueble>> inmuebles = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> snackbar = new MutableLiveData<>("");

    // Campos para “Agregar inmueble”
    private final MutableLiveData<String> nuevoTitulo = new MutableLiveData<>("");
    private final MutableLiveData<String> nuevaDescripcion = new MutableLiveData<>("");
    private final MutableLiveData<String> nuevaDireccion = new MutableLiveData<>("");
    private Uri fotoUriSeleccionada = null; // la uri cruda (la recibe desde el Fragment)

    public InmueblesViewModel(@NonNull Application application) {
        super(application);
    }

    // Exposición
    public LiveData<List<Inmueble>> getInmuebles() { return inmuebles; }
    public LiveData<Boolean> getLoading() { return loading; }
    public LiveData<String> getSnackbar() { return snackbar; }
    public LiveData<String> getNuevoTitulo() { return nuevoTitulo; }
    public LiveData<String> getNuevaDescripcion() { return nuevaDescripcion; }
    public LiveData<String> getNuevaDireccion() { return nuevaDireccion; }

    // Setters sin lógica (Fragment no hace ifs)
    public void setNuevoTitulo(String v) { nuevoTitulo.setValue(v); }
    public void setNuevaDescripcion(String v) { nuevaDescripcion.setValue(v); }
    public void setNuevaDireccion(String v) { nuevaDireccion.setValue(v); }
    public void setFotoUriSeleccionada(Uri uri) { this.fotoUriSeleccionada = uri; }

    /* ======================
       CARGAR LISTA
       ====================== */
    public void cargarInmuebles() {
        String token = ApiClient.obtenerToken(getApplication());
        if (token == null) {
            snackbar.setValue("No autenticado");
            return;
        }
        loading.setValue(true);
        ApiClient.getInmobiliariaService()
                .obtenerInmueblesPorPropietario("Bearer " + token)
                .enqueue(new Callback<List<Inmueble>>() {
                    @Override
                    public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> response) {
                        loading.setValue(false);
                        if (response.isSuccessful() && response.body() != null) {
                            inmuebles.setValue(response.body());
                        } else {
                            snackbar.setValue("No se pudo obtener la lista");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Inmueble>> call, Throwable t) {
                        loading.setValue(false);
                        snackbar.setValue("Error de conexión");
                    }
                });
    }

    /* ======================
       HABILITAR / DESHABILITAR
       ====================== */
    public void alternarHabilitado(Inmueble item) {
        String token = ApiClient.obtenerToken(getApplication());
        if (token == null) {
            snackbar.setValue("No autenticado");
            return;
        }
        // según estado, llamo habilitar o deshabilitar
        Call<Void> llamada = (item.isHabilitado())
                ? ApiClient.getInmobiliariaService().deshabilitarInmueble("Bearer " + token, item.getIdInmueble())
                : ApiClient.getInmobiliariaService().habilitarInmueble("Bearer " + token, item.getIdInmueble());

        loading.setValue(true);
        llamada.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                loading.setValue(false);
                if (response.isSuccessful()) {
                    snackbar.setValue(item.isHabilitado() ? "Inmueble deshabilitado" : "Inmueble habilitado");
                    cargarInmuebles(); // refresca lista
                } else {
                    snackbar.setValue("No se pudo cambiar el estado");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                loading.setValue(false);
                snackbar.setValue("Error de servidor");
            }
        });
    }

    /* ======================
       AGREGAR INMUEBLE
       ====================== */
    public void crearInmueble() {
        String titulo = safe(nuevoTitulo.getValue());
        String descripcion = safe(nuevaDescripcion.getValue());
        String direccion = safe(nuevaDireccion.getValue());

        if (TextUtils.isEmpty(titulo) || TextUtils.isEmpty(descripcion) || TextUtils.isEmpty(direccion)) {
            snackbar.setValue("Completá todos los campos");
            return;
        }
        if (fotoUriSeleccionada == null) {
            snackbar.setValue("Seleccioná una foto");
            return;
        }

        String token = ApiClient.obtenerToken(getApplication());
        if (token == null) {
            snackbar.setValue("No autenticado");
            return;
        }

        // Armado de parts
        RequestBody tituloRB = RequestBody.create(MediaType.parse("text/plain"), titulo);
        RequestBody descRB   = RequestBody.create(MediaType.parse("text/plain"), descripcion);
        RequestBody dirRB    = RequestBody.create(MediaType.parse("text/plain"), direccion);

        MultipartBody.Part fotoPart = buildFotoPartFromUri(fotoUriSeleccionada);
        if (fotoPart == null) {
            snackbar.setValue("No se pudo leer la foto");
            return;
        }

        loading.setValue(true);
        ApiClient.getInmobiliariaService()
                .crearInmuebleSinPropietario("Bearer " + token, tituloRB, descRB, dirRB, fotoPart)
                .enqueue(new Callback<Inmueble>() {
                    @Override
                    public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                        loading.setValue(false);
                        if (response.isSuccessful() && response.body() != null) {
                            snackbar.setValue("Inmueble agregado (queda deshabilitado)");
                            // limpiar campos del form
                            nuevoTitulo.setValue("");
                            nuevaDescripcion.setValue("");
                            nuevaDireccion.setValue("");
                            fotoUriSeleccionada = null;
                            cargarInmuebles();
                        } else {
                            snackbar.setValue("No se pudo crear el inmueble");
                        }
                    }

                    @Override
                    public void onFailure(Call<Inmueble> call, Throwable t) {
                        loading.setValue(false);
                        snackbar.setValue("Error de servidor al crear");
                    }
                });
    }

    private MultipartBody.Part buildFotoPartFromUri(Uri uri) {
        try {
            ContentResolver cr = getApplication().getContentResolver();

            String fileName = queryFileName(cr, uri);
            if (TextUtils.isEmpty(fileName)) {
                String ext = MimeTypeMap.getSingleton()
                        .getExtensionFromMimeType(cr.getType(uri));
                fileName = "foto_inmueble." + (ext == null ? "jpg" : ext);
            }

            File cacheDir = new File(getApplication().getCacheDir(), "uploads");
            if (!cacheDir.exists()) cacheDir.mkdirs();
            File temp = new File(cacheDir, fileName);

            try (InputStream in = cr.openInputStream(uri);
                 FileOutputStream out = new FileOutputStream(temp)) {
                byte[] buf = new byte[8192];
                int n;
                while ((n = in.read(buf)) > 0) out.write(buf, 0, n);
            }

            String mime = cr.getType(uri);
            RequestBody reqFile = RequestBody.create(
                    MediaType.parse(mime == null ? "image/*" : mime),
                    temp
            );
            return MultipartBody.Part.createFormData("foto", temp.getName(), reqFile);
        } catch (Exception e) {
            return null;
        }
    }

    private String queryFileName(ContentResolver cr, Uri uri) {
        String result = "";
        Cursor cursor = cr.query(uri, null, null, null, null);
        if (cursor != null) {
            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            if (nameIndex >= 0 && cursor.moveToFirst()) {
                result = cursor.getString(nameIndex);
            }
            cursor.close();
        }
        return result;
    }

    private static String safe(String s) { return s == null ? "" : s; }
}

