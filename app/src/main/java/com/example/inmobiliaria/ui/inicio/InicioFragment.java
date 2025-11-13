package com.example.inmobiliaria.ui.inicio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.inmobiliaria.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class InicioFragment extends Fragment implements OnMapReadyCallback {

    // Coordenadas de la inmobiliaria / ULP
    private static final LatLng INMOBILIARIA_UBICACION =
            new LatLng(-33.2279, -66.3145);

    // 🔎 Menos zoom (antes 16f → ahora 13f para que se vea más alejado)
    private static final float ZOOM = 13f;

    private MapView mapView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_inicio, container, false);

        TextView tvTitulo = v.findViewById(R.id.tvTituloInicio);
        TextView tvDireccion = v.findViewById(R.id.tvDireccion);

        tvTitulo.setText("Nuestra ubicación");
        tvDireccion.setText("Universidad de La Punta, San Luis, Argentina");

        mapView = v.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        return v;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        googleMap.clear();
        googleMap.addMarker(
                new MarkerOptions()
                        .position(INMOBILIARIA_UBICACION)
                        .title("Inmobiliaria ULP")
        );

        googleMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(INMOBILIARIA_UBICACION, ZOOM)
        );
    }

    @Override public void onResume()    { super.onResume();    mapView.onResume(); }
    @Override public void onPause()     { super.onPause();     mapView.onPause(); }
    @Override public void onDestroy()   { super.onDestroy();   mapView.onDestroy(); }
    @Override public void onLowMemory() { super.onLowMemory(); mapView.onLowMemory(); }
}
