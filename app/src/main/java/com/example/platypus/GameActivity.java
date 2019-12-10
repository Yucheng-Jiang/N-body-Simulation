package com.example.platypus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {
    private Timer timer;
    public static boolean isRunning = false;
    private Vibrator vibrator;
    public static Planet playerPlanet;
    private static final double UPDATE_TIME_INTERVAL = 0.004;
    private float gameRunningTime;
    public static  final float PLAYER_MOVE_RANGE = 900;
    private Button startButton;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        // project starts here
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gameRunningTime = 0;
        isRunning = false;
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        startButton = findViewById(R.id.startButton);
        GameView gameView = findViewById(R.id.gameView);
        TextView timeText = findViewById(R.id.timeText);

        Planet.planetList.clear();
        new Planet(600, new Vector(-200, 200), new Vector(-20, -20));
        new Planet(600, new Vector(200, 200), new Vector(-20, 20));
        new Planet(600, new Vector(200, -200), new Vector(20, 20));
        new Planet(600, new Vector(-200, -200), new Vector(20, -20));
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
                            gameRunningTime += 0.001;
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
        return;
    }

    private void endGame() {
        Intent intent = new Intent(this, GameEndActivity.class);
        intent.putExtra("time", gameRunningTime);
        startActivity(intent);
        finish();
        return;
    }
}
