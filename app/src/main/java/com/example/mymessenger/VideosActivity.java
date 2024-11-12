package com.example.mymessenger;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import androidx.appcompat.app.AppCompatActivity;

public class VideosActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        ListView listView = findViewById(R.id.list_view);
        String[] videoItems = {
                "YouTube Tendencias",
                "TikTok Trending",
                "Vimeo Staff Picks",
                "Twitch Streams",
                "Dailymotion Popular"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, videoItems);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String url = "";
            switch(position) {
                case 0:
                    url = "https://www.youtube.com/feed/trending";
                    break;
                case 1:
                    url = "https://www.tiktok.com/trending";
                    break;
                case 2:
                    url = "https://vimeo.com/watch";
                    break;
                case 3:
                    url = "https://www.twitch.tv/directory";
                    break;
                case 4:
                    url = "https://www.dailymotion.com/trending";
                    break;
            }
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        });
    }
}