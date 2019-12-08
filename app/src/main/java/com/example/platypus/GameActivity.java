package com.example.platypus;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {
    private Timer timer;
    private boolean isRunning = false;
    private Vibrator vibrator;
    public static Planet playerPlanet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // project starts here
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        Button startButton = findViewById(R.id.startButton);
        CustomView customView = findViewById(R.id.customView);

        Planet.planetList.clear();
        new Planet(600, new Vector(-200,200), new Vector(-20, -20));
        new Planet(600, new Vector(200,200), new Vector(-20, 20));
        new Planet(600, new Vector(200,-200), new Vector(20, 20));
        new Planet(600, new Vector(-200,-200), new Vector(20, -20));
        //
        playerPlanet = new Planet(150, new Vector(0,0), new Vector(20, -20), true);


        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRunning == false) {
                    startButton.setText("Stop");
                    isRunning = true;
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            for (Planet p : Planet.planetList) {
                                p.calcToMove(0.004);
                            }
                            for (Planet p : Planet.planetList) {
                                if (!p.update()) {
                                    vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "GameOver", Toast.LENGTH_SHORT).show();
                                            startButton.performClick();
                                        }
                                    });
                                    break;
                                }
                            }
                        }
                    }, 0, 1);
                } else {
                    startButton.setText("Click To Start");
                    isRunning = false;
                    timer.cancel();
                    timer.purge();
                }
            }
        });
    }
}
