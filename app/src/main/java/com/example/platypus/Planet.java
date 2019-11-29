package com.example.platypus;


import android.graphics.PointF;

import java.util.ArrayList;
import java.util.List;

public class Planet {
    public static List<Planet> planetList = new ArrayList<>();
    private static final double GRAVATATIONAL = 6.67 * Math.pow(10, 1);
    private double mass;
    private Vector position;
    private Vector speed;

    Planet(final double setMass, final Vector setPosition, final Vector setSpeed) {
        this.mass = setMass;
        this.position = setPosition;
        this.speed = setSpeed;
        planetList.add(this);
    }


    public double getMass() {
        return mass;
    }

    public Vector getPosition() {
        return position;
    }

    private Vector calcNetForce() {
        Vector netForce = new Vector(0, 0);
        for (int i = 0; i < planetList.size(); i++) {
            Planet p = planetList.get(i);
            if (p != this) {
                double distance = p.position.distance(position);
                double force = GRAVATATIONAL * mass * p.getMass()
                        / Math.pow(distance, 2);
                double cos = position.getX() / distance;
                double sin = position.getY() / distance;
                Vector forceVector = new Vector(force * cos, force * sin);
                netForce.add(forceVector);
            }
        }
        System.out.println(netForce.distance(new Vector(0, 0)));
        return netForce;
    }

    public void update(final int time) {
        Vector acceleration = new Vector(
                calcNetForce().getX() / mass,
                calcNetForce().getY() / mass
        );
        Vector speedToAdd = new Vector(
                time * calcNetForce().getX() / mass,
                time * calcNetForce().getY() / mass
        );

        Vector positionToAdd = new Vector(
                speed.getX() * time + Math.pow(time, 2) * acceleration.getX() / 2,
                speed.getY() * time + Math.pow(time, 2) * acceleration.getY() / 2
        );
        position.add(positionToAdd);
        speed.add(speedToAdd);
    }
}
