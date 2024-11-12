package com.example.mymessenger;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import androidx.appcompat.app.AppCompatActivity;

public class CookingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        ListView listView = findViewById(R.id.list_view);
        String[] cookingItems = {
                "Recetas Allrecipes",
                "Food Network",
                "BBC Good Food",
                "Tasty",
                "Kitchen Stories"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, cookingItems);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String url = "";
            switch(position) {
                case 0:
                    url = "https://www.allrecipes.com/";
                    break;
                case 1:
                    url = "https://www.foodnetwork.com/";
                    break;
                case 2:
                    url = "https://www.bbcgoodfood.com/";
                    break;
                case 3:
                    url = "https://tasty.co/";
                    break;
                case 4:
                    url = "https://www.kitchenstories.com/";
                    break;
            }
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        });
    }
}