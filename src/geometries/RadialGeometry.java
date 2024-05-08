/**
 * Abstract class representing a geometric object with a radial component (e.g., sphere, cylinder, cone).
 *
 * Subclasses should implement specific behavior for their geometry.
 *
 * @author Efrat and Moy
 */
package geometries;

import primitives.Point;
import primitives.Vector;

public abstract class RadialGeometry implements Geometry {

    /**
     * The radius of the radial geometry.
     */
    protected final double radius;

    /**
     * Constructor for creating a radial geometry object.
     *
     * @param radius The radius of the geometry.
     */
    public RadialGeometry(double radius) {
        this.radius = radius;
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

