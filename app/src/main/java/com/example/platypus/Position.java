package com.example.platypus;

public class Position {
    private double xCoordinate;
    private double yCoordinate;

    Position(final double setXCoordinate, final double setYCoordinate) {
        this.xCoordinate = setXCoordinate;
        this.yCoordinate = setYCoordinate;
    }

    /**
     * get current x position.
     * @return return x coordinate in double.
     */
    public double getxCoordinate() {
        return this.xCoordinate;
    }

    /**
     * get current y position
     * @return return current y coordinate in double
     */
    public double getyCoordinate() {
        return this.yCoordinate;
    }

    /**
     * update current x position
     * @param setXCoordinate double value represents latest x coordinates.
     */
    public void setxCoordinate(final double setXCoordinate) {
        this.xCoordinate = setXCoordinate;
    }

    /**
     * update current y position
     * @param setYCoordinate double value represents latest y coordinates.
     */
    public void setyCoordinate(final double setYCoordinate) {
        this.yCoordinate = setYCoordinate;
    }

    /**
     * get angle of two position reference.
     * @param position return a double value representing the angle. (scaled to -2pi - 2pi)
     * @return
     */
    public double getAngle(Position position) {
        if (position == null) {
            return 0;
        }

        double deltaY = position.getyCoordinate() - this.yCoordinate;
        double deltaX = position.getxCoordinate() - this.xCoordinate;

        return (Math.atan2(deltaY, deltaX));
    }

    public double getDistance(Position position) {
        double deltaY = position.getyCoordinate() - this.yCoordinate;
        double deltaX = position.getxCoordinate() - this.xCoordinate;

        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }
}
