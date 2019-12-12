package com.example.platypus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {
    private Timer timer;
    public static boolean isRunning = false;
    private Vibrator vibrator;
    public static Planet playerPlanet;
    private static final double UPDATE_TIME_INTERVAL = 0.002;
    private float gameRunningTime;
    public static final float PLAYER_MOVE_RANGE = 1000;
    private Button startButton;
    private static double cycle = 13850;
    private GameView gameView;
    private int count = 0;
    public static int leftCount = 200;
    public ProgressBar progressBar;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        // project starts here
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        timer = new Timer();
        gameRunningTime = 0;
        isRunning = false;
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        startButton = findViewById(R.id.startButton);
        gameView = findViewById(R.id.gameView);
        TextView timeText = findViewById(R.id.timeText);
        TextView countText = findViewById(R.id.countText);
        progressBar = findViewById(R.id.progressBar);
        leftCount = 200;

        Planet.planetList.clear();
        new Planet(500, new Vector(-400, 400), new Vector(-40, -40));
        new Planet(500, new Vector(400, 400), new Vector(-40, 40));
        new Planet(500, new Vector(400, -400), new Vector(40, 40));
        new Planet(500, new Vector(-400, -400), new Vector(40, -40));
        //
        playerPlanet = new Planet(150, new Vector(0, 0), new Vector(20, -20), true);


        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (isRunning == false) {
                    startButton.setText("Stop");
                    isRunning = true;
                    gameView.invalidate();
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            centerPlayer();
                            gameRunningTime += 0.001;
                            count++;
                            if (count % cycle == 0) {
                                resetPlanet();
                            }
                            for (Planet p : Planet.planetList) {
                                p.calcToMove(UPDATE_TIME_INTERVAL);
                            }
                            for (Planet p : Planet.planetList) {
                                if (!p.update()) {
                                    vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            endGame();
                                        }
                                    });
                                    break;
                                }
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    timeText.setText("Time: " + gameRunningTime);
                                    countText.setText("Left: " + leftCount);
                                    progressBar.setProgress(leftCount);
                                }
                            });
                        }
                    }, 0, 1);
                } else {
                    startButton.setText("Start");
                    isRunning = false;
                    timer.cancel();
                    timer.purge();
                }
            }
        });
    }

    protected void onPause() {
        super.onPause();
        timer.cancel();
        timer.purge();
        finish();
    }

    private void endGame() {
        Intent intent = new Intent(this, GameEndActivity.class);
        intent.putExtra("time", gameRunningTime);
        startActivity(intent);
        finish();
        return;
    }

    private void resetPlanet() {
        for (Planet p : Planet.planetList) {
            System.out.println(p.getPosition().getX() + "position" + p.getPosition().getY());
            System.out.println(p.getSpeed().getX() + "speed" + p.getSpeed().getY());
        }
        Planet.planetList.get(0).setPosition(new Vector(-400, 400));
        Planet.planetList.get(0).setSpeed(new Vector(-40, -40));
        Planet.planetList.get(1).setPosition(new Vector(400, 400));
        Planet.planetList.get(1).setSpeed(new Vector(-40, 40));
        Planet.planetList.get(2).setPosition(new Vector(400, -400));
        Planet.planetList.get(2).setSpeed(new Vector(40, 40));
        Planet.planetList.get(3).setPosition(new Vector(-400, -400));
        Planet.planetList.get(3).setSpeed(new Vector(40, -40));
    }

    private void centerPlayer() {
        float x = gameView.getWidth() / 2 - (float) playerPlanet.getPosition().getX();
        float y = gameView.getHeight() / 2 - (float) playerPlanet.getPosition().getY();
        gameView.setmPosXY(x, y);
    }
}
