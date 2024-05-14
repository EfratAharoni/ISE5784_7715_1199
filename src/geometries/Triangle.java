/**
 * Class representing a triangle in 3D space.
 *
 * This class inherits from the Polygon class.
 *
 * @author Efrat and Moy
 * @see Polygon
 */
package geometries;
import primitives.Point;

public class Triangle extends Polygon {

    /**
     * Constructor to initialize Triangle based on a normal vector and 3 points of the plane
     *
     * @param p1 first point of the triangle
     * @param p2 second point of the triangle
     * @param p3 third point of the triangle
     */
    public Triangle(Point p1, Point p2, Point p3) {
        super(p1, p2, p3);
    }
}


