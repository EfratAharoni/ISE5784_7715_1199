package geometries;

import primitives.*;

import java.util.List;

/**
 *
 */
public interface Intersectable {
    /**
     *
     * @param ray  A method that receives a ray
     * @return a list of intersection points between the ray and geometry
     */
    List<Point> findIntersections(Ray ray);
}
