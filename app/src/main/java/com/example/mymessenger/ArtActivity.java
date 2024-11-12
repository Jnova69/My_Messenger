package com.example.mymessenger;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import androidx.appcompat.app.AppCompatActivity;

public class ArtActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        ListView listView = findViewById(R.id.list_view);
        String[] artItems = {
                "Museo del Prado",
                "MoMA",
                "Louvre",
                "DeviantArt",
                "ArtStation"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, artItems);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String url = "";
            switch(position) {
                case 0:
                    url = "https://www.museodelprado.es/";
                    break;
                case 1:
                    url = "https://www.moma.org/";
                    break;
                case 2:
                    url = "https://www.louvre.fr/";
                    break;
                case 3:
                    url = "https://www.deviantart.com/";
                    break;
                case 4:
                    url = "https://www.artstation.com/";
                    break;
            }
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        });
    }
}