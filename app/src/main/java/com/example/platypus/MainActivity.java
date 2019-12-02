package com.example.platypus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // project starts here
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Planet.planetList.clear();
        new Planet(200, new Vector(300,600), new Vector(0, 0));
        new Planet(50, new Vector(400,400), new Vector(2, 2));
        new Planet(100, new Vector(600,600), new Vector(1, 1));

        /*
        Button addPlanet = findViewById(R.id.addPlanet);
        addPlanet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
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
                                    return;
                                };
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
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //TextView textViewMass = findViewById(R.id.mass);
                //textViewMass.setText(Planet.planetList.get(position).getMass());
                EditText mass = findViewById(R.id.mass);
                mass.setHint("Mass: " + Planet.planetList.get(position).getMass());

                System.out.println("***** change mass to" + mass.getText());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        updateSpinner();

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
