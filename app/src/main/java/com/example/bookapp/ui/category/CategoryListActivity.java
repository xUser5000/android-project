package com.example.bookapp.ui.category;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.bookapp.adapter.CategoryAdapter;
import com.example.bookapp.databinding.ActivityCategoryListBinding;
import com.example.bookapp.model.Category;
import com.example.bookapp.R;
import com.example.bookapp.ui.book.BookListActivity;
import com.example.bookapp.ui.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CategoryListActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    TextView userTypeTextView, emailTextView;
    ImageButton logoutBtn;

    private ActivityCategoryListBinding binding;
    private CategoryAdapter categoryAdapter;

    Button actionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        emailTextView = findViewById(R.id.subtitleTv);
        userTypeTextView = findViewById(R.id.user_type_textview);

        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();

        loadRemoteData();

        //handle the logout process
        logoutBtn = findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(v -> {
            firebaseAuth.signOut();
            checkUser();
        });


        actionButton = findViewById(R.id.category_lis_action_button);
    }

    private void loadRemoteData() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userType = String.valueOf(snapshot.child("userType").getValue());
                boolean isAdmin = userType.equals("admin");

                if (isAdmin) showAdminUI();
                else showUserUI();

                loadCategories(isAdmin);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void showAdminUI() {
        userTypeTextView.setText("Admin");
        actionButton.setText("+ Add Category");
        actionButton.setOnClickListener(v -> startActivity(new Intent(this, AddCategoryActivity.class)));
    }

    private void showUserUI() {
        userTypeTextView.setText("User");
        actionButton.setText("View all books");
        Intent intent = new Intent(this, BookListActivity.class);
        actionButton.setOnClickListener(v -> startActivity(intent));
    }

    private void loadCategories(boolean isAdmin) {
        ArrayList<Category> categoryArrayList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryArrayList.clear();
                for (DataSnapshot ds: snapshot.getChildren()) {
                    Category model = ds.getValue(Category.class);
                    categoryArrayList.add(model);
                }
                categoryAdapter = new CategoryAdapter(CategoryListActivity.this, categoryArrayList, isAdmin);
                binding.categoriesRv.setAdapter(categoryAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkUser() {
        //get the current user
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            String email = firebaseUser.getEmail();
            emailTextView.setText(email);
        }
    }

}
