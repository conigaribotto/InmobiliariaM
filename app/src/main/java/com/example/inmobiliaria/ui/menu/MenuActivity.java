package com.example.inmobiliaria.ui.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.inmobiliaria.R;
import com.example.inmobiliaria.databinding.ActivityMenuBinding;
import com.example.inmobiliaria.request.ApiClient;
import com.example.inmobiliaria.ui.login.LoginActivity;
import com.google.android.material.navigation.NavigationView;

public class MenuActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMenuBinding binding;
    private MenuViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Si no hay token válido → volver a login
        if (!ApiClient.isTokenValido(this)) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        binding = ActivityMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMenu.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navView = binding.navView;

        // NavController principal
        NavController navController =
                Navigation.findNavController(this, R.id.nav_host_fragment_content_menu);

        // Destinos de primer nivel del drawer
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_inicio,
                R.id.nav_perfil,
                R.id.nav_inmuebles,
                R.id.nav_inquilinos,
                R.id.nav_contratos
        ).setOpenableLayout(drawer).build();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        // ViewModel de propietario (header del drawer)
        vm = new ViewModelProvider(this).get(MenuViewModel.class);
        vm.cargarPropietario();

        View header = navView.getHeaderView(0);
        TextView tvNombre = header.findViewById(R.id.tvHeaderNombre);
        TextView tvEmail  = header.findViewById(R.id.tvHeaderEmail);

        vm.getPropietario().observe(this, p -> {
            if (p != null) {
                tvNombre.setText(p.getNombre() + " " + p.getApellido());
                tvEmail.setText(p.getEmail());
            }
        });

        // ******** FAB: solo visible en INMUEBLES y navega a AGREGAR INMUEBLE ********
        binding.appBarMenu.fab.setImageResource(android.R.drawable.ic_input_add);

        navController.addOnDestinationChangedListener((controller, dest, args) -> {
            if (dest.getId() == R.id.nav_inmuebles) {
                binding.appBarMenu.fab.show();
            } else {
                binding.appBarMenu.fab.hide();
            }
        });

        binding.appBarMenu.fab.setOnClickListener(view -> {
            if (navController.getCurrentDestination() != null &&
                    navController.getCurrentDestination().getId() == R.id.nav_inmuebles) {
                navController.navigate(R.id.action_nav_inmuebles_to_nav_agregar_inmueble);
            }
        });

        // Manejo del item Logout en el drawer
        navView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_logout) {
                ApiClient.borrarToken(this);
                Toast.makeText(this, getString(R.string.logout_ok), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return true;
            }
            boolean handled = NavigationUI.onNavDestinationSelected(item, navController);
            if (handled) drawer.closeDrawers();
            return handled;
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController =
                Navigation.findNavController(this, R.id.nav_host_fragment_content_menu);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
