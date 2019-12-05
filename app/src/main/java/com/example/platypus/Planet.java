package com.example.platypus;



import android.graphics.Color;
import android.graphics.Path;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Planet {

    public static List<Planet> planetList = new ArrayList<>();
    private static final double GRAVATATIONAL = 6.67 * Math.pow(10, 0);
    private double mass;
    private Vector position;
    private Vector speed;
    private int color;
    public Path path = new Path();


    Planet(final double setMass, final Vector setPosition, final Vector setSpeed) {
        this.mass = setMass;
        this.position = setPosition;
        this.speed = setSpeed;
        planetList.add(this);
        Random rand = new Random();
        this.color = Color.rgb(rand.nextFloat() + 0.1f, rand.nextFloat() + 0.1f, rand.nextFloat() + 0.1f);
        path.moveTo((float) position.getX(), (float) position.getY());
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
                double cos = (p.getPosition().getX() - position.getX()) / distance;
                double sin = (p.getPosition().getY() - position.getY()) / distance;
                Vector forceVector = new Vector(force * cos, force * sin);
                netForce.add(forceVector);
            }
        }
        return netForce;
    }

    public boolean update(final double time) {
        if (isCrashed()) {
            return false;
        }

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
        path.lineTo((float) position.getX(), (float) position.getY());
        return true;
    }

    private boolean isCrashed() {
        for (Planet p : planetList) {
            if (p.equals(this)) {
                continue;
            } else {
                if (this.getPosition().distance(p.getPosition()) <= Math.pow(this.mass, 0.5) + Math.pow(p.mass, 0.5)) {
                    planetList.remove(p);
                    planetList.remove(this);
                    return true;
                }
            }
        }
        return false;
    }

    public int getColor() {
        return this.color;
    }

    public Vector getSpeed() {
        return this.speed;
    }


}
