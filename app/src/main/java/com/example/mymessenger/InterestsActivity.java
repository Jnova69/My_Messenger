package com.example.mymessenger;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.net.Uri;
import com.android.volley.DefaultRetryPolicy;
import java.nio.charset.StandardCharsets;

public class InterestsActivity extends AppCompatActivity {
    private static final String TAG = "InterestsActivity";
    private static final String LAST_FM_API_KEY = "1580fb9b078c67f4ab8971a49b1378c5";
    private static final String LAST_FM_API_URL = "https://ws.audioscrobbler.com/2.0/?method=chart.gettoptracks&limit=20&api_key=" + LAST_FM_API_KEY + "&format=json";
    private static final String TMDB_API_KEY = "80436a5c02f795ac3af429978cb17245";
    private static final String TMDB_API_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=" + TMDB_API_KEY + "&language=es-ES";
    private static final String NYT_API_KEY = "aQNfVSaQFsb5kBaiRR3aDe3e3NkZmjJn";


    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interests);
        Log.d(TAG, "onCreate: Iniciando InterestsActivity");

        requestQueue = Volley.newRequestQueue(this);

        ListView listView = findViewById(R.id.list_interests);
        String[] interests = {"Música", "Películas", "Libros", "Deportes", "Redes", "Arte", "Cocina", "Viajes"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, interests);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = interests[position];
            Log.d(TAG, "onItemClick: Seleccionado " + selectedItem);
            switch (selectedItem) {
                case "Música":
                    fetchMusicTrends();
                    break;
                case "Películas":
                    fetchMovieTrends();
                    break;
                case "Libros":
                    fetchBookTrends();
                    break;
                case "Deportes":
                    startActivity(new Intent(this, SportsActivity.class));
                    break;
                case "Redes":
                    startActivity(new Intent(this, VideosActivity.class));
                    break;
                case "Arte":
                    startActivity(new Intent(this, ArtActivity.class));
                    break;
                case "Cocina":
                    startActivity(new Intent(this, CookingActivity.class));
                    break;
                case "Viajes":
                    startActivity(new Intent(this, TravelActivity.class));
                    break;
                default:
                    Toast.makeText(this, "Seleccionaste: " + selectedItem, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchMusicTrends() {
        Log.d(TAG, "fetchMusicTrends: Iniciando solicitud de música");
        Toast.makeText(this, "Cargando tendencias musicales...", Toast.LENGTH_SHORT).show();

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                LAST_FM_API_URL,
                null,
                response -> {
                    try {
                        JSONObject tracks = response.getJSONObject("tracks");
                        JSONArray trackList = tracks.getJSONArray("track");
                        ArrayList<String> trendingTracks = new ArrayList<>();
                        ArrayList<String> trackUrls = new ArrayList<>();

                        for (int i = 0; i < trackList.length(); i++) {
                            JSONObject track = trackList.getJSONObject(i);
                            String name = track.getString("name");
                            String artist = track.getJSONObject("artist").getString("name");
                            String url = track.getString("url"); // La URL es un string directo, no un array

                            // Formato: "Artista - Nombre de la canción"
                            trendingTracks.add(artist + " - " + name);
                            trackUrls.add(url);
                        }

                        Intent intent = new Intent(this, MusicTrendsActivity.class);
                        intent.putStringArrayListExtra("trending_tracks", trendingTracks);
                        intent.putStringArrayListExtra("track_urls", trackUrls);
                        startActivity(intent);

                    } catch (JSONException e) {
                        Log.e(TAG, "fetchMusicTrends: Error al procesar JSON: " + e.getMessage(), e);
                        Toast.makeText(this, "Error al procesar datos de música: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    Log.e(TAG, "fetchMusicTrends: Error de red: " + error.toString(), error);
                    Toast.makeText(this, "Error de conexión al cargar música: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json");
                return headers;
            }
        };

        request.setShouldCache(false); // Desactivamos el caché para obtener siempre datos frescos
        requestQueue.add(request);
    }
    private void fetchMovieTrends() {
        Log.d(TAG, "fetchMovieTrends: Iniciando solicitud de películas");
        Toast.makeText(this, "Cargando películas recientes...", Toast.LENGTH_SHORT).show();

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                TMDB_API_URL,
                null,
                response -> {
                    try {
                        JSONArray results = response.getJSONArray("results");
                        ArrayList<String> movieTitles = new ArrayList<>();
                        ArrayList<String> movieOverviews = new ArrayList<>();
                        ArrayList<String> moviePosters = new ArrayList<>();

                        for (int i = 0; i < results.length(); i++) {
                            JSONObject movie = results.getJSONObject(i);
                            String title = movie.getString("title");
                            String overview = movie.getString("overview");
                            String posterPath = "https://image.tmdb.org/t/p/w500" + movie.getString("poster_path");

                            movieTitles.add(title);
                            movieOverviews.add(overview);
                            moviePosters.add(posterPath);
                        }

                        Intent intent = new Intent(this, MovieTrendsActivity.class);
                        intent.putExtra("movie_titles", movieTitles);
                        intent.putExtra("movie_overviews", movieOverviews);
                        intent.putExtra("movie_posters", moviePosters);
                        startActivity(intent);

                    } catch (JSONException e) {
                        Log.e(TAG, "fetchMovieTrends: Error al procesar JSON: " + e.getMessage(), e);
                        Toast.makeText(this, "Error al procesar datos de películas: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    Log.e(TAG, "fetchMovieTrends: Error de red: " + error.toString(), error);
                    Toast.makeText(this, "Error de conexión al cargar películas: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json");
                return headers;
            }
        };

        requestQueue.add(request);
    }

    private void fetchBookTrends() {
        Log.d(TAG, "fetchBookTrends: Iniciando solicitud de libros");
        Toast.makeText(this, "Cargando libros recientes...", Toast.LENGTH_SHORT).show();
        String url = "https://api.nytimes.com/svc/books/v3/lists/current/hardcover-fiction.json";

        // Construimos la URL con los parámetros
        Uri.Builder builder = Uri.parse(url).buildUpon();
        builder.appendQueryParameter("api-key", NYT_API_KEY);
        String apiUrl = builder.build().toString();

        Log.d(TAG, "URL de la API: " + apiUrl); // Para debugging

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                apiUrl,
                null,
                response -> {
                    Log.d(TAG, "Respuesta recibida: " + response.toString());
                    try {
                        if (!response.has("results")) {
                            throw new JSONException("No se encontró el objeto 'results' en la respuesta");
                        }

                        JSONObject results = response.getJSONObject("results");
                        if (!results.has("books")) {
                            throw new JSONException("No se encontró el array 'books' en los resultados");
                        }

                        JSONArray books = results.getJSONArray("books");
                        ArrayList<String> bookTitles = new ArrayList<>();
                        ArrayList<String> bookAuthors = new ArrayList<>();
                        ArrayList<String> bookCovers = new ArrayList<>();
                        ArrayList<String> bookDescriptions = new ArrayList<>();

                        for (int i = 0; i < books.length(); i++) {
                            JSONObject book = books.getJSONObject(i);

                            String title = book.optString("title", "Sin título");
                            String author = book.optString("author", "Autor desconocido");
                            String coverUrl = book.optString("book_image", "");
                            String description = book.optString("description", "Sin descripción disponible");

                            // Validación adicional de la URL de la imagen
                            if (!coverUrl.startsWith("http")) {
                                coverUrl = ""; // Si la URL no es válida, la dejamos vacía
                            }

                            // Log para debugging
                            Log.d(TAG, String.format("Libro %d: %s por %s", i, title, author));
                            Log.d(TAG, "Cover URL: " + coverUrl);

                            bookTitles.add(title);
                            bookAuthors.add(author);
                            bookCovers.add(coverUrl);
                            bookDescriptions.add(description);
                        }

                        if (bookTitles.isEmpty()) {
                            Toast.makeText(this, "No se encontraron libros disponibles", Toast.LENGTH_LONG).show();
                            return;
                        }

                        Intent intent = new Intent(this, BookTrendsActivity.class);
                        intent.putStringArrayListExtra("book_titles", bookTitles);
                        intent.putStringArrayListExtra("book_authors", bookAuthors);
                        intent.putStringArrayListExtra("book_covers", bookCovers);
                        intent.putStringArrayListExtra("book_descriptions", bookDescriptions);
                        startActivity(intent);

                    } catch (JSONException e) {
                        Log.e(TAG, "fetchBookTrends: Error al procesar JSON: " + e.getMessage(), e);
                        Toast.makeText(this, "Error al procesar datos de libros: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    String errorMsg = "Error de conexión";
                    if (error.networkResponse != null) {
                        errorMsg += " (Código: " + error.networkResponse.statusCode + ")";

                        // Log del error completo
                        String errorData = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                        Log.e(TAG, "Error detallado: " + errorData);
                    }

                    Log.e(TAG, "fetchBookTrends: " + errorMsg, error);
                    Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json");
                return headers;
            }
        };

        // Configuración adicional de la request
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000, // 30 segundos de timeout
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        request.setShouldCache(false);
        requestQueue.add(request);
    }
}