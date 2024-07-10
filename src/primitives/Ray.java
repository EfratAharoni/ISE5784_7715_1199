/**
 * Class representing a ray in 3D space.
 *
 * A ray is defined by a starting point (head) and a direction vector.
 *
 * @author Moy and Efrat
 */
package primitives;


import java.util.List;
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
    private static final double DELTA = 0.1;

    public Ray(Point p, Vector v, Vector normalToP){
        double res = v.dotProduct(normalToP);
        head = Util.isZero(res) ? p : res > 0 ? p.add(normalToP.scale(DELTA)) : p.add(normalToP.scale(-DELTA));
        direction=v.normalize();
    }

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

    /**
     * Finds and returns the closest point to the head of the ray from a list of points.
     *
     * @param points the list of points to search.
     * @return the closest point to the head of the ray, or null if the list is empty.
     */
    public Point findClosestPoint(List<Point> points){
        return points == null||points.size()==0 ? null
                : findClosestGeoPoint(points.stream()
                .map(p -> new GeoPoint(null, p)).toList()).point;
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

