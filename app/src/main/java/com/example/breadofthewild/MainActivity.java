package com.example.breadofthewild;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText user;
    EditText pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button toSecondScreen = findViewById(R.id.button);
        toSecondScreen.setOnClickListener(this);
    }

    public void onClick(View v){
        Bundle bundleForSecondScreen = new Bundle();

        String inlog = "Link";
        user = findViewById(R.id.userInput);
        String name = user.getText().toString();

        String passGood = "Ganon?MoreLikeGaynon";
        pass = findViewById(R.id.passInput);
        String password = pass.getText().toString();

        if(inlog.equals(name) && passGood.equals(password)){
            bundleForSecondScreen.putString("name", name);
            Intent toSecondScreenIntent = new Intent(this, SecondActivity.class);
            toSecondScreenIntent.putExtras(bundleForSecondScreen);
            startActivity(toSecondScreenIntent);
            Toast.makeText(this, "Welcome!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Something went wrong...", Toast.LENGTH_SHORT).show();
        }
    }
}