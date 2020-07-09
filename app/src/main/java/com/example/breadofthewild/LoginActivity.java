package com.example.breadofthewild;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText mail;
    EditText pass;
    RequestQueue requestQueue;
    MediaPlayer mp_right, mp_wrong;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        mp_right = MediaPlayer.create(this, R.raw.access);
        mp_right.seekTo(0);
        mp_right.setVolume(0.5f, 0.5f);

        mp_wrong = MediaPlayer.create(this, R.raw.error);
        mp_wrong.seekTo(0);
        mp_wrong.setVolume(0.5f, 0.5f);

        CookieManager manager = new CookieManager();
        CookieHandler.setDefault(manager);
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.start();

        setContentView(R.layout.activity_login);
        Button toSecondScreen = findViewById(R.id.loginButton);
        toSecondScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logScreen();
            }
        });

        Button toLoginScreen = findViewById(R.id.newUserButton);
        toLoginScreen.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                toRegisterActivity();
            }
        });;
    }

    public void logScreen(){

        mail = findViewById(R.id.mailInput);
        String email = mail.getText().toString();

        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.ProgressDialog);
        progressDialog.setMessage("loading");
        progressDialog.show();

        pass = findViewById(R.id.passInput);
        String password = pass.getText().toString();
        String url = "https://botw-cookbook.herokuapp.com/api/login?email="+email+"&password="+password;
        final Intent toDashboardScreenIntent = new Intent(this, DashboardActivity.class);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        mp_right.start();
//                        getJsonData();
//                        Log.d("RESPONSE", response);
                        startActivity(toDashboardScreenIntent);

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        mp_wrong.start();
                    }
                });
        requestQueue.add(stringRequest);
    }

    public void toRegisterActivity(){
        Intent registerIntent = new Intent(this, RegisterActivity.class);
        startActivity(registerIntent);
    }

}
