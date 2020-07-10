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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;

public class RegisterActivity extends AppCompatActivity {

    EditText user;
    EditText mail;
    EditText pass;
    RequestQueue requestQueue;
    MediaPlayer mp_right, mp_wrong;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

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

        setContentView(R.layout.activity_register);
        Button toSecondScreen = findViewById(R.id.registerButton);
        toSecondScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logScreen();
            }
        });

        Button toLoginScreen = findViewById(R.id.signInButton);
        toLoginScreen.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                toLoginActivity();
            }
        });

    }

    public void logScreen(){
        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.ProgressDialog);
        progressDialog.setMessage("loading");
        progressDialog.show();

        user = findViewById(R.id.userInput);
        String username = user.getText().toString();

        mail = findViewById(R.id.mailInput);
        String email = mail.getText().toString();

        pass = findViewById(R.id.passInput);
        String password = pass.getText().toString();

        String url = "https://botw-cookbook.herokuapp.com/api/register?name="+username+"&email="+email+"&password="+password;
        final Intent toDashboardScreenIntent = new Intent(this, DashboardActivity.class);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            User user = new User();
                            user.setToken(jsonObject.getString("access_token"));
                            String result = user.getToken();
                            toDashboardScreenIntent.putExtra("User", user);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        progressDialog.dismiss();
                        mp_right.start();

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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 3, 1.0f));
        requestQueue.add(stringRequest);
    }

    public void toLoginActivity(){
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
    }
}