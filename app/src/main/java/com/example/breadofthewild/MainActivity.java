package com.example.breadofthewild;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button toLoginScreen = findViewById(R.id.loginButton);
        mp = MediaPlayer.create(this, R.raw.start);
        mp.seekTo(0);
        mp.setVolume(0.5f, 0.5f);
        toLoginScreen.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                toLoginActivity();
            }
        });
    }

    public void toLoginActivity(){
        mp.start();
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
    }
}