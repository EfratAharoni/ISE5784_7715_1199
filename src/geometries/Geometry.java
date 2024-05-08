/**
 * Interface representing a geometric object in 3D space.
 *
 * @author Efrat and Moy
 */
package geometries;

import primitives.Point;
import primitives.Vector;

public interface Geometry {

    /**
     * Calculates the normal vector to the geometry at a specific point on its surface.
     *
     * The normal vector is a vector perpendicular to the surface of the geometry at the given point.
     *
     * @param point The point on the surface of the geometry.
     * @return The normal vector to the geometry at the given point.
     *
     * @throws IllegalArgumentException if the point is not on the surface of the geometry.
     */
    public Vector getNormal(Point point);
}

