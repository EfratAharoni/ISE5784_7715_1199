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

    /**
     * Constructor to initialize Sphere based on a center point and a radius of the sphere
     *
     * @param center center of the sphere
     * @param radius radius of the sphere
     */
    public Sphere(double radius, Point center) {
        super(radius);
        this.center = center;
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

//    @Override
//    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray) { //(-1,0,0) (3,1,0)
//        // if the ray starts at the center of the sphere
//        double tm = 0;
//        double d = 0;
//        if (!center.equals(ray.getHead())){ // if the ray doesn't start at the center of the sphere
//            Vector L = center.subtract(ray.getHead());
//            tm = L.dotProduct(ray.getDirection());
//            d =L.lengthSquared() - tm * tm; // d = (|L|^2 - tm^2)
//            if (d < 0)
//                d = -d;
//            d = Math.sqrt(d);
//        }
//        if (d > radius) // if the ray doesn't intersect the sphere
//            return null;
//        // computing the distance from the ray's start point to the intersection points
//        double th = Math.sqrt(radius * radius - d * d);
//        double t1 = tm - th;
//        double t2 = tm + th;
//        if (t1 <= 0 && t2 <= 0)
//            return null;
//        if (Util.alignZero(t2) == 0) // if the ray is tangent to the sphere
//            return null;
//        if (th == 0)
//            return null;
//        if (t1 <= 0){ // if the ray starts inside the sphere or the ray starts after the sphere
//            return List.of(new GeoPoint(this,ray.getPoint(t2)));
//        }
//        if (t2 <= 0) { //if the ray starts after the sphere
//            return List.of(new GeoPoint(this,ray.getPoint(t1)));
//        }
//        return List.of(new GeoPoint(this,ray.getPoint(t1)),new GeoPoint(this, ray.getPoint(t2))); // if the ray intersects the sphere twice
//    }
}


