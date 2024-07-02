package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * LightSource interface to Light Source operations
 *
 * @author Yael and Shira
 *
 */
public interface LightSource {
    /**
     * get point and return the Intensity of this light on this point
     *
     * @param p The point for which we want to know the light intensity
     * @return color of this point
     */
    public Color getIntensity(Point p);

    /**
     * get point and return the vector from the light to the point
     *
     * @param p The point for which we want to know the direction to the light source
     * @return vector direction to light source
     */
    public Vector getL(Point p);
    double getDistance(Point point);
}