package com.example.platypus;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;

import android.view.LayoutInflater;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;




public class LabActivity extends AppCompatActivity {
    public static boolean isRunning = false;

    private Timer timer;
    private Spinner spinner;
    private TextView massText;
    private TextView speedText;
    private TextView positionText;
    private Planet selectedPlanet = null;
    private ImageButton deleteButton;
    private ImageButton editButton;
    private Vibrator vibrator;
    private static final double UPDATE_TIME_INTERVAL = 0.004;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // project starts here
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab);

        timer = new Timer();
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        massText = findViewById(R.id.MassText);
        speedText = findViewById(R.id.SpeedText);
        positionText = findViewById(R.id.PositionText);
        deleteButton = findViewById(R.id.Delete);
        editButton = findViewById(R.id.Edit);

        ImageButton startButton = findViewById(R.id.startButton);
        CustomView customView = findViewById(R.id.customView);

        Planet.planetList.clear();

        new Planet(600, new Vector(-400,400), new Vector(-40, -40));
        new Planet(600, new Vector(400, 400), new Vector(-40, 40));
        new Planet(600, new Vector(400, -400), new Vector(40, 40));
        new Planet(600, new Vector(-400,-400), new Vector(40, -40));

        /*
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            new Planet(random.nextInt(1000), new Vector(random.nextInt(1000), random.nextInt(1000)), new Vector(random.nextInt(5), random.nextInt(5)));
        }
        */

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRunning) {
                    startButton.performClick();
                }
                Planet.planetList.remove(selectedPlanet);
                if (Planet.planetList.size() == 0) {
                    new Planet(50, new Vector(0, 0), new Vector(0, 0));
                    Toast.makeText(getApplicationContext(), "Default Planet Created", Toast.LENGTH_SHORT).show();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateSpinner();
                    }
                });
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editInfo(false);
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRunning) {
                    startButton.setImageResource(android.R.drawable.ic_media_pause);
                    isRunning = true;
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            for (Planet p : Planet.planetList) {
                                p.calcToMove(UPDATE_TIME_INTERVAL);
                            }
                            for (Planet p : Planet.planetList) {
                                if (!p.update()) {
                                    vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            updateSpinner();
                                        }
                                    });
                                    if (Planet.planetList.size() == 0) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "Default Planet Created", Toast.LENGTH_SHORT).show();
                                                startButton.performClick();
                                            }
                                        });
                                        new Planet(50, new Vector(0, 0), new Vector(0, 0));
                                        break;
                                    }
                                    return;
                                }
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateSelectedPlanetData(selectedPlanet);
                                }
                            });
                        }
                    }, 0, 1);
                } else {
                    startButton.setImageResource(android.R.drawable.ic_media_play);
                    isRunning = false;
                    timer.purge();
                    timer.cancel();
                }
            }
        });

        spinner = findViewById(R.id.spinner);
        spinner.setTooltipText("");
        updateSpinner();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
                if (parent.getItemAtPosition(position).equals("Add Planet")) {
                    editInfo(true);
                } else {
                    selectedPlanet = Planet.planetList.get(position);
                    updateSelectedPlanetData(selectedPlanet);
                    float x = customView.getWidth() / 2 - (float) selectedPlanet.getPosition().getX();
                    float y = customView.getHeight() / 2 - (float) selectedPlanet.getPosition().getY();
                    customView.setmPosXY(x, y);
                }
            }

            @Override
            public void onNothingSelected(final AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isRunning) {
            findViewById(R.id.startButton).performClick();
        }
        finish();
    }

    private void updateSpinner() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < Planet.planetList.size(); i++) {
            list.add("Planet " + (i + 1));
        }

        list.add("Add Planet");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    private void updateSelectedPlanetData(Planet planet) {
        if (planet == null) {
            massText.setText("Mass");
            positionText.setText("Position");
            speedText.setText("Velocity");
            return;
        } else {
            massText.setText("Mass: " + round(planet.getMass()));
            positionText.setText(
                    "Position: x = " + round(planet.getPosition().getX())
                            + " y = " + round(planet.getPosition().getY())
            );
            speedText.setText(
                    "Velocity: x = " + round(planet.getSpeed().getX())
                    + " y = " + round(planet.getSpeed().getY())
            );
        }
    }

    private static String round(double value) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return decimalFormat.format(value);
    }

    private void editInfo(boolean isEditingNewPlanet) {
        ImageButton startButton = findViewById(R.id.startButton);

        if (isRunning) {
            startButton.performClick();
        }

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setCancelable(false);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.chunk_setinfo, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setTitle("PLANET BUILDER");

        final EditText setMass = (EditText) dialogView.findViewById(R.id.SetMass);
        final EditText setSpeedValue = (EditText) dialogView.findViewById(R.id.SetSpeedValue);
        final EditText setSpeedDirection = (EditText) dialogView.findViewById(R.id.SetSpeedDirection);
        final EditText setX = (EditText) dialogView.findViewById(R.id.SetX);
        final EditText setY = (EditText) dialogView.findViewById(R.id.SetY);

        if (isEditingNewPlanet) {
            setMass.setHint("Enter mass (Default 100)");
            setSpeedValue.setHint("Set speed value (Default 1)");
            setSpeedDirection.setHint("Set direction in degrees (Default 0)");
            setX.setHint("Set X coordinate (Default 200)");
            setY.setHint("Set Y coordinate (Default 200");
        } else {
            setMass.setHint("Current mass: " + selectedPlanet.getMass());
            setSpeedValue.setHint("Current speed: " + round(selectedPlanet.getSpeed().getModulus()));
            setSpeedDirection.setHint("Current moving direction: " + round(selectedPlanet.getSpeed().getAngle()));
            setX.setHint("Current x-coordinate: " + round(selectedPlanet.getPosition().getX()));
            setY.setHint("Current x-coordinate: " + round(selectedPlanet.getPosition().getY()));
        }

        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //do something with edt.getText().toString();
                String mass = setMass.getText().toString().trim();
                String directionStr = setSpeedDirection.getText().toString().trim();
                String xStr = setX.getText().toString().trim();
                String yStr = setY.getText().toString().trim();
                String speedStr = setSpeedValue.getText().toString().trim();
                if (isEditingNewPlanet) {
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
                    //check if newly added planet overlapped
                    int m = Integer.parseInt(mass);
                    for (Planet p : Planet.planetList) {
                        if (position.distance(p.getPosition()) <= Math.pow(m, 0.5) + Math.pow(p.getMass(), 0.5)) {
                            Toast.makeText(getApplicationContext(), "Current position has planet", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    //create new planet
                    double speed = Integer.parseInt(speedStr);
                    new Planet(m, position, new Vector(speed * Math.cos(direction), speed * Math.sin(direction)));
                } else {
                    if (mass.length() != 0) {
                        selectedPlanet.setMass(Integer.parseInt(mass));
                    }
                    if (xStr.length() != 0) {
                        selectedPlanet.setPosition(new Vector(
                                Integer.parseInt(xStr),
                                selectedPlanet.getPosition().getY()
                        ));
                    }
                    if (yStr.length() != 0) {
                        selectedPlanet.setPosition(new Vector(
                                selectedPlanet.getPosition().getX(),
                                Integer.parseInt(yStr)
                        ));
                    }
                    if (directionStr.length() != 0 && speedStr.length() != 0) {
                        double direction = (Integer.parseInt(directionStr) % 360) / (2 * Math.PI);
                        double speed = Integer.parseInt(speedStr);
                        selectedPlanet.setSpeed(new Vector(
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
            }

        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, final int whichButton) {

            }
        });
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }
}
