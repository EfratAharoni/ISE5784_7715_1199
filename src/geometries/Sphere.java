package geometries;

import primitives.Point;
import java.util.List;
import java.lang.Math;
import primitives.*;
import static primitives.Util.*;

public class Sphere extends RadialGeometry {
    /**
     * center point of the sphere
     */
    final private Point center;
    /**
     * Constructor to initialize Sphere based on a center point and a radius of the sphere
     *
     * @param center center of the sphere
     * @param radius radius of the sphere
     */
    public Sphere(Point center, double radius) {
        super(radius);
        this.center = center;
    }
}


