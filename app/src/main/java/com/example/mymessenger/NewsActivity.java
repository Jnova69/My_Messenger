package com.example.mymessenger;

import android.os.Bundle;
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
import android.widget.ImageButton;

public class NewsActivity extends AppCompatActivity {
    private ArrayList<NewsItem> newsList;
    private NewsAdapter adapter;
    private static final String API_KEY = "pub_589874190895902c004d5addb059719d7d4b0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        // Configurar el botón de retorno si existe en el layout
        ImageButton btnReturn = findViewById(R.id.btn_toolbar_return);
        if (btnReturn != null) {
            btnReturn.setOnClickListener(v -> onBackPressed());
        }

        ListView listView = findViewById(R.id.list_news);
        newsList = new ArrayList<>();
        adapter = new NewsAdapter(this, newsList);
        listView.setAdapter(adapter);

        fetchNews();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            NewsItem selectedItem = newsList.get(position);
            Toast.makeText(getApplicationContext(),
                    "Leyendo: " + selectedItem.getTitle(),
                    Toast.LENGTH_SHORT).show();
        });
    }

    private void fetchNews() {
        String url = "https://newsdata.io/api/1/news?apikey=" + API_KEY +
                "&country=co&language=es";

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray results = response.getJSONArray("results");
                        newsList.clear(); // Limpiar lista antes de agregar nuevos items
                        for (int i = 0; i < results.length(); i++) {
                            JSONObject article = results.getJSONObject(i);
                            NewsItem newsItem = new NewsItem(
                                    article.getString("title"),
                                    article.optString("description", "Sin descripción disponible"),
                                    article.optString("pubDate", "Fecha no disponible"),
                                    article.optString("source", "Fuente no disponible")
                            );
                            newsList.add(newsItem);
                        }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(NewsActivity.this,
                                "Error al cargar las noticias",
                                Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(NewsActivity.this,
                        "Error de conexión",
                        Toast.LENGTH_SHORT).show()
        );

        queue.add(request);
    }
}