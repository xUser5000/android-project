package com.example.bookapp.adapter;

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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookapp.model.Category;
import com.example.bookapp.databinding.RowCategoryBinding;
import com.example.bookapp.ui.book.BookListActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder>{
    private final Context context;
    private RowCategoryBinding binding;
    private ArrayList<Category> categoryArrayList;
    private boolean isAdmin;

    public CategoryAdapter(Context context , ArrayList<Category> categoryArrayList, boolean isAdmin) {
        this.context = context;
        this.categoryArrayList = categoryArrayList;
        this.isAdmin = isAdmin;
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowCategoryBinding.inflate(LayoutInflater.from(context),parent,false);
        return new CategoryHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
        Category model = categoryArrayList.get(position);
        String id = model.getId();
        String category = model.getCategory();
        String uid = model.getUid();
        long timestamp = model.getTimestamp();

        //set data
        holder.categoryTv.setText(category);
        if (isAdmin) {
            holder.deleteBtn.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete").setMessage("Are you sure to delete this category? ")
                        .setPositiveButton("Confirm", (dialog, which) -> {
                            Toast.makeText(context,"Deleting...",Toast.LENGTH_SHORT).show();
                            deleteCategory(model);
                        }).setNegativeButton("Cancel", (dialog, which) -> {
                            dialog.dismiss();
                        }).show();
                Toast.makeText(context," "+category,Toast.LENGTH_SHORT).show();
            });
        } else {
            holder.deleteBtn.setVisibility(View.GONE);
        }
    }

    private void deleteCategory(Category model) {
        String id = model.getId();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");
        ref.child(id)
                .removeValue()
                .addOnSuccessListener(unused -> Toast.makeText(context,"Success",Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(context," "+e.getMessage(),Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }

    class CategoryHolder extends RecyclerView.ViewHolder{
        TextView categoryTv;
        ImageButton deleteBtn;
        CardView cardView;

        public CategoryHolder(@NonNull View itemView) {
            super(itemView);
            cardView = binding.rowCategoryCardView;
            categoryTv = binding.rowCategoryName;
            deleteBtn = binding.rowCategoryDeleteButton;

            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, BookListActivity.class);
                Category category = categoryArrayList.get(getAdapterPosition());
                intent.putExtra("category", category.getCategory());
                if (isAdmin) intent.putExtra("is_admin", true);
                context.startActivity(intent);
            });
        }
    }

}
