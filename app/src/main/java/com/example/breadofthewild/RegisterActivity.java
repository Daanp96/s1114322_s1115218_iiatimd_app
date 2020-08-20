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

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity  {

    private final String URL = "http://10.0.2.2:8000/api/register";

    EditText user, mail, pass;
    private MediaPlayer mp_right, mp_wrong;

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

        setContentView(R.layout.activity_register);
        Button toSecondScreen = findViewById(R.id.registerButton);
        toSecondScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerScreen();
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

    public void registerScreen(){
        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.ProgressDialog);
        progressDialog.setMessage("loading");
        progressDialog.show();

        user = (EditText) findViewById(R.id.userInput);
        mail = (EditText) findViewById(R.id.mailInput);
        pass = (EditText) findViewById(R.id.passInput);

        final String username = user.getText().toString();
        final String email = mail.getText().toString();
        final String password = pass.getText().toString();

        final Intent toDashboardScreenIntent = new Intent(this, DashboardActivity.class);

        JSONObject newUser = registerData(username, email, password);
        if(newUser != null) {
            JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, URL, newUser, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String jwt = response.getString("access_token");
                        setUserData(username, email, password);
                        assignJWT(jwt);
                        logIn();
                        storeUser(new User(username, email, password));
                        Log.d("jwt", jwt);

                        toDashboardScreenIntent.putExtra("User", username);


                    } catch (JSONException e) {
                        Log.d("onReponse: ", String.valueOf(e));
                    }
                    progressDialog.dismiss();
                    mp_right.start();
                    startActivity(toDashboardScreenIntent);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("error", String.valueOf(error));
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Toast.makeText(RegisterActivity.this, "No connection...", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof AuthFailureError) {
                        Toast.makeText(RegisterActivity.this, "Couldn't resolve login", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof NetworkError){
                        Toast.makeText(RegisterActivity.this, "No network was found...", Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                    mp_wrong.start();
                }
            });

            VolleySingleton.getInstance(this).addToRequestQueue(jor);

        }
    }

    public void toLoginActivity(){
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
    }

    //GET DATA FROM THE REGISTER FORM
    private JSONObject registerData(String username, String email, String password) {
        JSONObject user = new JSONObject();
        try {
            user.put("name", username);
            user.put("email", email);
            user.put("password", password);
        } catch (Exception e) {
            return null;
        }
        return user;
    }

    private void setUserData(String username, String email, String password) {
        UserAuth.getInstance(this).setUsername(username);
        UserAuth.getInstance(this).setEmail(email);
        UserAuth.getInstance(this).setPassword(password);
    }

    //STORE USER IN SHARED PREFERENCES (for keeping them logged in)
    private void storeUser(User user) {
        UserAuth.getInstance(this).storeInSp(user);
    }

    //RETRIEVE USER FROM SHARED PREFERENCES (to stay logged in)
    private User retrieveUser(String key) {
        return UserAuth.getInstance(this).retrieveFromSp(key);
    }

    //GIVE USER A JWT TOKEN
    private void assignJWT(String jwt) {
        UserAuth.getInstance(this).setJWT(jwt);
    }

    private void logIn() {
        UserAuth.getInstance(this).setAuth(true);
    }


}
