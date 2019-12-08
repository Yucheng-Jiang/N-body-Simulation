package com.example.platypus;

import androidx.appcompat.app.AppCompatActivity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;

import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;




public class LabActivity extends AppCompatActivity {
    private Timer timer;
    private boolean isRunning = false;
    private Spinner spinner;
    private Handler handler = new Handler();
    //private SeekBar massSeekBar = findViewById(R.id.MassSeekBar);
    private TextView massText;
    private TextView speedText;
    private TextView positionText;
    private Planet currentPlanet = null;
    private ImageButton delete;
    private ImageButton edit;
    private Vibrator vibrator;
    private boolean currentStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // project starts here
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab);

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        massText = findViewById(R.id.MassText);
        speedText = findViewById(R.id.SpeedText);
        positionText = findViewById(R.id.PositionText);
        delete = findViewById(R.id.Delete);
        edit = findViewById(R.id.Edit);
        ImageButton startButton = findViewById(R.id.startButton);
        CustomView customView = findViewById(R.id.customView);

        Planet.planetList.clear();
        /*
        new Planet(600, new Vector(-200,200), new Vector(-20, -20));
        new Planet(600, new Vector(200,200), new Vector(-20, 20));
        new Planet(600, new Vector(200,-200), new Vector(20, 20));
        new Planet(600, new Vector(-200,-200), new Vector(20, -20));

         */
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            new Planet(random.nextInt(1000), new Vector(random.nextInt(1000), random.nextInt(1000)), new Vector(random.nextInt(5), random.nextInt(5)));
        }
        //

        delete.setOnClickListener(unused -> {
            boolean currentStop = false;
            if (isRunning) {
                startButton.performClick();
            } else {
                currentStop = true;
            }

            Planet.planetList.remove(currentPlanet);
            if (Planet.planetList.size() == 0) {
                new Planet(50, new Vector(0, 0), new Vector(0,0));
                Toast.makeText(getApplicationContext(), "Default Planet Created", Toast.LENGTH_SHORT).show();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateSpinner();
                }
            });
            updateData(currentPlanet);

            if (!currentStop) {
                startButton.performClick();
            }

        });

        edit.setOnClickListener(unused -> {
            editInfo(false);
        });


        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRunning == false) {
                    startButton.setImageResource(android.R.drawable.ic_media_pause);
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
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            updateSpinner();
                                        }
                                    });
                                    vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                                    if (Planet.planetList.size() == 0) {
                                        new Planet(50, new Vector(0, 0), new Vector(0, 0));
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "Default Planet Created", Toast.LENGTH_SHORT).show();
                                                startButton.performClick();
                                            }
                                        });
                                        break;
                                    }
                                    return;
                                }
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateData(currentPlanet);
                                }
                            });
                        }
                    }, 0, 1);
                } else {
                    startButton.setImageResource(android.R.drawable.ic_media_play);
                    isRunning = false;
                    timer.cancel();
                    timer.purge();
                }
            }
        });

        spinner = findViewById(R.id.spinner);
        spinner.setTooltipText("");
        updateSpinner();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Add Planet")) {
                    editInfo(true);
                } else {
                    currentPlanet = Planet.planetList.get(position);
                    float x = customView.getWidth() / 2 - (float) currentPlanet.getPosition().getX();
                    float y = customView.getHeight() / 2 - (float) currentPlanet.getPosition().getY();
                    customView.setmPosXY(x, y);
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
        } else {
            massText.setText("Mass: " + round(planet.getMass()));
            positionText.setText("Position: x = " + round(planet.getPosition().getX())
                    + " y = " + round(planet.getPosition().getY()));
            speedText.setText("Velocity: x = " + round(planet.getSpeed().getX())
                    + " y = " + round(planet.getSpeed().getY()));
        }
    }
    public static String round(double value) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return decimalFormat.format(value);
    }
    public void editInfo(boolean isNew) {
        ImageButton startButton = findViewById(R.id.startButton);

        currentStop = false;

        if (isRunning) {
            startButton.performClick();
        } else {
            currentStop = true;
        }


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setCancelable(false);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.chunk_setinfo, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setTitle("PLANET BUILDER");
        //dialogBuilder.setMessage("Enter data below");

        final EditText setMass = (EditText) dialogView.findViewById(R.id.SetMass);
        final EditText setSpeedValue = (EditText) dialogView.findViewById(R.id.SetSpeedValue);
        final EditText setSpeedDirection = (EditText) dialogView.findViewById(R.id.SetSpeedDirection);
        final EditText setX = (EditText) dialogView.findViewById(R.id.SetX);
        final EditText setY = (EditText) dialogView.findViewById(R.id.SetY);

        if (isNew) {
            setMass.setHint("Enter mass (Default 100)");
            setSpeedValue.setHint("Set speed value (Default 1)");
            setSpeedDirection.setHint("Set direction in degrees (Default 0)");
            setX.setHint("Set X coordinate (Default 200)");
            setY.setHint("Set Y coordinate (Default 200");
        } else {
            setMass.setHint("Current mass: " + currentPlanet.getMass());
            setSpeedValue.setHint("Current speed: " + round(currentPlanet.getSpeed().getModulus()));
            setSpeedDirection.setHint("Current moving direction: " + round(currentPlanet.getSpeed().getAngle()));
            setX.setHint("Current x-coordinate: " + round(currentPlanet.getPosition().getX()));
            setY.setHint("Current x-coordinate: " + round(currentPlanet.getPosition().getY()));
        }


        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //do something with edt.getText().toString();
                String mass = setMass.getText().toString().trim();
                String directionStr = setSpeedDirection.getText().toString().trim();
                String xStr = setX.getText().toString().trim();
                String yStr = setY.getText().toString().trim();
                String speedStr = setSpeedValue.getText().toString().trim();
                //

                if (isNew) {
                    if (mass.length() == 0) {
                        mass = "100";
                    }
                    if (speedStr.length() == 0) {
                        speedStr = "1";
                    }
                    if (directionStr.length() == 0) {
                        directionStr = "0";
                    }
                    if (xStr.length() == 0) {
                        xStr = "200";
                    }
                    if (yStr.length() == 0) {
                        yStr = "200";
                    }
                    double direction = (Integer.parseInt(directionStr) % 360) / (2 * Math.PI);
                    Vector position = new Vector(Integer.parseInt(xStr), Integer.parseInt(yStr));
                    int m = Integer.parseInt(mass);

                    for (Planet p : Planet.planetList) {
                        if (position.distance(p.getPosition()) <= Math.pow(m, 0.5) + Math.pow(p.getMass(), 0.5)) {
                            Toast.makeText(getApplicationContext(), "Current position has planet", Toast.LENGTH_SHORT).show();
                            if (!currentStop) {
                                startButton.performClick();
                            }
                            return;
                        }
                    }

                    double speed = Integer.parseInt(speedStr);
                    new Planet(m, position, new Vector(speed * Math.cos(direction), speed * Math.sin(direction)));
                } else {
                    if (mass.length() != 0) {
                        currentPlanet.setMass(Integer.parseInt(mass));
                    }
                    if (xStr.length() != 0) {
                        currentPlanet.setPosition( new Vector(
                                Integer.parseInt(xStr),
                                currentPlanet.getPosition().getY()
                        ));
                    }
                    if (yStr.length() != 0) {
                        currentPlanet.setPosition( new Vector(
                                currentPlanet.getPosition().getX(),
                                Integer.parseInt(yStr)
                        ));
                    }
                    if (directionStr.length() != 0 && speedStr.length() != 0) {
                        double direction = (Integer.parseInt(directionStr) % 360) / (2 * Math.PI);
                        double speed = Integer.parseInt(speedStr);
                        currentPlanet.setSpeed(new Vector(
                                speed * Math.cos(direction),
                                speed * Math.sin(direction)));
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateSpinner();
                    }
                });

                if (!currentStop) {
                    startButton.performClick();
                }
            }

        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (!currentStop) {
                    startButton.performClick();
                }
            }
        });
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

    }
}
