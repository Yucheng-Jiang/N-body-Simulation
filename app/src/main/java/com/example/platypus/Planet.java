package com.example.platypus;


import android.graphics.PointF;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Planet {
    public static final double GRAVATATIONAL = 6.7 * Math.pow(10, -11);
    public static List<Planet> planets = new ArrayList<>();
    private double mass;
    private Position position;
    private Force netForce;
    private Speed speed;

    Planet(final double setMass, final Position setPosition, final Speed setSpeed) {
        this.mass = setMass;
        this.position = setPosition;
        this.netForce = calcNetForce();
        this.speed = setSpeed;
        planets.add(this);
    }


    public double getMass() {
        return mass;
    }

    public Position getPosition() {
        return position;
    }

    public double getDistance(Planet planet) {
        return this.position.getDistance(planet.getPosition());
    }

    public Force calcNetForce() {
        if (planets == null || planets.size() == 0) {
            return new Force(0, 0);
        }

        for (int i = 0; i < planets.size(); i++) {
            Planet tempPlanet = planets.get(i);
            double force = GRAVATATIONAL * this.mass * tempPlanet.getMass()
                    / Math.pow(getDistance(tempPlanet), 2);
            double angle = this.position.getAngle(tempPlanet.getPosition());
            double xForce = Math.cos(angle) * force;
            double yForce = Math.sin(angle) * force;

            netForce.addForce(new Force(xForce, yForce));
        }

        return netForce;
    }

    public void updateLocation(final int time) {
        double xAcceleration = netForce.getxForce() / this.mass;
        double yAcceleratoin = netForce.getyForce() / this.mass;

        position.setxCoordinate(this.position.getxCoordinate() + xAcceleration * time * time / 2);
        position.setyCoordinate(this.position.getyCoordinate() + yAcceleratoin * time * time / 2);
    }
}
