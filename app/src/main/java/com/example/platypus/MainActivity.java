package com.example.platypus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Delayed;

public class MainActivity extends AppCompatActivity {
    private Timer timer;
    private boolean isRunning = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // project starts here
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button addPlanet = findViewById(R.id.addPlanet);
        addPlanet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Planet.planetList.add(new Planet(100, new Vector(300,300), new Vector(1, 0)));
                Planet.planetList.add(new Planet(100, new Vector(600,600), new Vector(1, 0)));
            }
        });
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
                                System.out.println(p.getPosition().getX());
                                System.out.println(p.getPosition().getY());
                                p.update(1);
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
