package com.example.inmobiliaria.ui.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.example.inmobiliaria.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.inmobiliaria.databinding.ActivityMenuBinding;
import com.example.inmobiliaria.request.ApiClient;

public class MenuActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMenuBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMenu.toolbar);
        binding.appBarMenu.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Acción de ejemplo", Snackbar.LENGTH_LONG)
                        .setAction("OK", null)
                        .setAnchorView(R.id.fab)
                        .show();
            }
        });

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Top-level SOLO los destinos que EXISTEN en nav_graph_menu
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_perfil,
                R.id.nav_inmuebles
        ).setOpenableLayout(drawer).build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_menu);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Manejo manual de Login/Logout + delegar el resto al NavController
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_login) {
                startActivity(new Intent(MenuActivity.this, com.example.inmobiliaria.ui.login.LoginActivity.class));
                drawer.closeDrawers();
                return true;
            } else if (id == R.id.nav_logout) {
                // limpiar token y avisar
                ApiClient.guardarToken(this, null);
                Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
                drawer.closeDrawers();
                return true;
            }

            boolean handled = NavigationUI.onNavDestinationSelected(item, navController);
            if (handled) drawer.closeDrawers();
            return handled;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_menu);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
