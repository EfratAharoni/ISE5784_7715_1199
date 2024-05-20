package geometries;

import primitives.Point;
import java.util.List;
import java.lang.Math;
import primitives.*;
import static primitives.Util.*;

public class Sphere implements Geometry {
    /**
     * center point of the sphere
     */
    final private Point center;

    @Override
    public Vector getNormal(Point point) {
        return point.subtract(center).normalize();
    }

    /**
     * Constructor to initialize Sphere based on a center point and a radius of the sphere
     *
     * @param center center of the sphere
     * @param radius radius of the sphere
     */
    public Sphere(Point center, double radius) {
        super();
        this.center = center;
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        return null;
    }
}


