package com.example.platypus;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startGameButton = findViewById(R.id.startGameButton);
        Button startLabButton = findViewById(R.id.startLabButton);
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), GameActivity.class);
                startActivity(intent);
            }
        });
        startLabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), LabActivity.class);
                startActivity(intent);
            }
        });
        Button tutorial = findViewById(R.id.Tutorial);
        MediaPlayer mediaPlayer= MediaPlayer.create(MainActivity.this, R.raw.intro);
        tutorial.setOnClickListener(unused-> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            } else {
                mediaPlayer.start();
            }
            List<String> list = new ArrayList<>();
            list.add("There'a  planet in Galaxy X, whose climate randomly flips between Orderly and Chaotic Eras.");
            list.add(" During Chaotic Eras, the weather oscillates unpredictably between extreme cold and extreme heat, sometimes within minutes.\n The inhabitants seek ways to predict Chaotic Eras so they can better survive.");
            list.add("Even worse, supercomputer predicts the planet will enter a more unstable status in the next few years. \n It could fly towards one of the stars in the galaxy and cause genocide.");
            list.add("You are an admirable astrophysicist trying to solve this dilemma. An experimental laboratory is provided for you to run simulations.");
            list.add("Your final goal is to control your planet's orbit to survive in the real universe.");
            list.add("Good luck scientist.");

            /*
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setCancelable(false);
            dialogBuilder.setTitle("Tutorial");
            dialogBuilder.setMessage(list.get(0));
            dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            });

            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();
             */
        });
    }
}