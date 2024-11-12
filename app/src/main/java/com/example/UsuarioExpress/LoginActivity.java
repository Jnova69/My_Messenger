package com.example.UsuarioExpress;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mymessenger.R;

public class LoginActivity extends AppCompatActivity {
    private DatabaseHelper baseDeDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar la base de datos
        baseDeDatos = new DatabaseHelper(this);

        // Vincular las vistas
        EditText etUsuario = findViewById(R.id.et_username);
        EditText etPassword = findViewById(R.id.et_password);
        Button btnLogin = findViewById(R.id.btn_login);
        Button btnRegistro = findViewById(R.id.btn_go_register);
        Button btnVolver = findViewById(R.id.btn_back);

        // Configurar el botón de login
        btnLogin.setOnClickListener(v -> {
            String usuario = etUsuario.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // Validar campos vacíos
            if (usuario.isEmpty() || password.isEmpty()) {
                mostrarMensaje(getString(R.string.error_empty_fields));
                return;
            }

            // Verificar credenciales usando el método correcto
            if (baseDeDatos.verificarCredenciales(usuario, password)) {
                // Login exitoso
                // Obtener el nombre del usuario usando el método correcto
                String nombreUsuario = baseDeDatos.getNombreUsuario(usuario);

                // Crear el intent y agregar los datos del usuario
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("username", usuario);
                intent.putExtra("nombreCompleto", nombreUsuario);

                // Iniciar MainActivity y cerrar LoginActivity
                startActivity(intent);
                finish();
            } else {
                // Credenciales incorrectas
                mostrarMensaje(getString(R.string.error_login_failed));

            }
        });

        // Configurar el botón de registro
        btnRegistro.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // Configurar el botón de volver
        btnVolver.setOnClickListener(v -> finish());
    }

    // Método auxiliar para mostrar mensajes Toast
    private void mostrarMensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    // Sobrescribir onResume para limpiar los campos cuando se vuelve a la actividad
    @Override
    protected void onResume() {
        super.onResume();
        // Limpiar los campos de texto
        EditText etUsuario = findViewById(R.id.et_username);
        EditText etPassword = findViewById(R.id.et_password);
        etUsuario.setText("");
        etPassword.setText("");
    }
}