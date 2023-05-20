package com.example.bookapp.ui.auth;

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    Button registerBtn;
    ImageButton backBtn;
    EditText nameEt;

    EditText emailEt;
    EditText cPasswordEt;

    TextView haveAccountLogin;

    //Firebase DB Auth
    private FirebaseAuth firebaseAuth;
    //progress Dialog
    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        nameEt = findViewById(R.id.nameEt);
        emailEt = findViewById(R.id.emailEt);
        cPasswordEt = findViewById(R.id.cPasswordEt);

        //initializing the firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        //setup the progress Dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait :)");
        progressDialog.setCanceledOnTouchOutside(false);


        //handle the go_back button click
        backBtn = findViewById(R.id.login_back_button);
        backBtn.setOnClickListener(v -> onBackPressed());

        //handle the Register Button click
        registerBtn = findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(v -> validateData());

        haveAccountLogin = findViewById(R.id.have_account_login);
        haveAccountLogin.setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));
    }


    private  String name = "" , email = "", password = "";
    private void validateData() {
        // Data Validation Before Create the Account
        name = nameEt.getText().toString().trim();
        email= emailEt.getText().toString().trim();
        password = cPasswordEt.getText().toString().trim();
        String cPassword = cPasswordEt.getText().toString().trim();
        if (TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please, Enter your name!", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid E-Mail Address Pattern", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(cPassword)){
            Toast.makeText(this, "Confirm your Password, Please!", Toast.LENGTH_SHORT).show();
        }else if (!password.equals(cPassword)){
            Toast.makeText(this, "Wrong Password", Toast.LENGTH_SHORT).show();

        }else{
            createUserAccount();
        }
    }

    private void createUserAccount() {

        // Display the Progress Dialog
        progressDialog.setMessage("Wait, Creating your Account");
        progressDialog.show();

        //create user Account and store it in firebase
        firebaseAuth.createUserWithEmailAndPassword(email , password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
               // In case of the Account creating success and storing the account in firebase realtime DB
                updateUserInfo();
                //progressDialog.dismiss();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                 // in case of Account creating fail
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void updateUserInfo() {
        progressDialog.setMessage("Saving your Information");
        //time stamp
        long timestamp = System.currentTimeMillis();
        //get the current user uid ASA the user registered
        String uid = firebaseAuth.getUid();
        // setup the data to add it in firebase DB
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uid", uid);
        hashMap.put("email" , email);
        hashMap.put("name" , name);
        hashMap.put("userType" , "user");
        hashMap.put("timestamp" , timestamp);
        //send the data to DB

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(uid).setValue(hashMap).addOnSuccessListener(unused -> {
              //Data is added to DB
            progressDialog.dismiss();
            Toast.makeText(RegisterActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(RegisterActivity.this , CategoryListActivity.class));
            finish();
        }).addOnFailureListener(e -> {
            e.printStackTrace();
            Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });

    }
}