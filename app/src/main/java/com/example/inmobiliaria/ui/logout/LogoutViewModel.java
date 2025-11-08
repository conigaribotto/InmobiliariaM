// LogoutViewModel.java
package com.example.inmobiliaria.ui.logout;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.request.ApiClient;

public class LogoutViewModel extends AndroidViewModel {

    // Evento de navegación: emitimos cualquier objeto para disparar
    private final MutableLiveData<Object> navigateToLogin = new MutableLiveData<>();

    public LogoutViewModel(@NonNull Application app) {
        super(app);
    }

    public LiveData<Object> getNavigateToLogin() { return navigateToLogin; }

    public void confirmarLogout() {
        ApiClient.borrarToken(getApplication());
        // Dispara navegación
        navigateToLogin.postValue(new Object());
    }
}
