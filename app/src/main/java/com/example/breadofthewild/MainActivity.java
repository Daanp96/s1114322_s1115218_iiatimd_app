package com.example.breadofthewild;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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