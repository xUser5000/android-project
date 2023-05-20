package com.example.bookapp.ui.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookapp.R;
import com.example.bookapp.ui.category.CategoryListActivity;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    TextView noAccountTv;
    Button loginBtn;
    ImageButton backButton;
    EditText emailEt;
    EditText passwordEt;


    //firebase auth
    private FirebaseAuth firebaseAuth;
    //the progress Dialog
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //view the contents of the form using the id
        emailEt = findViewById(R.id.emailEt);
        passwordEt = findViewById(R.id.passwordEt);

        firebaseAuth = firebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait :)");
        progressDialog.setCanceledOnTouchOutside(false);

        noAccountTv = findViewById(R.id.noAccountTv);
        noAccountTv.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));

        loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(v -> validateData());

        backButton = findViewById(R.id.login_back_button);
        backButton.setOnClickListener(v -> onBackPressed());
    }

    private String email = "", password = "";

    private void validateData() {
        // After Creating an account and before login , we will check the data
        email = emailEt.getText().toString().trim();
        password = passwordEt.getText().toString().trim();
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid Email Pattern!", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please, Enter your Password", Toast.LENGTH_SHORT).show();
        } else {
            //the data is validated
            loginUser();
        }


    }

    private void loginUser() {
        progressDialog.setMessage("Logging In");
        progressDialog.show();

        //login user
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
            progressDialog.dismiss();
            startActivity(new Intent(LoginActivity.this, CategoryListActivity.class));
            finish();
        }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Toast.makeText(LoginActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void checkUser() {

        /*
        //checking if the person is user or admin
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        //checking the DB
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressDialog.dismiss();
                //get User Type
                String userType = "" + snapshot.child("userType").getValue();
                System.out.println(userType);
                //check the user Type
                if (userType.equals("user")) {
                    // open the users Dashboard
                    startActivity(new Intent(LoginActivity.this, UserDashboardActivity.class));
                    finish();
                } else if (userType.equals("admin")) {
                    //open the admins dashboard
                    startActivity(new Intent(LoginActivity.this, CategoryListActivity.class));
                    finish();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
    }
}