/**
 * Interface representing a geometric object in 3D space.
 *
 * @author Efrat and Moy
 */
package geometries;

import primitives.Point;
import primitives.Vector;

public interface Geometry extends Intersectable {
    public abstract Vector getNormal(Point p0);
}

