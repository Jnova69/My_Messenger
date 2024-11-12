package com.example.mymessenger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;

public class TrackAdapter extends ArrayAdapter<String> {
    private Context context;
    private ArrayList<String> tracks;
    private int currentlyPlayingPosition = -1;

    public TrackAdapter(Context context, ArrayList<String> tracks) {
        super(context, R.layout.track_item, tracks);
        this.context = context;
        this.tracks = tracks;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.track_item, parent, false);
        }

        TextView trackNameTextView = convertView.findViewById(R.id.text_track_name);
        TextView artistTextView = convertView.findViewById(R.id.text_artist);
        TextView tapToPlayTextView = convertView.findViewById(R.id.text_tap_to_play);

        String[] trackInfo = tracks.get(position).split(" - ");
        if (trackInfo.length > 1) {
            artistTextView.setText(trackInfo[0]);
            trackNameTextView.setText(trackInfo[1]);
        } else {
            trackNameTextView.setText(tracks.get(position));
            artistTextView.setVisibility(View.GONE);
        }

        if (position == currentlyPlayingPosition) {
            tapToPlayTextView.setText("Reproduciendo... Toca para detener");
            tapToPlayTextView.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
        } else {
            tapToPlayTextView.setText("Toca para escuchar el preview");
            tapToPlayTextView.setTextColor(context.getResources().getColor(android.R.color.holo_blue_dark));
        }

        return convertView;
    }

    public void setCurrentlyPlayingPosition(int position) {
        this.currentlyPlayingPosition = position;
        notifyDataSetChanged();
    }
}