package com.example.inmobiliaria;

import android.app.Application;
import com.example.inmobiliaria.request.ApiClient;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ApiClient.init(this);
    }
}
