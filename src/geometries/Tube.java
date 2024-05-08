/**
 * Class representing a tube in 3D space.
 *
 * A tube is defined by a radius and a central axis represented by a ray.
 *
 * @author Efrat and Moy
 */
package geometries;

import primitives.Ray;

public class Tube {

    /**
     * The radius of the tube.
     */
    private final double radius;

    /**
     * The ray representing the central axis of the tube.
     */
    private final Ray r;

    /**
     * Constructor for creating a tube object.
     *
     * @param radius The radius of the tube.
     * @param r The ray representing the central axis of the tube.
     */
    public Tube(double radius, Ray r) {
        this.radius = radius;
        this.r = r;
    }

    // ... other methods specific to Tube ...
}

