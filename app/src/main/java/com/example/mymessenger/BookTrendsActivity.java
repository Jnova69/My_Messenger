package com.example.mymessenger;
import android.os.Bundle;
import android.util.Log;
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
import java.util.ArrayList;

public class BookTrendsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_trends);

        ArrayList<String> bookTitles = getIntent().getStringArrayListExtra("book_titles");
        ArrayList<String> bookAuthors = getIntent().getStringArrayListExtra("book_authors");
        ArrayList<String> bookCovers = getIntent().getStringArrayListExtra("book_covers");
        ArrayList<String> bookDescriptions = getIntent().getStringArrayListExtra("book_descriptions");

        if (bookTitles == null || bookAuthors == null || bookCovers == null || bookDescriptions == null) {
            Toast.makeText(this, "Error: No se recibieron datos de libros", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        RecyclerView recyclerView = findViewById(R.id.recycler_books);
        if (recyclerView == null) {
            Toast.makeText(this, "Error: No se encontró el RecyclerView", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new BookAdapter(bookTitles, bookAuthors, bookCovers, bookDescriptions));
    }

    private static class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
        private final ArrayList<String> titles;
        private final ArrayList<String> authors;
        private final ArrayList<String> covers;
        private final ArrayList<String> descriptions;

        public BookAdapter(ArrayList<String> titles, ArrayList<String> authors,
                           ArrayList<String> covers, ArrayList<String> descriptions) {
            this.titles = titles;
            this.authors = authors;
            this.covers = covers;
            this.descriptions = descriptions;
        }

        @NonNull
        @Override
        public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_book, parent, false);
            return new BookViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
            holder.titleView.setText(titles.get(position));
            holder.authorView.setText(authors.get(position));
            holder.descriptionView.setText(descriptions.get(position));

            Glide.with(holder.itemView.getContext())
                    .load(covers.get(position))
                    .into(holder.coverView);
        }

        @Override
        public int getItemCount() {
            return titles.size();
        }

        static class BookViewHolder extends RecyclerView.ViewHolder {
            final ImageView coverView;
            final TextView titleView;
            final TextView authorView;
            final TextView descriptionView;

            BookViewHolder(View view) {
                super(view);
                coverView = view.findViewById(R.id.image_book_cover);
                titleView = view.findViewById(R.id.text_book_title);
                authorView = view.findViewById(R.id.text_book_author);
                descriptionView = view.findViewById(R.id.text_book_description);

                // Agregar log para depuración
                if (coverView == null) Log.e("BookViewHolder", "coverView is null");
                if (titleView == null) Log.e("BookViewHolder", "titleView is null");
                if (authorView == null) Log.e("BookViewHolder", "authorView is null");
                if (descriptionView == null) Log.e("BookViewHolder", "descriptionView is null");
            }
        }
    }
}
