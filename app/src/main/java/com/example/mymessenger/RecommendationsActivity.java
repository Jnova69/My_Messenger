package com.example.mymessenger;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RecommendationsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendations);

        ListView listView = findViewById(R.id.list_recommendations);
        String[] recommendations = {
                "Libro: '1984' de George Orwell",
                "Película: 'Inception'",
                "Serie: 'Stranger Things'",
                "Juego: 'The Legend of Zelda: Breath of the Wild'",
                "Podcast: 'Serial'",
                "App: 'Duolingo'"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, recommendations);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = recommendations[position];
            Toast.makeText(getApplicationContext(), "Seleccionaste: " + selectedItem, Toast.LENGTH_SHORT).show();

        });
    }
}