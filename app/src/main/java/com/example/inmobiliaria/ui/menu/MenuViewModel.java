package com.example.inmobiliaria.ui.menu;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

/**
 * ViewModel del menú principal.
 * Se usará para manejar la lógica de navegación o acciones del menú en el futuro.
 */
public class MenuViewModel extends AndroidViewModel {

    private final MutableLiveData<String> mensaje = new MutableLiveData<>();

    public MenuViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<String> getMensaje() {
        return mensaje;
    }

    public void mostrarMensaje(String texto) {
        mensaje.setValue(texto);
    }
}
