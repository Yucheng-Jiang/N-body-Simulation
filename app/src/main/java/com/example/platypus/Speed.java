package com.example.platypus;

public class Speed {
    private double xSpeed;
    private double ySpeed;

    Speed(final double setXSpeed, final double setYSpeed) {
        this.xSpeed = setXSpeed;
        this.ySpeed = setYSpeed;
    }

    public void setxSpeed(final double setXSpeed) {
        this.xSpeed = setXSpeed;
    }

    public void setySpeed(final double setYSpeed) {
        this.ySpeed = setYSpeed;
    }

    public double getxSpeed() {
        return this.xSpeed;
    }

    public double getySpeed() {
        return this.ySpeed;

    }


}
