package com.example.breadofthewild;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstances){
        super.onCreate(savedInstances);
        setContentView(R.layout.activity_second);
        Bundle myBundle = getIntent().getExtras();
        Log.d("testen", myBundle.toString());
    }
}
