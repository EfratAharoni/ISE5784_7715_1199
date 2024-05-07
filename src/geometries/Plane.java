package geometries;

import primitives.Point;
import primitives.Vector;

public class Plane implements Geometry {
    protected final Point p;
    protected final Vector normal;
    public Plane(Point p1, Point p2, Point p3) {
        p = p1;
        normal = p1.subtract(p2).crossProduct(p2.subtract(p3)).normalize();
    }
    /**
     * constructor that take existing plane by -
     *
     * @param p0     the point in the plane
     * @param normal the normal vector to the plane
     */

    public Plane(Point p0, Vector normal) {
        this.p = p0;
        this.normal = normal;
    }

    /**
     * getter for normal
     *
     * @return the normal
     */
    @Override
    public Vector getNormal(Point point) {
        return normal;
    }
}
