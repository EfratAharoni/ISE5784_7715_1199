package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.*;

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
    public List<Point> findIntersections(Ray ray) {
        Vector v=ray.getDirection();
        double nv=normal.dotProduct(v);
        if (isZero(nv))
            return null;
        if(p.equals(ray.getHead()))
            return null;
        double t=alignZero((normal.dotProduct((p.subtract(ray.getHead()))))/(nv));
        if(t>0)
            return List.of(ray.getPoint(t));
        return null;
    }
}
