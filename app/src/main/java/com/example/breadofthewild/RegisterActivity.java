package com.example.breadofthewild;

import android.content.Intent;
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

public class RegisterActivity extends AppCompatActivity {

    EditText user;
    EditText mail;
    EditText pass;
    RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
//                        getJsonData();
                        Log.d("RESPONSE", response);


                        startActivity(toDashboardScreenIntent);

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                });
        requestQueue.add(stringRequest);
    }

    public void toLoginActivity(){
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
    }

}
