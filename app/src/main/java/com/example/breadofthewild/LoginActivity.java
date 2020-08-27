package com.example.breadofthewild;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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


public class LoginActivity extends AppCompatActivity {
    EditText mail, pass;
    MediaPlayer mp_right, mp_wrong;
    private String URL = "http://10.0.2.2:8000/api/login";
    private String jwt = null;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        UserAuth.getInstance(this).setPreferences(sp);

        mp_right = MediaPlayer.create(this, R.raw.access);
        mp_right.seekTo(0);
        mp_right.setVolume(0.5f, 0.5f);

        mp_wrong = MediaPlayer.create(this, R.raw.error);
        mp_wrong.seekTo(0);
        mp_wrong.setVolume(0.5f, 0.5f);


        // USER IS NOT LOGGED IN
        if(!UserAuth.getInstance(this).isAuth()){

        // USER IS LOGGED IN
        } else {
            Intent toDashboardScreenIntent = new Intent(this, DashboardActivity.class);
//            String username = UserAuth.getInstance(this).retrieveFromSp("aboutUser").getUsername();
//            toDashboardScreenIntent.putExtra("User", username);
            mp_right.start();
            startActivity(toDashboardScreenIntent);

        }
        setContentView(R.layout.activity_login);



        Button toSecondScreen = findViewById(R.id.loginButton);
        toSecondScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logScreen();
            }
        });

        Button toLoginScreen = findViewById(R.id.newUserButton);
        toLoginScreen.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                toRegisterActivity();
            }
        });
        ;
    }

    public void logScreen() {
        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.ProgressDialog);
        progressDialog.setMessage("loading");
        progressDialog.show();

        mail = findViewById(R.id.mailInput);
        final String email = mail.getText().toString();

        pass = findViewById(R.id.passInput);
        final String password = pass.getText().toString();

        String url = "http://10.0.2.2:8000/api/login?email=" + email + "&password=" + password;


        JSONObject user = getLoginData(email, password);
        final Context context = this;

        if(user != null) {
            JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, URL, user, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("response", response.toString());
                    try {
                        jwt = response.getString("access_token");

                    } catch (JSONException e) {

                    }
                    AuthenticateUser(email, password, jwt, true);
                    Intent toDashboardScreenIntent = new Intent(context, DashboardActivity.class);
                    startActivity(toDashboardScreenIntent);
                    progressDialog.dismiss();
                    mp_right.start();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("error", String.valueOf(error));
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Toast.makeText(LoginActivity.this, "No connection...", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof AuthFailureError) {
                        Toast.makeText(LoginActivity.this, "Couldn't resolve login", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof NetworkError){
                        Toast.makeText(LoginActivity.this, "No network was found...", Toast.LENGTH_SHORT).show();
                    }

                    progressDialog.dismiss();
                    mp_right.start();
                }
            });
            jor.setRetryPolicy(new DefaultRetryPolicy(10000, 3, 1.0f));
            VolleySingleton.getInstance(this).addToRequestQueue(jor);
        }
    }

    public void toRegisterActivity() {
        Intent registerIntent = new Intent(this, RegisterActivity.class);
        startActivity(registerIntent);
    }

    private JSONObject getLoginData(String email, String password){
        JSONObject user = new JSONObject();
        try{
            user.put("email", email);
            user.put("password", password);
        }
        catch(Exception e){}
        return user;
    }

    private void AuthenticateUser(String email, final String password, String jwt, boolean isAuth) {
        UserAuth.getInstance(this).setEmail(email);
        UserAuth.getInstance(this).setPassword(password);
        UserAuth.getInstance(this).setJWT(jwt);
        UserAuth.getInstance(this).setAuth(isAuth);
    }


    private String getJwt(){
        return UserAuth.getInstance(this).getJwt();
    }

}