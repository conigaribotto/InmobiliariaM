package com.example.inmobiliaria;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.inmobiliaria.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Usa ViewBinding para inflar el layout
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Configura la toolbar
        setSupportActionBar(binding.appBarMain.toolbar);

        // Acción del FAB (puedes eliminar si no lo usás)
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Acción personalizada", Snackbar.LENGTH_LONG)
                        .setAction("OK", null)
                        .setAnchorView(R.id.fab)
                        .show();
            }
        });

        // DrawerLayout y NavigationView
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Configura los destinos principales del Drawer
// MenuActivity.java
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_perfil,
                R.id.nav_inmuebles
        ).setOpenableLayout(drawer).build();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Infla el menú superior
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
