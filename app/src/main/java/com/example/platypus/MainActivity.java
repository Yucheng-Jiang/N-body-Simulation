package com.example.platypus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Delayed;

public class MainActivity extends AppCompatActivity {
    private Timer timer;
    private boolean isRunning = false;
    private Spinner spinner;
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // project starts here
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //test add planets
        Planet.planetList.clear();
        new Planet(200, new Vector(300,600), new Vector(0, 0));
        new Planet(50, new Vector(400,400), new Vector(2, 2));
        new Planet(100, new Vector(600,600), new Vector(1, 1));
        //
        CustomView customView = findViewById(R.id.customView);
        Button testButton = findViewById(R.id.testButton);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customView.setScale(0.8f);
            }
        });

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                updateSpinner();
            }
        };
        /*
        customView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
            public
        });
        */

        Button startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRunning == false) {
                    startButton.setText("Click To Stop");
                    isRunning = true;
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            for (Planet p : Planet.planetList) {
                                if (!p.update(0.1)) {
                                    handler.post(runnable);
                                    return;
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

        spinner = findViewById(R.id.spinner);
        updateSpinner();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //TextView textViewMass = findViewById(R.id.mass);
                //textViewMass.setText(Planet.planetList.get(position).getMass());
                if (position == Planet.planetList.size()) {
                    new Planet(50, new Vector(0,0), new Vector(0, 0));
                    handler.post(runnable);
                }
                EditText mass = findViewById(R.id.mass);
                mass.setHint("Mass: " + Planet.planetList.get(position).getMass());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void updateSpinner() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < Planet.planetList.size(); i++) {
            list.add("Planet " + String.valueOf(i + 1));
        }
        list.add("Add Planet");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

}
