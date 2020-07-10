package com.example.breadofthewild;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    MediaPlayer mp;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button toLoginScreen = findViewById(R.id.loginButton);
        mp = MediaPlayer.create(this, R.raw.start);
        mp.seekTo(0);
        mp.setVolume(0.5f, 0.5f);
        final Animation flash = AnimationUtils.loadAnimation(this, R.anim.flash);
        toLoginScreen.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                mp.start();
                ImageView image = findViewById((R.id.imageView));
                image.startAnimation(flash);
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toLoginActivity();
                    }
                }, 1100);
            }
        });
    }

    public void toLoginActivity(){

        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
    }
}