package com.example.notesapp;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            if (isUserLoggedIn()) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new NotesFragment())
                        .commit();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new LoginFragment())
                        .commit();
            }
        }
    }

    private boolean isUserLoggedIn() {
        SharedPreferences sharedPreferences = getSharedPreferences("NotesApp", MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("user_email", null);
        return userEmail != null;
    }
}


