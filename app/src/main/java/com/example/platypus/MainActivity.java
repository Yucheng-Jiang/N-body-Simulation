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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


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
    private Button edit;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // project starts here
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        //test add planets
        massText = findViewById(R.id.MassText);
        speedText = findViewById(R.id.SpeedText);
        positionText = findViewById(R.id.PositionText);
        delete = findViewById(R.id.Delete);
        edit = findViewById(R.id.Edit);

        Planet.planetList.clear();
        new Planet(3000, new Vector(0,0), new Vector(0, 0));
        new Planet(50, new Vector(300,300), new Vector(-3.5, 3));
        new Planet(50, new Vector(800, 800), new Vector(-2.5, 1.5));


        CustomView customView = findViewById(R.id.customView);
        Button testButton = findViewById(R.id.testButton);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customView.setmPosXY(100, 100);
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
                updateData(currentPlanet);
                if (currentPlanet == null) {
                    /*
                    delete.setVisibility(View.GONE);
                    edit.setVisibility(View.GONE);
                    return;

                     */
                } else {
                    if (Planet.planetList.size() > 1) {
                        delete.setVisibility(View.VISIBLE);
                        delete.setOnClickListener(unused -> {
                            Planet.planetList.remove(currentPlanet);
                            handler.post(runnable);

                            /*
                            if (Planet.planetList.size() == 0) {
                                delete.setVisibility(View.GONE);
                                currentPlanet = null;
                            }

                             */
                            updateData(currentPlanet);

                            if (Planet.planetList.size() == 0) {
                                new Planet(50, new Vector(0, 0), new Vector(0,0));
                                Toast.makeText(getApplicationContext(), "Default Planet Created", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    edit.setVisibility(View.VISIBLE);
                    edit.setOnClickListener(unused -> {
                        editInfo(false);
                        handler.post(runnable);
                    });
                }
            }
        };

        Button startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRunning == false) {
                    startButton.setText("Stop");
                    isRunning = true;
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            for (Planet p : Planet.planetList) {
                                if (!p.update(0.1)) {
                                    vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                                    if (Planet.planetList.size() == 0) {
                                        new Planet(50, new Vector(0, 0), new Vector(0,0));
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "Default Planet Created", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }
                                    handler.post(runnable);
                                    return;
                                }
                            }
                            handler.post(runnable1);
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

        spinner = findViewById(R.id.spinner);
        updateSpinner();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //TextView textViewMass = findViewById(R.id.mass);
                //textViewMass.setText(Planet.planetList.get(position).getMass());
                if (parent.getItemAtPosition(position).equals("Add Planet")) {
                    editInfo(true);
                    handler.post(runnable);
                } else if (parent.getItemAtPosition(position).equals("Special Planet"))  {
                    Planet.planetList.clear();
                    new SpecialPlanet(100, new Vector(0,0), new Vector(0, 0));
                    handler.post(runnable);
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
        list.add("Special Planet");
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
                + " y = " + round(planet.getSpeed().getY()));
    }
    public static String round(double value) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return decimalFormat.format(value);
    }
    public void editInfo(boolean isNew) {
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
                    int x = Integer.parseInt(xStr);
                    int y = Integer.parseInt(yStr);
                    double speed = Integer.parseInt(speedStr);
                    new Planet(Integer.parseInt(mass),
                            new Vector(x, y),
                            new Vector(speed * Math.cos(direction), speed * Math.sin(direction)));
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
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        updateSpinner();
                    }
                };
                handler.post(runnable);
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        //
    }

}
