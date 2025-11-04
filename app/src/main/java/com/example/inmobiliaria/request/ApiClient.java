package com.example.inmobiliaria.request;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.inmobiliaria.model.Propietario;
import com.example.inmobiliaria.model.Inmueble;
import com.example.inmobiliaria.model.Alquiler;
import com.example.inmobiliaria.model.Pagos;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public class ApiClient {
    private static final String URLBASE = "https://inmobiliariaulp-amb5hwfqaraweyga.canadacentral-01.azurewebsites.net/";

    public static InmobiliariaService getInmobiliariaService(){
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URLBASE)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit.create(InmobiliariaService.class);
    }

    public interface InmobiliariaService{
        @FormUrlEncoded
        @POST("api/propietarios/login")
        Call<String> login(@Field("Usuario") String usuario, @Field("Clave") String clave);

        @FormUrlEncoded
        @PUT("api/propietarios/changePassword")
        Call<Void> cambiarClave(@Header("Authorization") String token,
                                @Field("currentPassword") String claveActual,
                                @Field("newPassword") String claveNueva);

        @GET("api/propietarios")
        Call<Propietario> obtenerPropietario(@Header("Authorization") String token);

        @PUT("api/propietarios/actualizar")
        Call<Propietario> actualizarPropietario(@Header("Authorization") String token, @Body Propietario propietario);

        @GET("api/inmuebles/propietario")
        Call<List<Inmueble>> obtenerInmueblesPorPropietario(@Header("Authorization") String token);

        @PUT("api/inmuebles/{id}/habilitar")
        Call<Void> habilitarInmueble(@Header("Authorization") String token, @Path("id") int id);

        @PUT("api/inmuebles/{id}/deshabilitar")
        Call<Void> deshabilitarInmueble(@Header("Authorization") String token, @Path("id") int id);

        @Multipart
        @POST("api/inmuebles")
        Call<Inmueble> crearInmueble(@Header("Authorization") String token,
                                     @Part("titulo") RequestBody titulo,
                                     @Part("descripcion") RequestBody descripcion,
                                     @Part("direccion") RequestBody direccion,
                                     @Part("propietarioId") RequestBody propietarioId,
                                     @Part MultipartBody.Part foto);

        @GET("api/contratos/inmueble/{inmuebleId}")
        Call<List<Alquiler>> obtenerContratosPorInmueble(@Header("Authorization") String token, @Path("inmuebleId") int inmuebleId);

        @GET("api/pagos/contrato/{contratoId}")
        Call<List<Pagos>> obtenerPagosPorContrato(@Header("Authorization") String token, @Path("contratoId") int contratoId);
    }

    // SharedPreferences helpers
    private static final String PREFS_NAME = "inmobiliaria_prefs";
    private static final String PREF_TOKEN = "token";

    public static void guardarToken(Context context, String token){
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_TOKEN, token);
        editor.apply();
    }

    public static String obtenerToken(Context context){
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return preferences.getString(PREF_TOKEN, null);
    }
}
