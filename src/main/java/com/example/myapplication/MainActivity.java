package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText vibrationTimeEditText;
    private Button startButton;
    private Button stopButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vibrationTimeEditText = findViewById(R.id.vibration_time_edittext);
        startButton = findViewById(R.id.start_button);
        stopButton = findViewById(R.id.stop_button);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVibrationService();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopVibrationService();
            }
        });
    }

    private void startVibrationService() {
        Intent startIntent = new Intent(this, VibrationService.class);
        startIntent.setAction(VibrationService.ACTION_START_VIBRATION);
        long vibrationTime = Long.parseLong(vibrationTimeEditText.getText().toString());
        startIntent.putExtra(VibrationService.EXTRA_VIBRATION_TIME, vibrationTime);
        startService(startIntent);
    }

    private void stopVibrationService() {
        Intent stopIntent = new Intent(this, VibrationService.class);
        stopIntent.setAction(VibrationService.ACTION_STOP_VIBRATION);
        startService(stopIntent);
    }
}
