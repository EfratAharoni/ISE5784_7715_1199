/**
 * Interface representing a geometric object in 3D space.
 *
 * @author Efrat and Moy
 */
package geometries;

import primitives.Point;
import primitives.Vector;

import java.awt.*;

public abstract class Geometry extends Intersectable {
    /**
     * The emission color of the geometry.
     */
    protected Color emission = Color.BLACK;

    /**
     * Returns the emission color of the geometry.
     *
     * @return The emission color.
     */
    public Color getEmission() {
        return emission;
    }

    /**
     * Returns the material of the geometry.
     *
     * @return The geometry material.
     */
    public Geometry setEmission(Color emission) {
        this.emission = emission;
        return this;
    }

    public abstract Vector getNormal(Point p0);
}

