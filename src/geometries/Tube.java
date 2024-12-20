package geometries;

import primitives.Ray;
import primitives.Point;
import primitives.Vector;

import java.util.List;

/**
 * class Tube is a class representing a tube
 * of Euclidean geometry in Cartesian 3-Dimensional coordinate system.
 *
 * @author Efrat and Moy
 */
public class Tube extends RadialGeometry {
    /**
     * axis ray of the tube
     */
    final protected Ray axisRay;

    /**
     * Constructor to initialize Tube based on an axis ray and the radius of the tube
     *
     * @param axisRay axis ray of the tube
     * @param radius  radius of the tube
     */
    public Tube(Ray axisRay, double radius) {
        super(radius);
        this.axisRay = axisRay;
    }

    /**
     * getter for the axis ray of the Tube
     *
     * @return axis ray of the tube
     */
    @SuppressWarnings("unused")
    public Ray getAxisRay() {
        return axisRay;
    }

    @Override
    public Vector getNormal(Point point) {
         double t = this.axisRay.getDirection().dotProduct(point.subtract(this.axisRay.getHead()));
        return point.subtract(this.axisRay.getPoint(t)).normalize();
    }

    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance) {
        return null;
    }
}

