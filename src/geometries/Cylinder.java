/**
 * Class representing a cylinder.
 *
 * @author Efrat and Moy
 */
package geometries;

import primitives.*;
import static primitives.Util.isZero;

import java.util.List;

public class Cylinder extends Tube {
    /**
     * height of the tube
     */
    final private double height;

    /**
     * Constructor to initialize Cylinder based on given axis ray, radius, and height
     *
     * @param axisRay axis ray of the cylinder
     * @param radius  radius of the cylinder
     * @param height  height of the cylinder
     */
    public Cylinder(Ray axisRay, double radius, double height) {
        super(axisRay, radius);
        this.height = height;
    }

    @Override
    public Vector getNormal(Point point) {
        // if the given point collides with the base point of the axis ray, just return the normal vector (dir)
        if (point.equals(this.axisRay.getHead())) return this.axisRay.getDirection();

        //calculating distance of the given point from base point of the axis ray
        double t = this.axisRay.getDirection().dotProduct(point.subtract(this.axisRay.getHead()));
        //if the given point is on one of the bases of the cylinder, we just return a normal vector to the base (dir)
        if (isZero(t) || isZero(t - this.height)) return this.axisRay.getDirection();
        return super.getNormal(point);
    }
}

