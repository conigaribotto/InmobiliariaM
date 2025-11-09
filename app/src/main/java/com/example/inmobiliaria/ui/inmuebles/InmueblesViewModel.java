package com.example.inmobiliaria.ui.inmuebles;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.model.Inmueble;
import com.example.inmobiliaria.request.ApiClient;

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
    private boolean loaded = false;

    public InmueblesViewModel(@NonNull Application application) { super(application); }

    public LiveData<List<Inmueble>> getInmuebles() { return inmuebles; }
    public LiveData<Boolean> getLoading() { return loading; }

    public void cargar() {
        loading.postValue(true);
        ApiClient.getInmobiliariaService().obtenerInmuebles()
                .enqueue(new Callback<List<Inmueble>>() {
                    @Override public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> resp) {
                        loading.postValue(false);
                        inmuebles.postValue(resp.isSuccessful() && resp.body()!=null ? resp.body() : new ArrayList<>());
                        loaded = true;
                    }
                    @Override public void onFailure(Call<List<Inmueble>> call, Throwable t) {
                        loading.postValue(false);
                        inmuebles.postValue(new ArrayList<>());
                        Toast.makeText(getApplication(),"Red: "+t.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void crear(String tituloS, String descS, String dirS) {
        RequestBody titulo = RequestBody.create(MediaType.parse("text/plain"), tituloS);
        RequestBody desc   = RequestBody.create(MediaType.parse("text/plain"), descS);
        RequestBody direc  = RequestBody.create(MediaType.parse("text/plain"), dirS);
        MultipartBody.Part foto = MultipartBody.Part.createFormData("foto","",
                RequestBody.create(MediaType.parse("application/octet-stream"), new byte[0]));

        loading.postValue(true);
        ApiClient.getInmobiliariaService().crearInmueble(titulo, desc, direc, foto)
                .enqueue(new Callback<Inmueble>() {
                    @Override public void onResponse(Call<Inmueble> call, Response<Inmueble> resp) {
                        loading.postValue(false);
                        if (resp.isSuccessful()) cargar();
                    }
                    @Override public void onFailure(Call<Inmueble> call, Throwable t) {
                        loading.postValue(false);
                        Toast.makeText(getApplication(),"Red: "+t.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
    }
}
