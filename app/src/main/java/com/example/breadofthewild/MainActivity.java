package com.example.breadofthewild;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button toLoginScreen = findViewById(R.id.loginButton);
        toLoginScreen.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                toLoginActivity();
            }
        });
    }

    public void toLoginActivity(){
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
    }
}