package com.example.bookapp.ui.category;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.bookapp.model.Category;
import com.example.bookapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddCategoryActivity extends Activity {

    FirebaseAuth firebaseAuth;
    EditText categories;
    ProgressDialog progressDialog;

    Button submitBtn;
    ImageButton backBtn;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        submitBtn = findViewById(R.id.submitBtn);
        backBtn = findViewById(R.id.login_back_button);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);

        //Back button
        backBtn.setOnClickListener(v -> onBackPressed());

        //submit button
        submitBtn.setOnClickListener(v -> validateData());


    }
    private String category= "";
    private void validateData(){
        categories =findViewById(R.id.categoryEt);
        category = categories.getText().toString().trim();
        if(TextUtils.isEmpty(category)){
            Toast.makeText(this, "Please Enter Category", Toast.LENGTH_SHORT).show();
        }else{
            addCategoryFirebase();
        }
    }

    private void addCategoryFirebase() {
        progressDialog.setMessage("Adding category ...");
        progressDialog.show();

        long timestamp = System.currentTimeMillis();
        String id = String.valueOf(timestamp);

        Category c = new Category();;
        c.setId(id);
        c.setTimestamp(timestamp);
        c.setCategory(category);
        c.setUid(String.valueOf(firebaseAuth.getUid()));

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");
        ref.child(id).setValue(c).addOnSuccessListener(unused -> {
            progressDialog.dismiss();
            Toast.makeText(AddCategoryActivity.this, "Category added successfully", Toast.LENGTH_SHORT).show();
            onBackPressed();
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(AddCategoryActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
