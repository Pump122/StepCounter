package com.example.stepcountingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager manager;
    private float totalSteps = 0f;
    private float previousTotalSteps = 0f;
    public static final String PREFS_NAME = "PREFS_NAME";
    public static final String SAVED_STEPS = "SAVED_STEPS";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadData();
        resetSteps();

        manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = manager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(sensor == null)
            Toast.makeText(this, "No sensor detected on this device", Toast.LENGTH_SHORT).show();
        else {
            manager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);

        }

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
                Toast.makeText(getApplicationContext(), "Steps reset", Toast.LENGTH_SHORT).show();

                return true;
            }
        });
    }


    private void reset(View view){
        TextView steps = findViewById(R.id.tv_stepsTaken);
        Button reset = findViewById(R.id.button);

        previousTotalSteps = totalSteps;
        steps.setText("0");
        saveData();
        Toast.makeText(getApplicationContext(), "Steps reset", Toast.LENGTH_SHORT).show();

    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        TextView tv_stepsTaken = findViewById(R.id.tv_stepsTaken);
        totalSteps = event.values[0];
        // Current steps are calculated by taking the difference of total steps
        // and previous steps
        int currentSteps = (int) totalSteps - (int) previousTotalSteps;

        // It will show the current steps to the user
        tv_stepsTaken.setText(String.valueOf(currentSteps));

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
