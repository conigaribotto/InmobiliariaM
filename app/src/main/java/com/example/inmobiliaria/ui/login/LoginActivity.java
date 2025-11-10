package com.example.inmobiliaria.ui.login;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.inmobiliaria.databinding.ActivityLoginBinding;
import com.example.inmobiliaria.ui.menu.MenuActivity;

public class LoginActivity extends AppCompatActivity implements SensorEventListener {

    private ActivityLoginBinding binding;
    private LoginActivityViewModel viewModel;
    private SensorManager sensorManager;
    private Sensor accelerometer;

    private static final float SHAKE_THRESHOLD = 12.0f;
    private long lastShakeTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(LoginActivityViewModel.class);

        viewModel.getMensaje().observe(this,
                msg -> Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show());

        viewModel.getLoginExitoso().observe(this, ok -> {
            if (ok) {
                startActivity(new Intent(LoginActivity.this, MenuActivity.class));
                finish();
            }
        });

        binding.btnLogin.setOnClickListener((View v) -> {
            String usuario = binding.etUsuario.getText().toString();
            String clave = binding.etClave.getText().toString();
            viewModel.login(usuario, clave);
        });

        // SensorManager para detectar agitación
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }

        // Si ya hay token válido, ir al Menú
        viewModel.verificarSesion();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (accelerometer != null)
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (accelerometer != null)
            sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            double acceleration = Math.sqrt(x * x + y * y + z * z)
                    - SensorManager.GRAVITY_EARTH;

            long currentTime = System.currentTimeMillis();
            if (acceleration > SHAKE_THRESHOLD && (currentTime - lastShakeTime) > 1500) {
                lastShakeTime = currentTime;

                String usuario = binding.etUsuario.getText().toString();
                String clave = binding.etClave.getText().toString();

                if (usuario.isEmpty() || clave.isEmpty()) {
                    Toast.makeText(this, "Ingresá usuario y contraseña", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Dispositivo agitado → intentando login", Toast.LENGTH_SHORT).show();
                    viewModel.login(usuario, clave);
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
