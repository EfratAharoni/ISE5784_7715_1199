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
import java.util.List;

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

    /**
     * returns the closest point to the ray's origin point
     *
     * @param points points to check
     * @return closest point
     */
    public Point findClosestPoint(List<Point> points) {
        if (points.isEmpty())
            return null;
        Point closestPoint=points.get(0);
        double minimumDistanceSquared=closestPoint.distanceSquared(head);
        double distanceSquared;
        for(Point point:points){
            distanceSquared=point.distanceSquared(head);
            if(distanceSquared<minimumDistanceSquared){
                minimumDistanceSquared=distanceSquared;
                closestPoint=point;
            }
        }
        return closestPoint;
    }


}

