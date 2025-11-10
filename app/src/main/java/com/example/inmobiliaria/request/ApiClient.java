package com.example.inmobiliaria.request;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.inmobiliaria.model.Alquiler;
import com.example.inmobiliaria.model.Inmueble;
import com.example.inmobiliaria.model.Pagos;
import com.example.inmobiliaria.model.Propietario;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public class ApiClient {

    private static final String URLBASE = "https://inmobiliariaulp-amb5hwfqaraweyga.canadacentral-01.azurewebsites.net/";
    private static final String PREFS_NAME = "inmobiliaria_prefs";
    private static final String PREF_TOKEN = "token";
    private static final String PREF_OWNER_ID = "owner_id";

    private static Retrofit retrofit;
    private static InmobiliariaService service;

    public static void init(Context context) {
        if (retrofit != null) return;

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

        HttpLoggingInterceptor log = new HttpLoggingInterceptor();
        log.setLevel(HttpLoggingInterceptor.Level.BODY);

        Interceptor auth = chain -> {
            Request original = chain.request();
            String token = obtenerToken(context);
            if (token != null && !token.isEmpty()) {
                Request req = original.newBuilder()
                        .header("Authorization", "Bearer " + token)
                        .build();
                return chain.proceed(req);
            }
            return chain.proceed(original);
        };

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(log)
                .addInterceptor(auth)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(URLBASE)
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        service = retrofit.create(InmobiliariaService.class);
    }

    public static InmobiliariaService getInmobiliariaService() {
        if (service == null) throw new IllegalStateException("ApiClient no inicializado");
        return service;
    }

    public interface InmobiliariaService {

        @FormUrlEncoded
        @POST("api/propietarios/login")
        Call<String> login(@Field("Usuario") String usuario, @Field("Clave") String clave);

        @GET("api/propietarios")
        Call<Propietario> obtenerPropietario();

        @PUT("api/propietarios/actualizar")
        Call<Propietario> actualizarPropietario(@Body Propietario propietario);

        @FormUrlEncoded
        @PUT("api/propietarios/changePassword")
        Call<Void> cambiarClave(@Field("currentPassword") String claveActual,
                                @Field("newPassword") String claveNueva);

        @FormUrlEncoded
        @POST("api/propietarios/resetPassword")
        Call<Void> resetPassword(@Field("email") String email);

        @GET("api/inmuebles")
        Call<List<Inmueble>> obtenerInmuebles();

        @Multipart
        @POST("api/inmuebles")
        Call<Inmueble> crearInmueble(
                @Part("titulo") okhttp3.RequestBody titulo,
                @Part("descripcion") okhttp3.RequestBody descripcion,
                @Part("direccion") okhttp3.RequestBody direccion,
                @Part okhttp3.MultipartBody.Part foto
        );

        @GET("api/contratos")
        Call<List<Alquiler>> obtenerContratos();

        @GET("api/contratos/inmueble/{id}")
        Call<List<Alquiler>> obtenerContratosPorInmueble(@Path("id") int inmuebleId);

        @GET("api/pagos/contrato/{id}")
        Call<List<Pagos>> obtenerPagosPorContrato(@Path("id") int contratoId);
    }

    public static void guardarToken(Context ctx, String token) {
        if (token == null) return;
        token = token.trim().replace("\"", "");
        if (token.startsWith("Bearer ")) token = token.substring(7).trim();

        if (!token.isEmpty() && !"null".equalsIgnoreCase(token) && token.length() > 20) {
            ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                    .edit().putString(PREF_TOKEN, token).apply();
        }
    }

    public static String obtenerToken(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String t = sp.getString(PREF_TOKEN, null);
        if (t == null) return null;
        t = t.trim().replace("\"", "");
        if (t.startsWith("Bearer ")) t = t.substring(7).trim();
        return t;
    }

    public static boolean isTokenValido(Context ctx) {
        String t = obtenerToken(ctx);
        return t != null && !t.isEmpty() && !"null".equalsIgnoreCase(t) && t.length() > 20;
    }

    public static void borrarToken(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        sp.edit().clear().apply();
    }

    public static void guardarOwnerId(Context ctx, int id) {
        SharedPreferences sp = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        sp.edit().putInt(PREF_OWNER_ID, id).apply();
    }

    public static int obtenerOwnerId(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sp.getInt(PREF_OWNER_ID, -1);
    }
}
