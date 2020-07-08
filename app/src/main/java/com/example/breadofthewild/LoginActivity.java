package com.example.breadofthewild;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText user;
    EditText pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button toSecondScreen = findViewById(R.id.loginButton);
        toSecondScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logScreen();
            }
        });
    }

    public void logScreen(){
        Bundle bundleForSecondScreen = new Bundle();

        String inlog = "Link";
        user = findViewById(R.id.userInput);
        String name = user.getText().toString();

        String passGood = "Ganon?MoreLikeGaynon";
        pass = findViewById(R.id.passInput);
        String password = pass.getText().toString();

        if(inlog.equals(name) && passGood.equals(password)){
            bundleForSecondScreen.putString("name", name);
            Intent toDashboardScreenIntent = new Intent(this, DashboardActivity.class);
            toDashboardScreenIntent.putExtras(bundleForSecondScreen);
            startActivity(toDashboardScreenIntent);
            Toast.makeText(this, "Welcome!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Something went wrong...", Toast.LENGTH_SHORT).show();
        }
    }
}
