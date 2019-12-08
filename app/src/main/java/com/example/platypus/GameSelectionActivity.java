package com.example.platypus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class GameSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_selection);
        ImageButton button1 = findViewById(R.id.button1);
        ImageButton button2 = findViewById(R.id.button1);
        ImageButton button3 = findViewById(R.id.button1);
        ImageButton button4 = findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), GameActivity.class);
                startActivity(intent);
            }
        });
        //
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), GameActivity.class);
                startActivity(intent);
            }
        });
        //
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), GameActivity.class);
                startActivity(intent);
            }
        });
        //
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), GameActivity.class);
                startActivity(intent);
            }
        });
    }
}
