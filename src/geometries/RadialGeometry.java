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

public abstract class RadialGeometry extends Geometry {
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

    @Override
    public Vector getNormal(Point point) {
       return null;
        //throw new UnsupportedOperationException("getNormal is not implemented in RadialGeometry. Implement it in subclasses.");
    }
}


