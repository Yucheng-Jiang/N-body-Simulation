package com.example.platypus;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.os.Handler;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.Spinner;
import android.widget.TextView;


import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;




public class MainActivity extends AppCompatActivity {
    private Timer timer;
    private boolean isRunning = false;
    private Spinner spinner;
    private Handler handler = new Handler();
    //private SeekBar massSeekBar = findViewById(R.id.MassSeekBar);
    private TextView massText;
    private TextView speedText;
    private TextView positionText;
    private Planet currentPlanet = null;
    private Button delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // project starts here
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //test add planets
        massText = findViewById(R.id.MassText);
        speedText = findViewById(R.id.SpeedText);
        positionText = findViewById(R.id.PositionText);
        delete = findViewById(R.id.Delete);

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
                customView.setPosition(100, 100);
            }
        });

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                updateSpinner();
            }
        };

        Runnable runnable1 = new Runnable() {
            @Override
            public void run() {
                if (currentPlanet == null) {
                    delete.setVisibility(View.GONE);
                    return;
                } else {
                    delete.setVisibility(View.VISIBLE);
                    delete.setOnClickListener(unused -> {
                        Planet.planetList.remove(currentPlanet);
                        handler.post(runnable);

                        if (Planet.planetList.size() == 0) {
                            delete.setVisibility(View.GONE);
                            currentPlanet = null;
                        }
                        updateData(currentPlanet);
                    });
                }
            }
        };

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
                            handler.post(runnable1);
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
                    //new Planet(50, new Vector(0,0), new Vector(0, 0));
                    handler.post(runnable);
                } else {
                    currentPlanet = Planet.planetList.get(position);
                }

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
    public void updateData(Planet planet) {
        if (planet == null) {
            massText.setText("Mass");
            positionText.setText("Position");
            speedText.setText("Velocity");
            return;
        }
        massText.setText("Mass: " + round(planet.getMass()));
        positionText.setText("Position: x = " + round(planet.getPosition().getX())
                + " y = " + round(planet.getPosition().getY()));
        speedText.setText("Velocity: x = " + round(planet.getSpeed().getX())
                + " y = " + round(planet.getPosition().getY()));
    }
    public static String round(double value) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return decimalFormat.format(value);
    }

}
