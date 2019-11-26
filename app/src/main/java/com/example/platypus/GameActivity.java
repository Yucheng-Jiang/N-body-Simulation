package com.example.platypus;

import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity  extends AppCompatActivity {
    /** The tag for Log calls - this makes it easier to tell what component messages come from. */
    private static final String TAG = "GameActivity";


    @Override
    @SuppressWarnings("ConstantConditions")
    protected void onCreate(final Bundle savedInstanceState) {

        Log.i(TAG, "Creating");
        // The "super" call is required for all activities
        super.onCreate(savedInstanceState);
        // Create the UI from the activity_game.xml layout file (in src/main/res/layout)
        setContentView(R.layout.activity_game);
    }
}
