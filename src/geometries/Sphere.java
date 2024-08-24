package geometries;

import primitives.Point;
import java.util.List;
import java.lang.Math;
import static primitives.Util.*;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;


public class Sphere extends RadialGeometry {
    /**
     * center point of the sphere
     */
    final private Point center;

    @Override
    public Vector getNormal(Point point) {
        return point.subtract(center).normalize();
    }

//    /**
//     * Constructor to initialize Sphere based on a center point and a radius of the sphere
//     *
//     * @param center center of the sphere
//     * @param radius radius of the sphere
//     */
//    public Sphere( Point center, double radius) {
//        super(radius);
//        this.center = center;
//    }

    /**
     * Constructs a Sphere with the specified center point and radius.
     *
     * @param p the center point of the sphere
     * @param r the radius of the sphere
     */
    public Sphere(Point p, double r) {
        super(r);
        center = p;
        // if CBR improvement is on
        if (cbr)
            // builds the sphere's box
            this.box = new Box(center.getX() - radius, center.getX() + radius, //
                    center.getY() - radius, center.getY() + radius, //
                    center.getZ() - radius, center.getZ() + radius);
    }

    public Point getCenter() {
        return center;
    }

    @Override
    public String toString() {
        return "Sphere [center=" + center + ", radius=" + radius + "]";
    }


    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance) {
        Point rayHead=ray.getHead();
        if (center.equals(rayHead))
            return List.of(new GeoPoint(this, ray.getPoint(radius)));

        // if the ray starts at the center of the sphere
        double tm = 0;
        double d = 0;
        double dSquared;
        Vector rayDirection=ray.getDirection();
        Vector l = center.subtract(rayHead);
        tm = l.dotProduct(rayDirection);
        d =Math.sqrt(Math.abs(l.lengthSquared() - tm * tm)); // d = (|L|^2 - tm^2)^0.5
        if (alignZero(d-radius)>=0) // d > radius->if the ray doesn't intersect the sphere
            return null;

        // computing the distance from the ray's start point to the intersection points
        double th = Math.sqrt(radius * radius - d * d);
        double t1 = alignZero(tm - th);
        double t2 = alignZero(tm + th);
        if (t2 <= 0 || Util.alignZero(t1 - maxDistance) >= 0)
            return null;
        if (Util.alignZero(t2 - maxDistance) >= 0)
            return t1 > 0 ? List.of(new GeoPoint(this, ray.getPoint(t1))) : null;
        return t1 > 0
                ? List.of(new GeoPoint(this, ray.getPoint(t1)), new GeoPoint(this, ray.getPoint(t2)))
                : List.of(new GeoPoint(this, ray.getPoint(t2)));

    }

    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
        // get ray point and vector
        Point p0 = ray.getHead();
        Vector v = ray.getDirection();

        // vector between p0 start and sphere center-O
        Vector u = null;
        try {
            u = center.subtract(p0);
        } catch (Exception ex)// p0=center
        {
            // return p0 + r*v
            return List.of(new GeoPoint(this, ray.getPoint(radius)));
        }

        double tm = v.dotProduct(u);

        // get the distance between the ray and the sphere center
        double d2 = alignZero(u.lengthSquared() - tm * tm);

        // the ray tangent the sphere
        double delta2 = alignZero(radiusSquared - d2);
        if (delta2 <= 0) // if d=radius
            return null;

        // the ray crosses the sphere in two places
        double th = Math.sqrt(delta2);
        // get the distance to the two points
        double t2 = alignZero(tm + th);
        if (t2 <= 0)
            return null;

        double t1 = alignZero(tm - th);
        return t1 <= 0 //
                ? List.of(new GeoPoint(this, ray.getPoint(t2))) // P2
                : List.of(new GeoPoint(this, ray.getPoint(t1)), new GeoPoint(this, ray.getPoint(t2))); // P1 , P2
    }
}

//
//package geometries;
//
//import primitives.Point;
//import primitives.Ray;
//import primitives.Util;
//import primitives.Vector;
//import static primitives.Util.*;
//import java.util.List;
//import static primitives.Util.alignZero;
//
//
///**
// * Represents a sphere in three-dimensional space.
// * This class extends RadialGeometry, inheriting its properties and methods.
// * @author Hadar Nagar & Elinoy Damari
// */
//public class Sphere extends RadialGeometry{
//
//    /** The center point of the sphere. */
//    final Point center;
//
//    /**
//     * Constructs a sphere with the specified radius and center point.
//     * @param radius The radius of the sphere.
//     * @param center The center point of the sphere.
//     */
//    public Sphere(double radius, Point center) {
//        super(radius);
//        this.center = center;
//    }
//
//
//    @Override
//    public Vector getNormal(Point point) {
//        return (point.subtract(center)).normalize();
//    }
//
//    @Override
//    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance) {
//        Point rayHead=ray.getHead();
//        if (center.equals(rayHead))
//            return List.of(new GeoPoint(this, ray.getPoint(radius)));
//
//        // if the ray starts at the center of the sphere
//        double tm = 0;
//        double d = 0;
//        double dSquared;
//        Vector rayDirection=ray.getDirection();
//        Vector l = center.subtract(rayHead);
//        tm = l.dotProduct(rayDirection);
//        d =Math.sqrt(Math.abs(l.lengthSquared() - tm * tm)); // d = (|L|^2 - tm^2)^0.5
//        if (alignZero(d-radius)>=0) // d > radius->if the ray doesn't intersect the sphere
//            return null;
//
//        // computing the distance from the ray's start point to the intersection points
//        double th = Math.sqrt(radius * radius - d * d);
//        double t1 = alignZero(tm - th);
//        double t2 = alignZero(tm + th);
//        if (t2 <= 0 || Util.alignZero(t1 - maxDistance) >= 0)
//            return null;
//        if (Util.alignZero(t2 - maxDistance) >= 0)
//            return t1 > 0 ? List.of(new GeoPoint(this, ray.getPoint(t1))) : null;
//        return t1 > 0
//                ? List.of(new GeoPoint(this, ray.getPoint(t1)), new GeoPoint(this, ray.getPoint(t2)))
//                : List.of(new GeoPoint(this, ray.getPoint(t2)));
//
//    }
//
//    @Override
//    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
//        // get ray point and vector
//        Point p0 = ray.getHead();
//        Vector v = ray.getDirection();
//
//        // vector between p0 start and sphere center-O
//        Vector u = null;
//        try {
//            u = center.subtract(p0);
//        } catch (Exception ex)// p0=center
//        {
//            // return p0 + r*v
//            return List.of(new GeoPoint(this, ray.getPoint(radius)));
//        }
//
//        double tm = v.dotProduct(u);
//
//        // get the distance between the ray and the sphere center
//        double d2 = alignZero(u.lengthSquared() - tm * tm);
//
//        // the ray tangent the sphere
//        double delta2 = alignZero(radius2 - d2);
//        if (delta2 <= 0) // if d=radius
//            return null;
//
//        // the ray crosses the sphere in two places
//        double th = Math.sqrt(delta2);
//        // get the distance to the two points
//        double t2 = alignZero(tm + th);
//        if (t2 <= 0)
//            return null;
//
//        double t1 = alignZero(tm - th);
//        return t1 <= 0 //
//                ? List.of(new GeoPoint(this, ray.getPoint(t2))) // P2
//                : List.of(new GeoPoint(this, ray.getPoint(t1)), new GeoPoint(this, ray.getPoint(t2))); // P1 , P2
//    }
//}
