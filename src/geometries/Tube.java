package geometries;

import primitives.Ray;

public class Tube {
    private final double radius;
    private final Ray r;

    public Tube(double radius, Ray r) {
        this.radius = radius;
        this.r = r;
    }
}
