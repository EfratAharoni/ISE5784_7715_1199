/**
 * Class representing a ray in 3D space.
 *
 * A ray is defined by a starting point (head) and a direction vector.
 *
 * @author Moy and Efrat
 */
package primitives;

import java.util.Objects;
import static primitives.Util.isZero;
import geometries.Intersectable.GeoPoint;

public class Ray {

    /**
     * The starting point of the ray (head).
     */
    final public Point head;

    /**
     * The direction vector of the ray, normalized to unit length.
     */
    final public Vector direction;

    /**
     * Constructor for creating a ray.
     *
     * @param head The starting point of the ray.
     * @param direction The direction vector of the ray. The direction vector is normalized internally.
     */
    public Ray(Point head, Vector direction) {
        this.head = head;
        this.direction = direction.normalize();
    }

    public Vector getDirection() {
        return direction;
    }

    public Point getHead() {
        return head;
    }

    public Point getPoint(double t) {
        return isZero(t) ? head : head.add(direction.scale(t));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ray ray = (Ray) o;
        return Objects.equals(head, ray.head) && Objects.equals(direction, ray.direction);
    }

    @Override
    public String toString() {
        return "Ray{" +
                "head=" + head +
                ", direction=" + direction +
                '}';
    }

    public GeoPoint findClosestGeoPoint(List<GeoPoint> geoPoints)
    {
        if (geoPoints.isEmpty())
            return null;
        GeoPoint closestPoint=geoPoints.get(0);
        double minimumDistanceSquared=closestPoint.point.distanceSquared(head);
        double distanceSquared;
        for(GeoPoint geoPoint:geoPoints){
            distanceSquared=geoPoint.point.distanceSquared(head);
            if(distanceSquared<minimumDistanceSquared){
                minimumDistanceSquared=distanceSquared;
                closestPoint=geoPoint;
            }
        }
        return closestPoint;
    }


}

