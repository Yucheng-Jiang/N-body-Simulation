package com.example.platypus;

import android.graphics.Color;

import java.util.Random;

public class SpecialPlanet extends Planet {
    private double t = 0.13;
    SpecialPlanet(final double setMass, final Vector setPosition, final Vector setSpeed) {
        super(setMass, setPosition, setSpeed);
        Random rand = new Random();
        this.color = Color.rgb(rand.nextFloat() + 0.1f, rand.nextFloat() + 0.1f, rand.nextFloat() + 0.1f);
        path.moveTo((float) position.getX(), (float) position.getY());
    }

    public boolean update(final double time) {
        if (isCrashed()) {
            return false;
        }

        /*
        double r =  2 - 2 * Math.sin(t) * Math.pow(Math.abs(Math.cos(t)), 0.5) * Math.sin(t) / (Math.sin(t) + 1.4);
        double x = 150 * r * Math.cos(t);
        double y = 150 * r * Math.sin(t);

         */
        double x = 16 * Math.pow(Math.sin(t), 3);
        double y =  13 * Math.cos(t) - 5 * Math.cos(2 * t) - 2 * Math.cos(3 * t) - Math.cos(4 * t);
        setPosition(new Vector(x * 80, -y * 80));
        path.lineTo((float) position.getX(), (float) position.getY());
        t  = (t + 0.05) % (2 * Math.PI);

        return true;
    }
}
