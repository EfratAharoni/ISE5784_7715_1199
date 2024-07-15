package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import primitives.Util;

import java.util.List;

import static primitives.Util.*;

public class Plane extends Geometry {
    final Point p;
    final Vector normal;

    public Plane(Point p1, Point p2, Point p3) {
        if (p1.equals(p2) || p2.equals(p3) || p1.equals(p3))
            throw new IllegalArgumentException("can't create plane with less than 3 different points");
        Vector v1 = p2.subtract(p1);
        Vector v2 = p3.subtract(p1);
        p = p1;
        try {
            normal = (v1.crossProduct(v2)).normalize();
        } catch (IllegalArgumentException zeroVectorIgnore) {
            throw new IllegalArgumentException("can't create plane with 3 points on the same line");
        }
    }

    /**
     * constructor that take existing plane by -
     *
     * @param p0     the point in the plane
     * @param normal the normal vector to the plane
     */
    public Plane(Point p0, Vector normal) {
        this.p = p0;
        this.normal = normal.normalize();
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

    public Vector getNormal() {
        return normal;
    }

    /**
     * Returns the reference point of the plane.
     */
    public Point getP() {
        return p;
    }

    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance) {
        Point rayHead = ray.getHead();
        if (p.equals(ray.getHead()))
            return null;
        Vector v = ray.getDirection();
        double nv = normal.dotProduct(v);
        if (isZero(nv))
            return null;
        double t = alignZero((normal.dotProduct((p.subtract(ray.getHead())))) / (nv));
        return t <= 0 || Util.alignZero(t - maxDistance) >= 0 ? null : List.of(new GeoPoint(this, ray.getPoint(t)));
    }
}
