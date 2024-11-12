package com.example.mymessenger;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MusicTrendsActivity extends AppCompatActivity {
    private static final String TAG = "MusicTrendsActivity";
    private ListView trendsListView;
    private ProgressBar progressBar;
    private TextView loadingText;
    private HashMap<String, String> trackUrls;
    private MediaPlayer mediaPlayer;
    private int currentlyPlayingPosition = -1;
    private TrackAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_trends);

        trendsListView = findViewById(R.id.list_music_trends);
        progressBar = findViewById(R.id.progress_bar);
        loadingText = findViewById(R.id.text_loading);
        trackUrls = new HashMap<>();

        initializeMediaPlayer();

        ArrayList<String> trendingTracks = getIntent().getStringArrayListExtra("trending_tracks");
        ArrayList<String> trackUrlsList = getIntent().getStringArrayListExtra("track_urls");

        if (trendingTracks != null && !trendingTracks.isEmpty() && trackUrlsList != null) {
            for (int i = 0; i < trendingTracks.size(); i++) {
                trackUrls.put(trendingTracks.get(i), trackUrlsList.get(i));
            }
            showTrendingTracks(trendingTracks);
        } else {
            showError();
        }

        trendsListView.setOnItemClickListener((parent, view, position, id) -> {
            assert trendingTracks != null;
            String selectedTrack = trendingTracks.get(position);
            String trackUrl = trackUrls.get(selectedTrack);
            Log.d(TAG, "Selected track URL: " + trackUrl);

            if (currentlyPlayingPosition == position && mediaPlayer.isPlaying()) {
                stopPlayback();
            } else {
                playTrack(trackUrl, position);
            }
        });
    }

    private void initializeMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(
                new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
        );
        mediaPlayer.setOnErrorListener((mp, what, extra) -> {
            Log.e(TAG, "MediaPlayer error: " + what + ", " + extra);
            Toast.makeText(MusicTrendsActivity.this, "Error al reproducir la pista", Toast.LENGTH_SHORT).show();
            return false;
        });
    }
//adaptador
    private void showTrendingTracks(ArrayList<String> tracks) {
        progressBar.setVisibility(View.GONE);
        loadingText.setVisibility(View.GONE);
        trendsListView.setVisibility(View.VISIBLE);
        adapter = new TrackAdapter(this, tracks);
        trendsListView.setAdapter(adapter);
    }

    private void showError() {
        progressBar.setVisibility(View.GONE);
        trendsListView.setVisibility(View.GONE);
        loadingText.setVisibility(View.VISIBLE);
        loadingText.setText("No se encontraron tendencias musicales");
    }

    private void playTrack(String trackUrl, int position) {
        try {
            stopPlayback();
            mediaPlayer.setDataSource(trackUrl);
            mediaPlayer.prepareAsync();
            Toast.makeText(this, "Cargando preview...", Toast.LENGTH_SHORT).show();

            mediaPlayer.setOnPreparedListener(mp -> {
                mp.start();
                currentlyPlayingPosition = position;
        //adatador
                adapter.setCurrentlyPlayingPosition(position);
                Toast.makeText(MusicTrendsActivity.this, "Reproduciendo preview", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "MediaPlayer started playing");
            });

            mediaPlayer.setOnCompletionListener(mp -> {
                Log.d(TAG, "MediaPlayer playback completed");
                stopPlayback();
            });
        } catch (IOException e) {
            Log.e(TAG, "Error setting data source: " + e.getMessage());
            Toast.makeText(this, "Error al reproducir la pista: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void stopPlayback() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer.reset();
        currentlyPlayingPosition = -1;
        adapter.setCurrentlyPlayingPosition(-1);
        Log.d(TAG, "MediaPlayer stopped and reset");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}