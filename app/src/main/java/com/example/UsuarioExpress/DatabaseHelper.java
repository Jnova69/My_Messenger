package com.example.UsuarioExpress;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper implements AutoCloseable {
    private static final String DATABASE_NAME = "BaseDatosUsuarios";
    private static final int DATABASE_VERSION = 1;

    // Tabla Usuarios
    private static final String TABLA_USUARIOS = "usuarios";
    private static final String COLUMNA_ID = "id";
    private static final String COLUMNA_NOMBRE = "nombre";
    private static final String COLUMNA_USUARIO = "usuario";
    private static final String COLUMNA_EMAIL = "email";
    private static final String COLUMNA_PASSWORD = "password";

    // SQL para crear la tabla de usuarios
    private static final String CREAR_TABLA_USUARIOS = "CREATE TABLE " + TABLA_USUARIOS + "("
            + COLUMNA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMNA_NOMBRE + " TEXT, "
            + COLUMNA_USUARIO + " TEXT UNIQUE, "
            + COLUMNA_EMAIL + " TEXT UNIQUE, "
            + COLUMNA_PASSWORD + " TEXT)";

    private final SQLiteDatabase db;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREAR_TABLA_USUARIOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_USUARIOS);
        onCreate(db);
    }

    @Override
    public void close() {
        if (db != null && db.isOpen()) {
            db.close();
        }
        super.close();
    }


    // Método para insertar un nuevo usuario
    public long insertarUsuario(String nombre, String usuario, String email, String password) {
        // Verificar si el usuario ya existe
        if (usuarioExiste(usuario)) {
            return -1;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();

        // Si el nombre está vacío, usar el nombre de usuario como nombre
        valores.put(COLUMNA_NOMBRE, nombre.isEmpty() ? usuario : nombre);
        valores.put(COLUMNA_USUARIO, usuario);
        valores.put(COLUMNA_EMAIL, email);
        valores.put(COLUMNA_PASSWORD, password);

        long id = db.insert(TABLA_USUARIOS, null, valores);
        db.close();
        return id;
    }

    // Método para verificar si un usuario ya existe
    private boolean usuarioExiste(String usuario) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columnas = {COLUMNA_ID};
        String seleccion = COLUMNA_USUARIO + " = ?";
        String[] argumentosSeleccion = {usuario};

        Cursor cursor = db.query(TABLA_USUARIOS, columnas, seleccion, argumentosSeleccion,
                null, null, null);
        boolean existe = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return existe;
    }

    // Método para verificar credenciales (renombrado de verificarUsuario)
    public boolean verificarCredenciales(String usuario, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columnas = {COLUMNA_ID};
        String seleccion = COLUMNA_USUARIO + " = ? AND " + COLUMNA_PASSWORD + " = ?";
        String[] argumentosSeleccion = {usuario, password};

        Cursor cursor = db.query(TABLA_USUARIOS, columnas, seleccion, argumentosSeleccion,
                null, null, null);
        int contador = cursor.getCount();
        cursor.close();
        db.close();

        return contador > 0;
    }

    // Método para obtener el nombre completo del usuario
    @SuppressLint("Range")
    public String getNombreUsuario(String usuario) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columnas = {COLUMNA_NOMBRE};
        String seleccion = COLUMNA_USUARIO + " = ?";
        String[] argumentosSeleccion = {usuario};
        String nombreUsuario = "";

        Cursor cursor = db.query(TABLA_USUARIOS, columnas, seleccion, argumentosSeleccion,
                null, null, null);

        if (cursor.moveToFirst()) {
            nombreUsuario = cursor.getString(cursor.getColumnIndex(COLUMNA_NOMBRE));
        }

        cursor.close();
        db.close();
        return nombreUsuario;
    }
}