package com.example.mymessenger;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.Arrays;

public class SportsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports);

        ArrayList<SportItem> sportsItems = new ArrayList<>(Arrays.asList(
                new SportItem(
                        "ESPN Deportes",
                        "Noticias deportivas en español",
                        "https://1000marcas.net/wp-content/uploads/2020/02/logo-ESPN.png",
                        "Cobertura completa de deportes incluyendo fútbol, béisbol, baloncesto y más",
                        "https://espndeportes.espn.com/"
                ),
                new SportItem(
                        "FIFA",
                        "Fútbol Internacional",
                        "https://www.fifplay.com/img/public/fifa-logo-transparent.png",
                        "Toda la información sobre el fútbol mundial, torneos, rankings y eventos",
                        "https://www.fifa.com/es"
                ),
                new SportItem(
                        "NBA",
                        "Baloncesto Profesional",
                        "https://assets.stickpng.com/images/58428defa6515b1e0ad75ab4.png",
                        "Resultados en vivo, estadísticas, noticias y highlights de la NBA",
                        "https://www.nba.com/espanol"
                ),
                new SportItem(
                        "UEFA Champions League",
                        "Liga de Campeones",
                        "https://logodownload.org/wp-content/uploads/2017/05/uefa-champions-league-logo-0.png",
                        "La competición de clubes más prestigiosa del fútbol europeo",
                        "https://es.uefa.com/uefachampionsleague/"
                ),
                new SportItem(
                        "NFL",
                        "Fútbol Americano",
                        "https://upload.wikimedia.org/wikipedia/en/thumb/a/a2/National_Football_League_logo.svg/1200px-National_Football_League_logo.svg.png",
                        "Toda la acción del fútbol americano profesional",
                        "https://www.nfl.com/es/"
                )
        ));
        RecyclerView recyclerView = findViewById(R.id.recycler_sports);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new SportsAdapter(sportsItems, this::onSportItemClick));
    }

    // ... resto del código permanece igual ...
    private void onSportItemClick(SportItem item) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.websiteUrl));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "No se pudo abrir el sitio web", Toast.LENGTH_SHORT).show();
        }
    }

    private static class SportItem {
        String title;
        String subtitle;
        String imageUrl;
        String description;
        String websiteUrl;

        SportItem(String title, String subtitle, String imageUrl, String description, String websiteUrl) {
            this.title = title;
            this.subtitle = subtitle;
            this.imageUrl = imageUrl;
            this.description = description;
            this.websiteUrl = websiteUrl;
        }
    }

    private static class SportsAdapter extends RecyclerView.Adapter<SportsAdapter.SportViewHolder> {
        private final ArrayList<SportItem> sportsItems;
        private final OnSportItemClickListener clickListener;

        public interface OnSportItemClickListener {
            void onItemClick(SportItem item);
        }

        public SportsAdapter(ArrayList<SportItem> sportsItems, OnSportItemClickListener listener) {
            this.sportsItems = sportsItems;
            this.clickListener = listener;
        }

        @NonNull
        @Override
        public SportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_sport, parent, false);
            return new SportViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SportViewHolder holder, int position) {
            SportItem item = sportsItems.get(position);
            holder.bind(item, clickListener);
        }

        @Override
        public int getItemCount() {
            return sportsItems.size();
        }

        static class SportViewHolder extends RecyclerView.ViewHolder {
            final ImageView imageView;
            final TextView titleView;
            final TextView subtitleView;
            final TextView descriptionView;

            SportViewHolder(View view) {
                super(view);
                imageView = view.findViewById(R.id.image_sport_logo);
                titleView = view.findViewById(R.id.text_sport_title);
                subtitleView = view.findViewById(R.id.text_sport_subtitle);
                descriptionView = view.findViewById(R.id.text_sport_description);
            }

            void bind(final SportItem item, final OnSportItemClickListener listener) {
                titleView.setText(item.title);
                subtitleView.setText(item.subtitle);
                descriptionView.setText(item.description);

                RequestOptions options = new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.sport_placeholder)
                        .error(R.drawable.error_placeholder)
                        .centerInside()
                        .timeout(15000);

                Glide.with(itemView.getContext())
                        .load(item.imageUrl)
                        .apply(options)
                        .override(300, 300)
                        .into(imageView);

                itemView.setOnClickListener(v -> listener.onItemClick(item));
            }
        }
    }
}