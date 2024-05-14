/**
 * Abstract class representing a geometric object with a radial component (e.g., sphere, cylinder, cone).
 *
 * Subclasses should implement specific behavior for their geometry.
 *
 * @author Efrat and Moy
 */
package geometries;

import static primitives.Util.alignZero;
import primitives.Point;
import primitives.Vector;

public abstract class RadialGeometry implements Geometry {
    /**
     * radius of the geometry
     */
    final protected double radius;
    /**
     * squared radius of the geometry
     */
    final protected double radiusSquared;
    /**
     * Constructor for creating a radial geometry object.
     *
     * @param radius The radius of the geometry.
     */

    /**
     * Constructor to initialize radialGeometry based on a radius
     *
     * @param radius radius of the geometry
     * @throws IllegalArgumentException if radius &lt;= 0
     */
    public RadialGeometry(double radius) {
        if (alignZero(radius) <= 0)
            throw new IllegalArgumentException("radius cannot be less than or equal to zero");
        this.radius = radius;
        this.radiusSquared = radius * radius;
    }

    /**
     * Inherited method from Geometry interface.
     * Needs to be implemented by subclasses to provide specific normal vector calculation for their geometry.
     *
     * @param point The point on the surface of the geometry.
     * @return The normal vector to the geometry at the given point. (implementation by subclass)
     *
     * @throws UnsupportedOperationException This method is not implemented in the abstract RadialGeometry class.
     *         Subclasses must implement their own normal vector calculation logic.
     */
    @Override
    public Vector getNormal(Point point) {
        throw new UnsupportedOperationException("getNormal is not implemented in RadialGeometry. Implement it in subclasses.");
    }
}

