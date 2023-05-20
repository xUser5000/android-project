package com.example.bookapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.bookapp.R;
import com.example.bookapp.ui.auth.LoginActivity;
import com.example.bookapp.ui.auth.RegisterActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         Button loginBtn = findViewById(R.id.loginBtn);
         loginBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this , LoginActivity.class)));

        Button registerButton = findViewById(R.id.skipBtn);
        registerButton.setOnClickListener(v ->
            startActivity(new Intent(MainActivity.this , RegisterActivity.class))
        );
    }
}
