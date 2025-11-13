package com.example.inmobiliaria.ui.inmuebles;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.inmobiliaria.R;

public class AgregarInmuebleFragment extends Fragment {

    private AgregarInmuebleViewModel vm;
    private Uri imgUri = null;
    private ImageView ivFoto;

    private final ActivityResultLauncher<String> pickImg =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                imgUri = uri;
                ivFoto.setVisibility(View.VISIBLE);
                ivFoto.setImageURI(uri);
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_agregar_inmueble, container, false);

        vm = new ViewModelProvider(this).get(AgregarInmuebleViewModel.class);

        EditText etTipo      = view.findViewById(R.id.etTipo);
        EditText etUso       = view.findViewById(R.id.etUso);
        EditText etDireccion = view.findViewById(R.id.etDireccion);
        EditText etAmbientes = view.findViewById(R.id.etAmbientes);
        EditText etSuperf    = view.findViewById(R.id.etSuperficie);
        EditText etValor     = view.findViewById(R.id.etValor);
        EditText etLatitud   = view.findViewById(R.id.etLatitud);
        EditText etLongitud  = view.findViewById(R.id.etLongitud);

        Button btnFoto  = view.findViewById(R.id.btnFoto);
        Button btnCrear = view.findViewById(R.id.btnCrear);
        ivFoto = view.findViewById(R.id.ivFoto);

        ivFoto.setVisibility(View.GONE);

        btnFoto.setOnClickListener(v -> pickImg.launch("image/*"));

        btnCrear.setOnClickListener(v -> vm.crear(
                etTipo.getText().toString(),
                etUso.getText().toString(),
                etDireccion.getText().toString(),
                etAmbientes.getText().toString(),
                etSuperf.getText().toString(),
                etValor.getText().toString(),
                etLatitud.getText().toString(),
                etLongitud.getText().toString(),
                imgUri,
                requireContext()
        ));

        return view;
    }
}
