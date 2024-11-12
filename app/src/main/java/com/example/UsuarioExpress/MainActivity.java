package com.example.UsuarioExpress;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.activity.OnBackPressedCallback;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.mymessenger.NewsWorker;
import com.example.mymessenger.NotificationHelper;
import com.example.mymessenger.R;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseHelper baseDeDatos = null;
        try {
            // Inicializar la base de datos como variable local
            baseDeDatos = new DatabaseHelper(this);

            setupToolbar();
            handleBackButton();
            showWelcomeMessage(baseDeDatos);
            initializeButtons();
            setupNotificationScheduler();

        } catch (Exception e) {
            Log.e(TAG, "Error en onCreate: ", e);
            showErrorMessage();
        } finally {
            // Asegurarse de cerrar la conexión a la base de datos si se creó
            if (baseDeDatos != null) {
                baseDeDatos.close();
            }
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(getString(R.string.app_name));
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    private void handleBackButton() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });
    }

    private void showWelcomeMessage(DatabaseHelper baseDeDatos) {
        String username = getIntent().getStringExtra("username");
        TextView titleTextView = findViewById(R.id.tv_title);

        if (titleTextView != null) {
            if (username != null && !username.isEmpty()) {
                String nombreCompleto = baseDeDatos.getNombreUsuario(username);
                if (nombreCompleto != null && !nombreCompleto.isEmpty()) {
                    titleTextView.setText(getString(R.string.welcome_message, nombreCompleto));
                } else {
                    titleTextView.setText(getString(R.string.welcome_message, username));
                }
            }
        }
    }

    private void initializeButtons() {
        setupButton(R.id.btn_section1, R.string.explore_interests,
                com.example.mymessenger.InterestsActivity.class);
        setupButton(R.id.btn_section2, R.string.view_recommendations,
                com.example.mymessenger.RecommendationsActivity.class);
        setupButton(R.id.btn_section3, R.string.read_news,
                com.example.mymessenger.NewsActivity.class);

        Button btnReturnLogin = findViewById(R.id.btn_return_login);
        if (btnReturnLogin != null) {
            btnReturnLogin.setText(getString(R.string.return_to_login));
            btnReturnLogin.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            });
        }
    }

    private void setupNotificationScheduler() {
        NotificationHelper.createNotificationChannel(this);
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        PeriodicWorkRequest newsWorkRequest = new PeriodicWorkRequest.Builder(NewsWorker.class, 5, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build();

        WorkManager.getInstance(this).enqueue(newsWorkRequest);
    }

    private void showErrorMessage() {

    }

    private void setupButton(int buttonId, int stringId, Class<?> targetActivity) {
        Button button = findViewById(buttonId);
        if (button != null) {
            button.setText(getString(stringId));
            button.setOnClickListener(v -> {
                try {
                    Intent intent = new Intent(MainActivity.this, targetActivity);
                    startActivity(intent);
                } catch (Exception e) {
                    Log.e(TAG, "Error al iniciar actividad: " + targetActivity.getSimpleName(), e);
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}