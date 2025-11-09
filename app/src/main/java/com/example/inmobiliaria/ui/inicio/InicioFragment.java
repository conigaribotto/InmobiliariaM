package com.example.inmobiliaria.ui.inicio;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.inmobiliaria.R;

public class InicioFragment extends Fragment {

    private static final String DIRECCION = "Universidad de La Punta, San Luis, Argentina"; // cambiala por la tuya
    private static final String GEO_QUERY = "geo:0,0?q=" + DIRECCION;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_inicio, container, false);

        TextView tv = v.findViewById(R.id.tvDireccion);
        tv.setText(DIRECCION);

        Button btn = v.findViewById(R.id.btnAbrirMaps);
        btn.setOnClickListener(view -> {
            Uri gmmIntentUri = Uri.parse(GEO_QUERY);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        });

        return v;
    }
}
