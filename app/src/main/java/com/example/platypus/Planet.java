package com.example.platypus;


import android.graphics.PointF;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Planet {
    public static final double GRAVATATIONAL = 6.7 * Math.pow(10, -11);
    private double mass;
    private Vector<Double> position;
    private Vector<Double> speed;

    Planet(final double setMass, final Vector<Double> setPosition) {
        this.mass = setMass;
        this.position = setPosition;
    }


    public double getMass() {
        return mass;
    }

    public Vector<Double> getPosition() {
        return position;
    }

    public double getDistance(Planet planet) {
        planet.getPosition().add(this.position);
    }


    public Vector calcNetForce(final List<Planet> a) {
        List<Vector<Double>> force = new ArrayList<>();
        for (int i = 0; i < a.size(); i++) {
            a.get(i).getMass() * this.mass *
        }
    }
}
