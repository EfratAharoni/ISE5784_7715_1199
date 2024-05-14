/**
 * Interface representing a geometric object in 3D space.
 *
 * @author Efrat and Moy
 */
package geometries;

import primitives.Point;
import primitives.Vector;
import primitives.*;

public interface Geometry {


    public abstract Vector getNormal(Point p0);
}

