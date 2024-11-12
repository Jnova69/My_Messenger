package com.example.UsuarioExpress;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mymessenger.R;

public class RegisterActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inicializar el helper de la base de datos
        databaseHelper = new DatabaseHelper(this);

        EditText nameEditText = findViewById(R.id.et_name_register);
        EditText usernameEditText = findViewById(R.id.et_username_register);
        EditText emailEditText = findViewById(R.id.et_email_register);
        EditText passwordEditText = findViewById(R.id.et_password_register);
        Button registerButton = findViewById(R.id.btn_register_confirm);
        Button loginButton = findViewById(R.id.btn_go_login);

        registerButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String username = usernameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            // Validar campos obligatorios
            if (username.isEmpty() || password.isEmpty()) {
                mostrarMensaje(getString(R.string.error_campos_obligatorios));
                return;
            }

            // Intentar insertar el nuevo usuario
            long result = databaseHelper.insertarUsuario(name, username, email, password);

            if (result != -1) {
                mostrarMensaje(getString(R.string.registro_exitoso));
                // Redirigir al login
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            } else {
                mostrarMensaje(getString(R.string.error_usuario_existe));
            }
        });

        loginButton.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void mostrarMensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }
}