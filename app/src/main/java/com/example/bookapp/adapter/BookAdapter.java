package com.example.bookapp.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookapp.databinding.RowBookBinding;
import com.example.bookapp.model.Book;
import com.example.bookapp.model.Category;
import com.example.bookapp.ui.book.BookDetailsActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;


public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookHolder>{
    private final Context context;
    private RowBookBinding binding;
    private List<Book> booksArrayList;

    private boolean isAdmin = false;

    public BookAdapter(Context context, List<Book> booksArrayList, boolean isAdmin) {
        this.context = context;
        this.booksArrayList = booksArrayList;
        this.isAdmin = isAdmin;
    }

    @NonNull
    @Override
    public BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowBookBinding.inflate(LayoutInflater.from(context),parent,false);
        return new BookHolder(binding.getRoot());
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull BookAdapter.BookHolder holder, int position) {
        Book book = booksArrayList.get(position);
        holder.bookTitleView.setText(book.getTitle());
        holder.bookAuthorView.setText(book.getAuthor());
        holder.bookPriceView.setText(book.getPrice() + "$");

        if (isAdmin) {
            holder.deleteButton.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete").setMessage("Are you sure to delete this book? ")
                        .setPositiveButton("Confirm", (dialog, which) -> deleteBook(booksArrayList.get(position)))
                        .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                        .show();
            });
        } else {
            holder.deleteButton.setVisibility(View.GONE);
        }
    }

    private void deleteBook(Book book) {
        String id = book.getId();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books");
        ref.child(id)
                .removeValue()
                .addOnSuccessListener(unused -> Toast.makeText(context,book.getTitle() + " was deleted",Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(context," "+e.getMessage(),Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return booksArrayList.size();
    }

    class BookHolder extends RecyclerView.ViewHolder{
        TextView bookTitleView, bookAuthorView, bookPriceView;
        ImageButton deleteButton;

        public BookHolder(@NonNull View itemView) {
            super(itemView);
            bookTitleView = binding.bookTitle;
            bookAuthorView = binding.bookAuthor;
            bookPriceView = binding.bookPrice;
            deleteButton = binding.bookDeleteButton;

            itemView.setOnClickListener(v -> {
                Book book = booksArrayList.get(getAdapterPosition());

                Intent intent = new Intent(context, BookDetailsActivity.class);
                intent.putExtra("id", book.getId());
                intent.putExtra("title", book.getTitle());
                intent.putExtra("author", book.getAuthor());
                intent.putExtra("category", book.getCategory());
                intent.putExtra("price", book.getPrice());

                context.startActivity(intent);
            });
        }
    }

}
