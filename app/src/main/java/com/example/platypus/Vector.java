package com.example.platypus;

public class Vector {
    private double x;
    private double y;
    Vector() {
        x = 0;
        y = 0;
    }
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
    public void add(double xx, double yy) {
        x = x + xx;
        y = y + yy;
    }
    public void minus(final Vector v) {
        x = x - v.getX();
        y = y - v.getY();
    }
    public Vector getMinus(final Vector v) {
        return new Vector(x - v.getX(), y - v.getY());
    }
    public void multiply(final double d) {
        x *= d;
        y *= d;
    }
    public void set(double xx, double yy) {
        x = xx;
        y = yy;
    }

    public double getModulus() {
        return Math.pow(Math.pow(x, 2) + Math.pow(y, 2), 0.5);
    }

    public double getAngle() {
        return Math.atan(y / x) / (Math.PI * 2) * 360;
    }
}
