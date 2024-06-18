/**
 * Interface representing a geometric object in 3D space.
 *
 * @author Efrat and Moy
 */
package geometries;

import primitives.Point;
import primitives.Vector;
import primitives.*;


public abstract class Geometry extends Intersectable {
    /**
     * The emission color of the geometry.
     */
    protected Color emission = Color.BLACK;

    /**
     * The material of the geometry.
     */
    private Material material = new Material();

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

    /**
     * Returns the material of the geometry.
     *
     * @return The geometry material.
     */
    public Material getMaterial()
    {
        return material;
    }

    /**
     * Sets the material of the geometry.
     *
     * @param material The new material.
     * @return This Geometry object (builder pattern).
     */
    public Geometry setMaterial(Material material)
    {
        this.material = material;
        return this;
    }

    public abstract Vector getNormal(Point p0);
}

