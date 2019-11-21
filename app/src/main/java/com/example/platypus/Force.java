package com.example.platypus;

public class Force {
    private double xForce;
    private double yForce;

    Force(final double setXForce, final double setYForce) {
        this.xForce = setXForce;
        this.yForce = setYForce;
    }

    public double getxForce() {
        return this.xForce;
    }

    public double getyForce() {
        return this.yForce;
    }

    public void setxForce(final double setXForce) {
        this.xForce = setXForce;
    }

    public void setyForce(final double setYForce) {
        this.yForce = setYForce;
    }

    public void addForce(Force force) {
        this.xForce += force.getxForce();
        this.yForce += force.getyForce();
    }

}
