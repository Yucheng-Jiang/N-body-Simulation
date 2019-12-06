package com.example.platypus;



import android.graphics.Color;
import android.graphics.Path;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;



public class Planet {

    public static List<Planet> planetList = new ArrayList<>();
    private static final double GRAVATATIONAL = 6.67 * Math.pow(10, 2);
    private double mass;
    private Vector position;
    private Vector speed;
    private int color;
    public Path path = new Path();
    private Vector positionToAdd;
    private Vector speedToAdd;

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
        for (Planet p : Planet.planetList) {
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

    public void calcToMove(final double time) {
        Vector acceleration = new Vector(
                calcNetForce().getX() / mass,
                calcNetForce().getY() / mass
        );
        speedToAdd = new Vector(
                time * calcNetForce().getX() / mass,
                time * calcNetForce().getY() / mass
        );
        positionToAdd = new Vector(
                speed.getX() * time + Math.pow(time, 2) * acceleration.getX() / 2,
                speed.getY() * time + Math.pow(time, 2) * acceleration.getY() / 2
        );
    }

    public boolean update() {
        position.add(positionToAdd);
        speed.add(speedToAdd);
        path.lineTo((float) position.getX(), (float) position.getY());

        if (isCrashed()) {
            return false;
        } else {
            return true;
        }
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

    public void setMass(int setMass) {
        this.mass = setMass;
    }

    public void setSpeed(Vector speed) {
        this.speed = speed;
    }

    public void setPosition(Vector position) {
        this.position = position;
    }




}
