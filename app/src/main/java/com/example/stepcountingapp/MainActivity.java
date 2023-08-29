package com.example.stepcountingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private SensorManager manager;
    private float totalSteps = 0f;
    private float previousTotalSteps = 0f;
    public static final String PREFS_NAME = "PREFS_NAME";
    public static final String SAVED_STEPS = "SAVED_STEPS";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void saveData() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat(SAVED_STEPS, previousTotalSteps);
        editor.apply();
    }
    private void loadData(){
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        float savedSteps = preferences.getFloat(SAVED_STEPS, 0f);
        previousTotalSteps = savedSteps;
    }

    private void resetSteps () {
        TextView steps = findViewById(R.id.tv_stepsTaken);
        steps.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Long press to reset steps", Toast.LENGTH_SHORT).show();
            }
        }));


        steps.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                previousTotalSteps = totalSteps;
                steps.setText("0");
                saveData();
                return true;
            }
        });
    }
}