package com.example.mymessenger;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import androidx.appcompat.app.AppCompatActivity;

public class TravelActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        ListView listView = findViewById(R.id.list_view);
        String[] travelItems = {
                "Lonely Planet",
                "TripAdvisor",
                "Booking.com",
                "National Geographic Travel",
                "Airbnb Experiences"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, travelItems);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String url = "";
            switch(position) {
                case 0:
                    url = "https://www.lonelyplanet.com/";
                    break;
                case 1:
                    url = "https://www.tripadvisor.com/";
                    break;
                case 2:
                    url = "https://www.booking.com/";
                    break;
                case 3:
                    url = "https://www.nationalgeographic.com/travel";
                    break;
                case 4:
                    url = "https://www.airbnb.com/s/experiences";
                    break;
            }
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        });
    }
}