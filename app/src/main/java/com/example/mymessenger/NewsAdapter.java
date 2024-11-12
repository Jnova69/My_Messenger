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

public class NewsAdapter extends ArrayAdapter<NewsItem> {
    private Context context;
    private ArrayList<NewsItem> news;

    public NewsAdapter(Context context, ArrayList<NewsItem> news) {
        super(context, R.layout.news_item, news);
        this.context = context;
        this.news = news;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.news_item, parent, false);
        }

        NewsItem item = news.get(position);

        TextView titleTextView = convertView.findViewById(R.id.text_news_title);
        TextView descriptionTextView = convertView.findViewById(R.id.text_news_description);
        TextView sourceTextView = convertView.findViewById(R.id.text_news_source);
        TextView dateTextView = convertView.findViewById(R.id.text_news_date);

        titleTextView.setText(item.getTitle());
        descriptionTextView.setText(item.getDescription());
        sourceTextView.setText(item.getSource());
        dateTextView.setText(item.getDate());

        return convertView;
    }
}