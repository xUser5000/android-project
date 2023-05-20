package com.example.bookapp.ui.book;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bookapp.adapter.BookAdapter;
import com.example.bookapp.databinding.ActivityBookListBinding;
import com.example.bookapp.model.Book;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BookListActivity extends AppCompatActivity {
    private ActivityBookListBinding binding;

    String category = null;
    boolean isAdmin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        category = getIntent().getStringExtra("category");
        isAdmin = getIntent().getBooleanExtra("is_admin", false);

        String title = (category != null) ? (category + " books") : ("All books");
        binding.bookListTitle.setText(title);

        if (isAdmin) {
            binding.fab.setOnClickListener(view -> {
                Intent intent = new Intent(BookListActivity.this, AddBookActivity.class);
                intent.putExtra("category", category);
                startActivity(intent);
            });
        } else {
            binding.fab.setVisibility(View.GONE);
        }

        binding.bookListBackButton.setOnClickListener(v -> onBackPressed());

        loadBooks();
    }

    private void loadBooks() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Book> books = new ArrayList<>();

                for (DataSnapshot ds: snapshot.getChildren())
                    books.add(ds.getValue(Book.class));

                books = books
                        .stream()
                        .filter(book -> category == null || book.getCategory().equals(category))
                        .sorted(Comparator.comparing(Book::getTitle))
                        .collect(Collectors.toList());

                if (!books.isEmpty()) {
                    binding.booksRecyclerView.setVisibility(View.VISIBLE);
                    binding.noBooksFoundTextView.setVisibility(View.GONE);
                    BookAdapter bookAdapter = new BookAdapter(BookListActivity.this, books, isAdmin);
                    binding.booksRecyclerView.setAdapter(bookAdapter);
                } else {
                    binding.booksRecyclerView.setVisibility(View.GONE);
                    binding.noBooksFoundTextView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadBooks();
    }
}
