package com.example.bookapp.ui.book;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.bookapp.databinding.ActivityBookDetailsBinding;

public class BookDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityBookDetailsBinding binding = ActivityBookDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle extras = getIntent().getExtras();

        binding.bookDetailsId.setText("ID: " + extras.getString("id"));
        binding.bookDetailsTitle.setText("Title: " + extras.getString("title"));
        binding.bookDetailsAuthor.setText("Author: " + extras.getString("author"));
        binding.bookDetailsCategory.setText("Category " + extras.getString("category"));
        binding.bookDetailsPrice.setText("Price: " + String.valueOf(extras.getInt("price")) + "$");

        binding.bookDetailsBackButton.setOnClickListener(v -> onBackPressed());
    }

}
