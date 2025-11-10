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

    private static final LatLng INMOBILIARIA_UBICACION = new LatLng(-33.2279, -66.3145); // Coordenadas ULP (cámbialas)
    private static final float ZOOM = 16f; // nivel de zoom
    private MapView mapView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_inicio, container, false);

        TextView tvDireccion = v.findViewById(R.id.tvDireccion);
        tvDireccion.setText("Universidad de La Punta, San Luis, Argentina");

        mapView = v.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        return v;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        googleMap.clear();
        googleMap.addMarker(new MarkerOptions()
                .position(INMOBILIARIA_UBICACION)
                .title("Inmobiliaria ULP"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(INMOBILIARIA_UBICACION, ZOOM));
    }

    // Ciclo de vida del MapView
    @Override public void onResume() { super.onResume(); mapView.onResume(); }
    @Override public void onPause() { super.onPause(); mapView.onPause(); }
    @Override public void onDestroy() { super.onDestroy(); mapView.onDestroy(); }
    @Override public void onLowMemory() { super.onLowMemory(); mapView.onLowMemory(); }
}
