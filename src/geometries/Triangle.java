/**
 * Class representing a triangle in 3D space.
 *
 * This class inherits from the Polygon class.
 *
 * @author Efrat and Moy
 * @see Polygon
 */
package geometries;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import static primitives.Util.*;
import java.util.List;

public class Triangle extends Polygon {
    /**
     * Constructor to initialize Triangle based on a normal vector and 3 points of the plane
     *
     * @param p1 first point of the triangle
     * @param p2 second point of the triangle
     * @param p3 third point of the triangle
     */
    public Triangle(Point p1, Point p2, Point p3) {
        super(p1, p2, p3);
    }

    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance) {
        List<GeoPoint> intersections=plane.findGeoIntersections(ray,maxDistance);
        if(intersections==null)
            return null;
        intersections = List.of(new GeoPoint(this, intersections.get(0).point));
        Point rayHead=ray.getHead();
        Vector rayDiraction=ray.getDirection();
        Vector v1= vertices.get(0).subtract(rayHead);
        Vector v2= vertices.get(1).subtract(rayHead);
        Vector v3= vertices.get(2).subtract(rayHead);
        Vector n1=(v1.crossProduct(v2)).normalize();
        Vector n2=(v2.crossProduct(v3)).normalize();
        Vector n3=(v3.crossProduct(v1)).normalize();
        double vn1=alignZero(rayDiraction.dotProduct(n1));
        double vn2=alignZero(rayDiraction.dotProduct(n2));
        double vn3=alignZero(rayDiraction.dotProduct(n3));

        if(isZero(vn1)||isZero(vn2)||isZero(vn3))
            return null;
        if((vn1>0&&vn2>0&&vn3>0)||(vn1<0&&vn2<0&&vn3<0))
            return intersections;
        return null;
    }
}



