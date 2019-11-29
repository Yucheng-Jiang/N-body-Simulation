package com.example.platypus;

public class Vector {
    private double x;
    private double y;
    Vector(final double setX, final double setY) {
        x = setX;
        y = setY;
    }
    Vector(final Vector a) {
        x = a.getX();
        y = a.getY();
    }
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public double distance(Vector v) {
        return Math.hypot(v.getX() - x, v.getY() - y);
    }
    public void add(Vector v) {
        x = x + v.getX();
        y = y + v.getY();
    }
}
