package com.example.bookapp.ui.book;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.bookapp.R;
import com.example.bookapp.model.Book;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddBookActivity extends AppCompatActivity {

    EditText bookTitleEditText, bookAuthorEditText, bookPriceEditText;
    Button submitButton;
    ImageButton backButton;

    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        bookTitleEditText = findViewById(R.id.book_title_edit_text);
        bookAuthorEditText = findViewById(R.id.book_author_edit_text);
        bookPriceEditText = findViewById(R.id.book_price_edit_text);

        backButton = findViewById(R.id.add_book_back_button);
        submitButton = findViewById(R.id.add_book_submit_button);

        backButton.setOnClickListener(v -> onBackPressed());

        submitButton.setOnClickListener(this::handleSubmit);

        category = getIntent().getStringExtra("category");
    }

    private void handleSubmit(View v) {
        String bookTitle = bookTitleEditText.getText().toString().trim();
        String bookAuthor = bookAuthorEditText.getText().toString().trim();
        String bookPriceStr =  bookPriceEditText.getText().toString().trim();

        if (bookTitle.isEmpty() || bookAuthor.isEmpty() || bookPriceStr.isEmpty()) {
            Toast.makeText(this, "Some fields are missing!", Toast.LENGTH_SHORT).show();
            return;
        }

        int bookPrice = Integer.parseInt(bookPriceStr);

        Book book = new Book();
        book.setId(String.valueOf(System.currentTimeMillis()));
        book.setTitle(bookTitle);
        book.setAuthor(bookAuthor);
        book.setCategory(category);
        book.setPrice(bookPrice);

        addBook(book);
    }

    private void addBook(Book book) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books");
        ref.child(book.getId()).setValue(book).addOnSuccessListener(unused -> {
            Toast.makeText(this, "Book added successfully!", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }).addOnFailureListener(e -> {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}
